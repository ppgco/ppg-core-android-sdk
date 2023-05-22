package com.pushpushgo.core_sdk.sdk.events

import com.google.gson.*
import com.pushpushgo.core_sdk.sdk.extensions.getStringOrNull
import com.pushpushgo.core_sdk.sdk.extensions.getUuid
import com.pushpushgo.core_sdk.sdk.message.ContextId
import com.pushpushgo.core_sdk.sdk.message.ForeignId
import com.pushpushgo.core_sdk.sdk.message.MessageId
import com.pushpushgo.core_sdk.sdk.message.MessageMetadata
import com.pushpushgo.core_sdk.sdk.utils.TimeHelper
import java.lang.reflect.Type


class EventSerializerDeserializer : IEventSerializerDeserializer {
    private val gsonSerializer = GsonBuilder()
        .serializeNulls()
        .registerTypeHierarchyAdapter(Event::class.java, InternalEventSerializer())
        .create()

    override fun <E : Event> serialize(event: E): String = gsonSerializer.toJson(event)

    override fun deserialize(json: String): Event {
        val jsonObject: JsonObject = JsonParser
            .parseString(json)
            .asJsonObject

        val className = jsonObject.get(EVENT_KEY).asString
        val cls = Class.forName(className) as Class<*>
        val isEventClass = Event::class.java.isAssignableFrom(cls)

        if (!isEventClass) {
            throw java.lang.Exception("cls is not an instance of Event class")
        }
        val clsConstructor = cls.getConstructor(MessageMetadata::class.java)

        clsConstructor.isAccessible = true
        val event: Event = clsConstructor.newInstance(
            MessageMetadata(
                messageId = MessageId(jsonObject.getUuid(MESSAGE_ID_KEY)),
                foreignId = ForeignId(jsonObject.getStringOrNull(FOREIGN_ID_KEY)),
                contextId = ContextId(jsonObject.getUuid(CONTEXT_ID_KEY)),
            )
        ) as Event

        val ts = TimeHelper.fromIso8601String(jsonObject.get(TIME_STAMP_KEY).asString)
        event.createdAt.time = ts.time

        return event
    }

    private class InternalEventSerializer : JsonSerializer<Event> {
        override fun serialize(
            src: Event?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            return JsonObject().apply {
                src?.let {
                    addProperty(EVENT_KEY, src.javaClass.name)
                    addProperty(TIME_STAMP_KEY, TimeHelper.toIso8601String(src.createdAt))
                    addProperty(MESSAGE_ID_KEY, src.message.messageId.toString())
                    addProperty(CONTEXT_ID_KEY, src.message.contextId.toString())
                    addProperty(FOREIGN_ID_KEY, src.message.foreignId.toStringOrNull())
                }
            }
        }
    }

    companion object {
        internal const val EVENT_KEY = "\$event"
        internal const val TIME_STAMP_KEY = "ts"
        internal const val MESSAGE_ID_KEY = "messageId"
        internal const val CONTEXT_ID_KEY = "contextId"
        internal const val FOREIGN_ID_KEY = "foreignId"
    }
}