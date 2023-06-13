package com.pushpushgo.core_sdk.sdk.notification

import com.pushpushgo.core_sdk.sdk.message.MessageMetadata

sealed class PpgNotification {
    class Silent(val notification: SilentPpgMessage) : PpgNotification()

    class Data(val notification: RemotePpgMessage) : PpgNotification()

    class Unknown : PpgNotification()
}