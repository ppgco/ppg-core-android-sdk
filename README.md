# PushPushGo Core SDK for Android

[![JitPack](https://img.shields.io/jitpack/v/github/ppgco/ppg-core-android-sdk?style=flat-square)](https://jitpack.io/#ppgco/ppg-core-android-sdk)
![GitHub tag (latest)](https://img.shields.io/github/v/tag/ppgco/ppg-core-android-sdk?style=flat-square)
![GitHub Workflow Status (main)](https://img.shields.io/github/actions/workflow/status/ppgco/ppg-core-android-sdk/release.yml?style=flat-square)
![Discord](https://img.shields.io/discord/1108358192339095662?color=%237289DA&label=Discord&style=flat-square)
## Requirements:
Project integrated with FCM or HMS.

## Product Info

PushPushGo Core* is a building block for push notifications:
 - sender for push notifications - we handle batch requests, aggregate feedback events and inform your webhook
 - images storage & traffic - we handle, crop and serve your push images
 - fast implementation - we cover Android, iOS, Web with push notifications support
 - you own your database and credentials - no vendor lock-in - we provide infrastructure, sdk & support
 - simple API - we provide one API for all providers

Contact: support+core@pushpushgo.com or [Discord](https://discord.gg/NVpUWvreZa)

<sub>PushPushGo Core is not the same as PushPushGo product - if you are looking for [PushPushGo - Push Notifications Management Platform](https://pushpushgo.com)</sub>

## How it works

IMAGE HERE

When you send request to our API to send message, we prepare images and then connect to different providers. 

When message is delieverd to the device and interacts with user, we collect events and pass them to our API.

After a short time you will recieve package with events on your webhook:

```json
{
    "messages": [
        {
            "messageId": "8e3075f1-6b21-425a-bb4f-eeaf0eac93a2",
            "foreignId": "my_id",
            "result": {
                "kind": "sent"
            },
            "ts": 1685009020243
        },
        {
            "messageId": "8e3075f1-6b21-425a-bb4f-eeaf0eac93a2",
            "foreignId": "my_id",
            "result": {
                "kind": "delivered"
            },
            "ts": 1685009020564
        }
    ]
}
```

Using that data you can calculate statistics or do some of your business logic.

## 1. Add dependencies to your project

```gradle
// build.gradle (:app)
dependencies {  
// PPG Core jitpack
  implementation "com.github.ppgco:ppg-core-android-sdk:0.0.30"
// FCM - use this for the Android system
  implementation 'com.google.firebase:firebase-messaging-ktx:23.1.2'  
  implementation platform('com.google.firebase:firebase-bom:31.2.3')  
// HMS - use this for Harmony system
  implementation 'com.huawei.agconnect:agconnect-core:1.7.2.300'  
  implementation 'com.huawei.hms:push:6.7.0.300'   
}

// build.gradle (root) or settings.gradle (dependencyResolutionManagement)
allprojects {
    repositories {
        // jitpack
        maven { url 'https://jitpack.io' }
    }
}
```

## 2. Add required files
In case of example we store our files in **services** folder

### 2.1 For FCM - **MyFirebaseMessagingService** (/services/MyFirebaseMessagingService)
File should contain class which inherits from sdk's **FcmMessagingService** class.

Example file:

```kotlin
import com.pushpushgo.core_sdk.sdk.fcm.FcmMessagingService  
  
class MyFirebaseMessagingService : FcmMessagingService() {}
```
### 2.2 For HMS - **MyHMSMessagingService** (/services/MyHMSMessagingService)
File should contain class which inherits from sdk's **HmsMessagingService** class.
Example file:

```kotlin
import com.pushpushgo.core_sdk.sdk.hms.HmsMessagingService  
  
class MyHMSMessagingService : HmsMessagingService() {}
```

### 2.3 Create resources values
To satisfy required parameters for PPG Core sdk you should create .xml file with values.

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- Required fields  -->
    <string name="default_fcm_project_id">Get_value_from_google-services.json</string> 
    <string name="default_hms_app_id">Get_value_from_hms_developer_account</string>
    <!-- Choose HMS or FCM - remove other one -->
    <drawable name="default_notification_icon">@drawable/ic_launcher_foreground</drawable>
    <!-- Optional fields -->
    <string name="default_channel_id">ppg_core_default</string>
    <string name="default_channel_name">PPG Core Default Channel</string>
    <bool name="default_channel_badge_enabled">true</bool>
    <bool name="default_channel_vibration_enabled">true</bool>
    <string-array name="default_vibration_pattern">0, 1000, 500, 1500, 1000</string-array>
    <bool name="default_channel_lights_enabled">true</bool>
    <color name="default_lights_color">#ff00ff</color>
    <string name="default_channel_sound">magic_tone</string>
</resources>
```
Default channel badge is set to true by default. You can modify badge number in notification payload. (See send notification)

Vibration pattern - (before start delay, first vibration duration, between vibrations delay, second vibration duration, after vibrations delay). Values in milliseconds.

To set channel sound you have to place your sound file in `res/raw/` folder and set it's name in like in example above. (without extension) 

Support for adding more than one default channel will be added soon. 
> **IMPORTANT:** Be careful when setting notification channel options. Once a channel is set, its settings cannot be overriden. Consider testing different settings on staging environment before production release. However you can change name of default channel to create new channel with new settings.

## 3. AndroidManifest.xml

### 3.1 At the top of your manifest file add:
```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>  
<!-- If you want to support vibration -->
<uses-permission android:name="android.permission.VIBRATE"/>
```
### 3.2 Inside <*application*> tag add previously created service name:
#### For FCM
```xml
<service  
  android:name=".services.MyFirebaseMessagingService"  
  android:exported="false">  
  <intent-filter> 
    <action android:name="com.google.firebase.MESSAGING_EVENT" />  
  </intent-filter>
</service>
```
#### For HMS
```xml
<service
  android:name=".service.MyHMSMessagingService"
  android:exported="false">
  <intent-filter>
    <action android:name="com.huawei.push.action.MESSAGING_EVENT" />
  </intent-filter>
</service>
```
### 3.3 Inside main <*activity*> tag:
Specify launchMode:
```xml
<activity
  android:launchMode="singleTop"
  ...
```
also add intent filter for handling notification events:
```xml
<intent-filter>  
 <action android:name="PUSH_CLICK" />  
 <action android:name="PUSH_CLOSE" />  
</intent-filter>
```
to handle web/app/deepLinks redirects add another intent filter in your activity (this cannot be done in `.LAUNCHER` intent):
```xml
<intent-filter>  
 <action android:name="android.intent.action.VIEW" />  
 <category android:name="android.intent.category.DEFAULT" />  
 <category android:name="android.intent.category.BROWSABLE"/>  
 <data android:host="example.com"  
       android:scheme="app" />  
</intent-filter>
```
<*data*> tag allows deep linking from sources matching scheme://host. You can add more data tags to match more sources.

> **IMPORTANT:** multiple `<data>` elements in the same intent filter are actually merged together to match all variations of their combined attributes.

### 4. Usage
In your MainActivity file initialize ppgClient:

```kotlin
private val ppgClient: PpgCoreClient by lazy {  
  //this -> activity context
  PpgCoreClient(this)  
}
```
Add **ppgClient.onReceive(this, intent)** to MainActivity functions `onNewIntent, onCreate, onResume`:
```kotlin
override fun onNewIntent(intent: Intent?) {  
    super.onNewIntent(intent)  
    ppgClient.onReceive(this, intent)  
}

override fun onCreate(savedInstanceState: Bundle?) {  
    super.onCreate(savedInstanceState)  
    ppgClient.onReceive(this, intent)
}

override fun onResume() {  
    super.onResume()   
    ppgClient.onReceive(this, intent)  
}

```

Inside onCreate function you can implement registration for notifications.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ppgClient.onReceive(this, intent)
    ppgClient.register(this) {
        it?.let { subscription -> Log.d("Subscription", subscription.toJSON()) }
    }
}
```

**Save subscription in your database. Now you can send notifications via our API [Example JS sdk sender](https://github.com/ppgco/ppg-core-js-sdk/tree/main/examples/sender)**