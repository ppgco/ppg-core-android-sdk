package com.pushpushgo.core_sdk.sdk.events

import com.pushpushgo.core_sdk.sdk.message.MessageMetadata
import com.pushpushgo.core_sdk.sdk.notification.RemotePpgMessage

/**
 * An event which describes click into notification body.
 */
internal class NotificationBodyClicked(
    message: MessageMetadata,
) : NotificationActionClicked(message, RemotePpgMessage.DEFAULT_ACTION_NAME)