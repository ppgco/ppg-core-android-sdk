package com.pushpushgo.core_sdk.sdk.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pushpushgo.core_sdk.sdk.PpgConfig
import com.pushpushgo.core_sdk.sdk.client.Subscription
import com.pushpushgo.core_sdk.sdk.client.SubscriptionTypes
import com.pushpushgo.core_sdk.sdk.notification.PpgNotificationService

abstract class FcmMessagingService(
    private val config: PpgConfig
) : FirebaseMessagingService() {

    private val projectId: String =
        config.fcmProjectId ?: throw java.lang.RuntimeException("config.fcmProjectId is required")

    private val fcmNotificationTranslator by lazy { FcmNotificationTranslator() }
    private val ppgNotificationService by lazy { PpgNotificationService(config, this) }

    final override fun onNewToken(token: String) {
        this.onNewSubscription(
            Subscription(
                token = token,
                project = projectId,
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