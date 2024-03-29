# **CORE** _by PushPushGo_ SDK for Android

[![JitPack](https://img.shields.io/jitpack/v/github/ppgco/ppg-core-android-sdk?style=flat-square)](https://jitpack.io/#ppgco/ppg-core-android-sdk)
![GitHub tag (latest)](https://img.shields.io/github/v/tag/ppgco/ppg-core-android-sdk?style=flat-square)
![GitHub Workflow Status (main)](https://img.shields.io/github/actions/workflow/status/ppgco/ppg-core-android-sdk/release.yml?style=flat-square)
[![Discord](https://img.shields.io/discord/1108358192339095662?color=%237289DA&label=Discord&style=flat-square)](https://discord.gg/NVpUWvreZa)

## Product Info

**CORE** _by PushPushGo_ is a hassle-free building block for all your web and mobile push needs.

Send your transactional and bulk messages, and we'll take care of the rest.

#### What we offer:
 - Ready SDK for client/server integration - we have SDK for the most popular platforms.
 - Mobile and WebPush implementation (APNS, FCM, VAPID).
 - Transactional and bulk push notifications through API.
 - Hassle-free usage. Our servers handle traffic peaks and store images.
 - Event aggregation in bulk sent to your webhook for easy analysis or running your business logic.
 - GDPR-ready solution protecting private data in accordance with EU regulations.
 - No vendor lock-in - build and own your subscriber base (stateless solution).

#### What you get:
 - Cost-effective solution: pay for sent pushes, not for infrastructure, traffic, and storage.
 - Save time and effort on developing the sender and infrastructure.
 - Simple API interface for all channels with bulk support.
 - Support on our [Discord Server](https://discord.gg/NVpUWvreZa).
 - You can implement notification features your way.

#### Try it if:
 - You want to control the flow in your transactional messages and add a push notification channel.
 - You're looking for an easy push notifications tool for your organization, whether it's finance, e-commerce, online publishing, or any other sector.
 - You work in a software house and build solutions for your clients.
 - You want a hassle-free solution to focus on other tasks at hand.
 - You want to implement an on-premise solution for sending notifications.
 - You have issues with an in-house solution.
 - You're looking for a reliable provider and cooperation based on your needs.

## How it works

When client register for notifications you will get object with:
 - Credentials
 - Token/endpoint
 - Type of provider
 - Identifier of provider

We call this **Recipient** - it's your subscription data, store it in your database.

When you try to send message you will prepare:

 - **Bucket** - your temporary credentials bucket - this bucket can be reused any time, or recreated when credentials changed,

 - **Context** - your message - this context can be reused to send bulk messages or just used once when you send transactional message then is context is **temporary**

When you send message you will _authorize_ via **bucket** data, prepare message with **context** and send to **recipients** that can be bulked up to 1000 per request.

On the server side:
 - We validate and prepare the message body.
 - Then, we upload and resize images to our CDN.
 - Next, we connect and send to different providers.

On the client side:
 - Get notifications via our SDK in your App/Website.
 - When interacting with a notification, we collect events with our API.

On the server side:
 - We aggregate events and deliver them in bulk to your webhook endpoint.

### Architecture

![image](https://i.ibb.co/tst39rS/architecture.png "Architecture")

When a message is delivered to the device and interacts with the user, we collect events and pass them to our API. The collected events are resent to your webhook endpoint.

#### Webhooks events
During the journey of push we will trigger webhook events.

| Push Type    | Event      | Foreground | Background |
|---------|------------|------------|------------|
| Data    |            |            |            |
|         | delivered  | ✓          | ✓          |
|         | clicked    | ✓          | ✓          |
|         | sent       | ✓          | ✓          |
|         | close      | ✓          | ✓          |
| Silent<sup>1</sup>  |            |            |            |
|         | delivered  | ✓          | ✓          |
|         | sent       | ✓          | ✓          |

<small><sup>1</sup> - webpush doesn't support silent messages due to Push API implementation</small>

If `foreignId` field was passed with `receiver` then it will also be included in event in message.

Example events package:

```json
{
    "messages": [
        {
            "messageId": "8e3075f1-6b21-425a-bb4f-eeaf0eac93a2",
            "foreignId": "my_id",
            "result": {
                "kind": "delivered"
            },
            "ts": 1685009020243
        }
    ]
}
```

## Pricing

We charge $0.50 USD for every 1000 sent notifications.

## Open API Specification

Please visit our [Swagger](https://ppgco.github.io/ppg-core-open-api/#/)

## Support & Sales

Join our [Discord](https://discord.gg/NVpUWvreZa) for docs, support, talk or just to keep in touch.

<sub>**CORE** _by PushPushGo_ is not the same as our main **PushPushGo** product - are you looking for [PushPushGo - Push Notifications Management Platform?](https://pushpushgo.com)</sub>

## Client side SDK - supported platforms / providers

| Platform | Provider | SDK        |
|----------|----------|------------|
| Android / Huawei  | FCM / HMS      | [CORE Android SDK](https://github.com/ppgco/ppg-core-android-sdk) |
| iOS | APNS      | [CORE iOS SDK](https://github.com/ppgco/ppg-core-ios-sdk) |
| Flutter | FCM / HMS / APNS      | [CORE Flutter SDK](https://github.com/ppgco/ppg-core-flutter-sdk) |
| Web | Vapid (WebPush)     | [CORE JS SDK](https://github.com/ppgco/ppg-core-js-sdk) |

## Server side SDK (sending notifications)
| Platform | SDK      |
|----------|----------|
| JavaScript / TypeScript  | [CORE JS SDK](https://github.com/ppgco/ppg-core-js-sdk) | 
| .NET  | [WIP - ask](https://discord.gg/NVpUWvreZa) | 
| Java  | [WIP - ask](https://discord.gg/NVpUWvreZa) | 

# SDK Integration instructions

## Requirements:
Project integrated with FCM or HMS.
In case of HMS your package must be signed for debug/release and SHA fingerprint of certificate must be added in HMS developer app settings.
Otherwise u will encounter 6003 error during receiving push notifications.

## 1. Add dependencies to your project

```gradle
// build.gradle (:app)
dependencies {  
// PPG Core jitpack
  implementation "com.github.ppgco:ppg-core-android-sdk:0.0.33"
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
    <!-- Choose HMS or FCM - remove other one -->
    <string name="default_fcm_project_id">Get_value_from_google-services.json</string> 
    <string name="default_hms_app_id">Get_value_from_hms_developer_account</string>
    <drawable name="default_notification_icon">@drawable/ic_launcher_foreground</drawable>
    <!-- Optional fields -->
    <!-- Default channel -->
    <string name="default_channel_id">ppg_core_default</string>
    <string name="default_channel_name">PPG Core Default Channel</string>
    <bool name="default_channel_badge_enabled">true</bool>
    <bool name="default_channel_vibration_enabled">true</bool>
    <string-array name="default_channel_vibration_pattern">0, 1000, 500, 1500, 1000</string-array>
    <bool name="default_channel_lights_enabled">true</bool>
    <color name="default_lights_color">#ff00ff</color>
    <string name="default_channel_sound">magic_tone</string>

    <!-- Second channel -->
    <string name="second_channel_id">second_channel</string>
    <string name="second_channel_name">second</string>
    <bool name="second_channel_badge_enabled">false</bool>
    <bool name="second_channel_vibration_enabled">true</bool>
    <string-array name="second_channel_vibration_pattern">0, 4000, 1000, 4000, 1000</string-array>
    <bool name="second_channel_lights_enabled">true</bool>
    <string name="second_channel_lights_color">#FF0000</string>
    <string name="second_channel_sound">magic_tone_2</string>
</resources>
```
> **IMPORTANT** Be careful when naming variables in resources as they are directly related to **channelName** retrieved from notification payload. If your payload will contain channel name that does not exist in resources file, default channel willl be used. When creating resources variables for your channels, follow the example blueprint below: 
```xml
<!--
   Replace <placeholder> with name of your channel.
   You can use this name to send notifications via that channel (channelName
   in payload)
-->
<string name="<placeholder>_channel_id">your_channel_id</string>
<string name="<placeholder>_channel_name">Your channel name</string>
<bool name="<placeholder>_channel_badge_enabled">true</bool>
<bool name="<placeholder>_channel_vibration_enabled">true</bool>
<string-array name="<placeholder>_channel_vibration_pattern">0, 1000, 500, 500, 1000</string-array>
<bool name="<placeholder>_channel_lights_enabled">true</bool>
<string name="<placeholder>_channel_lights_color">#FF0000</string>
<string name="<placeholder>_channel_sound">your_sound_file_name</string>
```

Default channel badge is set to true by default. You can modify badge number in notification payload. (See send notification)

Vibration pattern - (before start delay, first vibration duration, between vibrations delay, second vibration duration, after vibrations delay). Values in milliseconds.

To set channel sound you have to place your sound file in `res/raw/` folder and set it's name in like in example above. (without extension) 
 
> **IMPORTANT:** Be careful when setting notification channel options. Once a channel is set, its settings cannot be overriden. Consider testing different settings on staging environment before production release. However you can create more channels with different names and new settings as in example above (default_channel, second_channel). If you won't provide any channel settings, default channel with default settings will be created. 

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

# Support & production run
All API keys available in this documentation allows you to test service with very low rate-limits.
If you need production credentials or just help with integration please visit us in [discord](https://discord.gg/NVpUWvreZa) or just mail to [support+core@pushpushgo.com](mailto:support+core@pushpushgo.com)
