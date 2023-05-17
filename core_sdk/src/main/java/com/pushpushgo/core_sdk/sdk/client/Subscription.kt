package com.pushpushgo.core_sdk.sdk.client

import com.google.gson.JsonObject

data class Subscription(
    val token: String,
    val project: String,
    val type: SubscriptionTypes,
) {
    fun toJSON(): String {
        return JsonObject().apply {
            addProperty("token", token)
            addProperty("project", project)
            addProperty("type", type.type)
        }.toString()
    }
}