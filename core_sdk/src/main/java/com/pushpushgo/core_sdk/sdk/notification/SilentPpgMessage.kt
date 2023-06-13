package com.pushpushgo.core_sdk.sdk.notification

import com.pushpushgo.core_sdk.sdk.message.ContextId
import com.pushpushgo.core_sdk.sdk.message.ForeignId
import com.pushpushgo.core_sdk.sdk.message.MessageId
import com.pushpushgo.core_sdk.sdk.message.MessageMetadata
import java.util.*

data class SilentPpgMessage(
    // uuid message id
    val messageId: String,
    val contextId: String,
    val foreignId: String? = "",

    val externalData: String?

) {
    companion object {
        const val SILENT_DATA_KEY = "_silent"
    }

    fun getMessageMetadata() = MessageMetadata(
        messageId = MessageId(UUID.fromString(messageId)),
        contextId = ContextId(UUID.fromString(contextId)),
        foreignId = ForeignId(foreignId)
    )
}