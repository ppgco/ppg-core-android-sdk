package com.pushpushgo.core_sdk.sdk.events

interface IEventSerializerDeserializer {
    fun <E : Event> serialize(event: E): String
    fun deserialize(json: String): Event
}