package com.pushpushgo.core_sdk.sdk.events

import com.pushpushgo.core_sdk.sdk.message.MessageMetadata
import java.util.*

abstract class Event(
    val message: MessageMetadata
) {
    val createdAt: Date = Date()


    override fun equals(other: Any?): Boolean {
        return other != null
                && other is Event
                && other.message == message
                && other.createdAt == createdAt
    }
}