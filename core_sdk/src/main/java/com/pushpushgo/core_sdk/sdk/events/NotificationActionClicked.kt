package com.pushpushgo.core_sdk.sdk.events

import com.pushpushgo.core_sdk.sdk.message.MessageMetadata

open class NotificationActionClicked(
    message: MessageMetadata,
    val actionDescription: String,
) : Event(message)