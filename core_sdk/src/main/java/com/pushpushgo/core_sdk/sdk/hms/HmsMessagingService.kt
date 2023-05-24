package com.pushpushgo.core_sdk.sdk.hms

import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import com.pushpushgo.core_sdk.sdk.notification.PpgNotificationService

abstract class HmsMessagingService : HmsMessageService() {
    private val hmsNotificationTranslator by lazy { HmsNotificationTranslator() }
    private val ppgNotificationService by lazy { PpgNotificationService(this) }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        ppgNotificationService.notify(
            hmsNotificationTranslator.translate(remoteMessage),
        )
    }
}