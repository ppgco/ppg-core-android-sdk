package com.pushpushgo.core_sdk.sdk.extensions

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import java.util.*

fun JsonObject.element(key: String) =
    this[key] ?: throw JsonParseException("Unknown json field: $key")

fun JsonObject.getString(key: String): String {
    return this.element(key).asString
}

fun JsonObject.getStringOrNull(key: String): String? {
    val keyValue = this.get(key)

    return if (keyValue.isJsonNull) {
        null
    } else {
        keyValue.asString
    }
}

fun JsonObject.getUuid(key: String): UUID = UUID.fromString(this.getString(key))