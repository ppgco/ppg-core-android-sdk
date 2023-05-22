package com.pushpushgo.core_sdk.sdk.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pushpushgo.core_sdk.sdk.PpgConfig
import com.pushpushgo.core_sdk.sdk.client.Subscription
import com.pushpushgo.core_sdk.sdk.client.SubscriptionTypes
import com.pushpushgo.core_sdk.sdk.notification.PpgNotificationService

abstract class FcmMessagingService : FirebaseMessagingService() {
    private val fcmNotificationTranslator by lazy { FcmNotificationTranslator() }
    private val ppgNotificationService by lazy { PpgNotificationService(applicationContext) }
    private val config by lazy { PpgConfig(applicationContext) }

    final override fun onNewToken(token: String) {
        this.onNewSubscription(
            Subscription(
                token = token,
                project = config.fcmProjectId,
                type = SubscriptionTypes.FCM
            )
        )
    }

    abstract fun onNewSubscription(subscription: Subscription)

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        ppgNotificationService.notify(
            fcmNotificationTranslator.translate(remoteMessage),
        )
    }
}