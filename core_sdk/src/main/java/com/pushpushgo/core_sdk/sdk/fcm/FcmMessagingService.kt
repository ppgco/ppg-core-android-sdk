package com.pushpushgo.core_sdk.sdk.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pushpushgo.core_sdk.sdk.notification.PpgNotificationService

abstract class FcmMessagingService : FirebaseMessagingService() {
    private val fcmNotificationTranslator by lazy { FcmNotificationTranslator() }
    private val ppgNotificationService by lazy { PpgNotificationService(applicationContext) }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        ppgNotificationService.notify(
            fcmNotificationTranslator.translate(remoteMessage),
        )
    }
}