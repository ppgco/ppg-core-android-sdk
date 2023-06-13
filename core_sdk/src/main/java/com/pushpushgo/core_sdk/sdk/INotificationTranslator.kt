package com.pushpushgo.core_sdk.sdk

import com.google.gson.Gson
import com.pushpushgo.core_sdk.sdk.message.MessageMetadata
import com.pushpushgo.core_sdk.sdk.message.MessageMetadataRaw
import com.pushpushgo.core_sdk.sdk.notification.PpgNotification
import com.pushpushgo.core_sdk.sdk.notification.PpgRawNotification
import com.pushpushgo.core_sdk.sdk.notification.RemotePpgMessage
import com.pushpushgo.core_sdk.sdk.notification.SilentPpgMessage

interface INotificationTranslator<T> {
    fun getNotification(remoteMessage: T): PpgRawNotification

    fun translate(
        remoteMessage: T,
    ): PpgNotification {
        return when (val msg = this.getNotification(remoteMessage)) {
            is PpgRawNotification.Data -> {
                PpgNotification.Data(
                    Gson().fromJson(msg.data, RemotePpgMessage::class.java)
                )
            }
            is PpgRawNotification.Silent -> {
                PpgNotification.Silent(
                    Gson().fromJson(msg.data, SilentPpgMessage::class.java)
                )
            }
            is PpgRawNotification.Unknown -> PpgNotification.Unknown()
        }
    }
}