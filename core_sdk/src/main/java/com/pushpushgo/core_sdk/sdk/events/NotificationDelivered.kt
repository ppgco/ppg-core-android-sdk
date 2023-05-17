package com.pushpushgo.core_sdk.sdk.events

import com.pushpushgo.core_sdk.sdk.message.MessageMetadata

/**
 * Notification was delivered to device
 */
class NotificationDelivered(messageMetadata: MessageMetadata) : Event(messageMetadata)