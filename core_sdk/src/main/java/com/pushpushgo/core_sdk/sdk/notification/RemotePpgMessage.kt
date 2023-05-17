package com.pushpushgo.core_sdk.sdk.notification

import android.net.Uri
import com.pushpushgo.core_sdk.sdk.message.ContextId
import com.pushpushgo.core_sdk.sdk.message.ForeignId
import com.pushpushgo.core_sdk.sdk.message.MessageId
import com.pushpushgo.core_sdk.sdk.message.MessageMetadata
import java.util.*

data class Action(
    val action: String,
    val title: String,
    val actionUrl: String,
    val icon: String? = null
)

data class RemotePpgMessage(
    // uuid message id
    val messageId: String,
    val contextId: String,
    val foreignId: String? = "",

    val title: String? = null,
    val body: String? = null,
    val icon: String? = null,
    val image: String? = null,
    val smallIcon: String? = null,
    val actions: List<Action> = listOf(),
    val defaultAction: String? = null,
    val sound: String? = null,
    val badge: Int? = null,
    val vibration: List<Long>? = listOf()

) {
    companion object {
        const val SILENT_DATA_KEY = "_silent"
        const val PPG_DATA_KEY = "_ppg"
        const val INTENT_DATA_KEY = "data"
        const val INTENT_ACTION_NAME_KEY = "actionName"
        const val DEFAULT_ACTION_NAME = "default"
        const val PPG_INTENT_IDENTIFIER = "_ppg_intent"
    }

    fun getNotificationId(): Int = this.messageId.hashCode()

    fun getClickUrlForAction(actionName: String): Uri? {
        for (action in actions) {
            if (action.action == actionName) {
                return Uri.parse(action.actionUrl)
            }
        }
        return null
    }

    fun getDefaultActionUrl(): Uri? {
        if (defaultAction != null) {
            return Uri.parse(defaultAction)
        }

        return null
    }

    fun getMessageMetadata() = MessageMetadata(
        messageId = MessageId(UUID.fromString(messageId)),
        contextId = ContextId(UUID.fromString(contextId)),
        foreignId = ForeignId(foreignId)
    )
}