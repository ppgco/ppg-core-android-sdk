package com.pushpushgo.core_sdk

import com.pushpushgo.core_sdk.sdk.events.EventSerializerDeserializer
import com.pushpushgo.core_sdk.sdk.events.NotificationBodyClicked
import com.pushpushgo.core_sdk.sdk.message.ContextId
import com.pushpushgo.core_sdk.sdk.message.MessageId
import com.pushpushgo.core_sdk.sdk.message.MessageMetadata
import com.pushpushgo.core_sdk.sdk.message.ForeignId
import org.junit.Test
import java.util.UUID

class EventSerializerTest {

    private fun makeMessageMock(): MessageMetadata {
        return MessageMetadata(
            MessageId(UUID.randomUUID()),
            ForeignId(),
            ContextId(UUID.randomUUID())
        )
    }

    @Test
    fun `events are serializable`() {
        val message = makeMessageMock()


        val event = NotificationBodyClicked(message)
        val serde = EventSerializerDeserializer()

        val serializedEvent: String = serde.serialize(event)

        assert(serializedEvent is String)

        val deserializedEvent = serde.deserialize(serializedEvent)

        println("event = ${serde.serialize(event)}")
        println("deserializedEvent = ${serde.serialize(deserializedEvent)}")

        assert(event == deserializedEvent)
    }


}
