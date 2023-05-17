package com.pushpushgo.core_sdk.sdk.hms

import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import com.pushpushgo.core_sdk.sdk.PpgConfig
import com.pushpushgo.core_sdk.sdk.client.Subscription
import com.pushpushgo.core_sdk.sdk.client.SubscriptionTypes
import com.pushpushgo.core_sdk.sdk.notification.PpgNotificationService

abstract class HmsMessagingService(
    private val config: PpgConfig
) : HmsMessageService() {

    private val projectId: String =
        config.hmsAppId ?: throw java.lang.RuntimeException("config.hmsAppId is required")


    private val hmsNotificationTranslator by lazy { HmsNotificationTranslator() }
    private val ppgNotificationService by lazy { PpgNotificationService(config, this) }

    final override fun onNewToken(token: String) {
        this.onNewSubscription(
            Subscription(
                token = token,
                project = projectId,
                type = SubscriptionTypes.HMS
            )
        )
    }

    abstract fun onNewSubscription(subscription: Subscription)

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        ppgNotificationService.notify(
            hmsNotificationTranslator.translate(remoteMessage),
        )
    }
}