package com.pushpushgo.core_sdk.sdk.fcm

import com.google.firebase.messaging.RemoteMessage
import com.pushpushgo.core_sdk.sdk.INotificationTranslator
import com.pushpushgo.core_sdk.sdk.notification.PpgRawNotification
import com.pushpushgo.core_sdk.sdk.notification.RemotePpgMessage
import com.pushpushgo.core_sdk.sdk.notification.SilentPpgMessage

open class FcmNotificationTranslator() : INotificationTranslator<RemoteMessage> {

    override fun getNotification(remoteMessage: RemoteMessage): PpgRawNotification {
        return when {
            remoteMessage.data.containsKey(SilentPpgMessage.SILENT_DATA_KEY) -> PpgRawNotification.Silent(
                remoteMessage.data[SilentPpgMessage.SILENT_DATA_KEY]
                    ?: throw java.lang.RuntimeException("no data from message")
            )
            remoteMessage.data.containsKey(RemotePpgMessage.PPG_DATA_KEY) -> PpgRawNotification.Data(
                remoteMessage.data[RemotePpgMessage.PPG_DATA_KEY]
                    ?: throw java.lang.RuntimeException("no data from message")
            )
            else -> PpgRawNotification.Unknown()
        }
    }
}