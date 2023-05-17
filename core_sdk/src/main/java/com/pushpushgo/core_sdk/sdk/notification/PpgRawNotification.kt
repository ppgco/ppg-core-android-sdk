package com.pushpushgo.core_sdk.sdk.notification

sealed class PpgRawNotification {
    class Silent(val data: String) : PpgRawNotification()

    class Data(val data: String) : PpgRawNotification()

    class Unknown : PpgRawNotification()
}