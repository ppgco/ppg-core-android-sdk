package com.pushpushgo.core_sdk.sdk.message

import java.util.*

data class MessageMetadata(
    val messageId: MessageId,
    val foreignId: ForeignId,
    val contextId: ContextId
){
    companion object{
    fun create(messageRaw: MessageMetadataRaw) = MessageMetadata(
        messageId = MessageId(UUID.fromString(messageRaw.messageId)),
        contextId = ContextId(UUID.fromString(messageRaw.contextId)),
        foreignId = ForeignId(messageRaw.foreignId)
        )
    }
}

data class MessageMetadataRaw(
    val messageId: String,
    val contextId: String,
    val foreignId: String?
)
