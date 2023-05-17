package com.pushpushgo.core_sdk.sdk.hms

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.huawei.hms.push.RemoteMessage
import com.pushpushgo.core_sdk.sdk.INotificationTranslator
import com.pushpushgo.core_sdk.sdk.extensions.getStringOrNull
import com.pushpushgo.core_sdk.sdk.notification.PpgRawNotification
import com.pushpushgo.core_sdk.sdk.notification.RemotePpgMessage

open class HmsNotificationTranslator : INotificationTranslator<RemoteMessage> {
    override fun getNotification(remoteMessage: RemoteMessage): PpgRawNotification {
        val jsonObject: JsonObject = JsonParser
            .parseString(remoteMessage.data)
            .asJsonObject

        val silentData = jsonObject.getStringOrNull(RemotePpgMessage.SILENT_DATA_KEY)
        val ppgData = jsonObject.getStringOrNull(RemotePpgMessage.PPG_DATA_KEY)

        return when {
            (silentData != null) -> PpgRawNotification.Silent(
                silentData
            )
            (ppgData != null) -> PpgRawNotification.Data(
                ppgData
            )
            else -> PpgRawNotification.Unknown()
        }
    }
}