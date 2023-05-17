package com.pushpushgo.core_sdk.sdk

import com.pushpushgo.core_sdk.sdk.events.NotificationActionClicked
import com.pushpushgo.core_sdk.sdk.events.NotificationClosed
import com.pushpushgo.core_sdk.sdk.events.NotificationDelivered

interface IHttpApiClient {
    fun registerClicked(event: NotificationActionClicked): Unit

    fun registerClosed(event: NotificationClosed): Unit

    fun registerDelivered(event: NotificationDelivered): Unit
}