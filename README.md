# PushPushGo Core SDK for Android

[![JitPack](https://img.shields.io/jitpack/v/github/ppgco/ppg-core-android-sdk?style=flat-square)](https://jitpack.io/#ppgco/ppg-core-android-sdk)
![GitHub tag (latest)](https://img.shields.io/github/v/tag/ppgco/ppg-core-android-sdk?style=flat-square)
![Discord](https://img.shields.io/discord/1108358192339095662?color=%237289DA&label=Discord&style=flat-square)

### 1. Add dependencies to your project

```
// build.gradle (:app)
dependencies {  
// jitpack
  implementation "repo.z.jitpacka" //TODO jitpack
// FCM
  implementation 'com.google.firebase:firebase-messaging-ktx:23.1.2'  
  implementation platform('com.google.firebase:firebase-bom:31.2.3')  
// HMS
  implementation 'com.huawei.agconnect:agconnect-core:1.7.2.300'  
  implementation 'com.huawei.hms:push:6.7.0.300'   
}

// build.gradle (root)
allprojects {
    repositories {
        // jitpack
        maven { url 'https://jitpack.io' }
    }
}
```

### 2. Add service files
In case of example we store our files in **services** folder
#### 2.1 MyPpgConfig (/services/MyPpgConfig) //TODO jitpack
File should contain class which inherits from sdk's **PpgConfig** class. Example MyPpgConfig file:
```
import android.graphics.Color  
import com.pushpushgo.core_sdk.sdk.PpgConfig  
import com.pushpushgo.example.R  
  
class MyPpgConfig: PpgConfig( 
  //Default notification Icon settings 
  defaultNotificationIcon = R.drawable.ic_launcher_foreground,  
  defaultNotificationIconColor = Color.RED,
  // PPG endpoint - leave unchanged  
  ppgCoreEndpoint = "https://ppg-core.master1.qappg.co",
  // FCM project ID / HMS app ID  
  fcmProjectId = "test-64257",  
  hmsAppId = "1234567890",
  // Default notification channel settings  
  defaultChannelId = "MyId1",  
  defaultChannelName = "PushPushGoExample",
  // All below are optional
  defaultChannelBadgeEnabled = true,  
  defaultChannelVibrationEnabled = true,  
  defaultChannelVibrationPattern = longArrayOf(0, 1000, 500, 1500, 1000),  
  defaultChannelSound = R.raw.magic_tone, 
  defaultChannelLightsEnabled = true, 
  defaultChannelLightsColor = Color.MAGENTA  
)
```
Vibration pattern - (before start delay, first vibration duration, between vibartions delay, second vibration duration, after vibrations delay). Values in miliseconds.
Support for adding more than one default channel will be added soon. 
> **IMPORTANT:** Be careful when setting notification channel options. Once a channel is set, its settings cannot be overriden. Consider testing different settings on staging environment before production release.

#### 2.1 MyFirebaseMessagingService (/services/MyFirebaseMessagingService) //TODO jitpack
File should contain class which inherits from sdk's **FcmMessagingService** class.
You should provide MyPpgConfig() class which you created in previous step as constructor for **FcmMessagingService**
**onNewSubscription** function should be implemented in way that allows you to save "subscription" in your database.
Example file:
```
import android.util.Log  
import com.pushpushgo.core_sdk.sdk.client.Subscription  
import com.pushpushgo.core_sdk.sdk.fcm.FcmMessagingService  
  
class MyFirebaseMessagingService : FcmMessagingService(MyPpgConfig()) {  
    override fun onNewSubscription(subscription: Subscription) {  
        Log.d("onNewSubscription", "Save this data in your database ${subscription.toJSON()}");  
  }  
}
```

### 3. AndroidManifest.xml

#### 3.1 At the top of your manifest file add:
```
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>  
<!-- If you want to support vibration -->
<uses-permission android:name="android.permission.VIBRATE"/>
```
#### 3.2 Inside <*application*> tag add MyFirebaseMessagingService from previous step:
```
<service  
  android:name=".services.MyFirebaseMessagingService"  
  android:exported="false">  
  <intent-filter> 
    <action android:name="com.google.firebase.MESSAGING_EVENT" />  
  </intent-filter>
</service>
```
If you want to support using default notification icon for older API's (21+) you can specify <*meta-data*> tag with icon resource, e.g:
```
<meta-data android:name="com.google.firebase.messaging.default_notification_icon"  
  android:resource="@drawable/ic_launcher_foreground" />
```
#### 3.3 Inside main <*activity*> tag:
Specify launchMode:
```
<activity
  android:launchMode="singleTop"
  ...
```
also add intent filter for handling notification events:
```
<intent-filter>  
 <action android:name="PUSH_CLICK" />  
 <action android:name="PUSH_CLOSE" />  
</intent-filter>
```
to handle web/app/deepLinks redirect add another intent filter:
```
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

### 4. Usage  //TODO jitpack
In your MainActivity file initialize ppgClient:

```
private val ppgClient: PpgCoreClient by lazy {  
//this -> activity context
//MyPpgConfig() -> /services/MyPpgConfig
  PpgCoreClient(this, MyPpgConfig())  
}
```
Add **ppgClient.onReceive(this, intent)** to functions onNewIntent, onCreate, onResume:
```
override fun onNewIntent(intent: Intent?) {  
    super.onNewIntent(intent)  
    Log.d(mTag, "onnewIntent in main activity intent: $intent")
    ppgClient.onReceive(this, intent)  
}

override fun onCreate(savedInstanceState: Bundle?) {  
    super.onCreate(savedInstanceState)  
  
    Log.d(mTag, "onCreate in main activity intent: $intent")  
  
    ppgClient.onReceive(this, intent)
}

override fun onResume() {  
    super.onResume()  
    Log.d(mTag, "onResume in main activity intent: $intent")  
    ppgClient.onReceive(this, intent)  
}

```

Inside onCreate function you should implement registration for notifications. You can find example implementation in our example app (com.pushpushgo.example)