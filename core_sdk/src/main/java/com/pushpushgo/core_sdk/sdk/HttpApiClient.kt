package com.pushpushgo.core_sdk.sdk

import android.util.Log
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.pushpushgo.core_sdk.sdk.events.Event
import com.pushpushgo.core_sdk.sdk.events.NotificationActionClicked
import com.pushpushgo.core_sdk.sdk.events.NotificationClosed
import com.pushpushgo.core_sdk.sdk.events.NotificationDelivered
import com.pushpushgo.core_sdk.sdk.message.MessageMetadata
import com.pushpushgo.core_sdk.sdk.utils.TimeHelper
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class HttpApiClient(
    private val config: PpgConfig
) : IHttpApiClient {
    private val mTag = javaClass.name

    private val mClient = OkHttpClient()

    private val mClickedTemplate: String = "/context/:contextId/events/clicked"
    private val mClosedTemplate: String = "/context/:contextId/events/closed"
    private val mDeliveredTemplate: String = "/context/:contextId/events/delivered"

    override fun registerClicked(event: NotificationActionClicked) {
        exec(mClickedTemplate, event.message, serializeNotificationActionClick(event))
    }

    override fun registerClosed(event: NotificationClosed) {
        exec(mClosedTemplate, event.message, serializeEvent(event))
    }

    override fun registerDelivered(event: NotificationDelivered) {
        exec(mDeliveredTemplate, event.message, serializeEvent(event))
    }

    private fun serializeNotificationActionClick(
        event: NotificationActionClicked
    ): JsonObject {

        return serializeEvent(event).apply {
            addProperty(KEY_ACTION, event.actionDescription.toString())
        }
    }

    private fun serializeEvent(event: Event): JsonObject {
        return JsonObject().apply {
            addProperty(KEY_TIME_STAMP, TimeHelper.toIso8601String(event.createdAt))
            addProperty(KEY_MESSAGE_ID, event.message.messageId.toString())
            addProperty(KEY_CONTEXT_ID, event.message.contextId.toString())
            addProperty(KEY_FOREIGN_ID, event.message.foreignId.toStringOrNull())
        }
    }

    private fun exec(
        urlTemplate: String,
        metadata: MessageMetadata,
        body: JsonElement,
    ) {
        val requestBody = RequestBody.create(
            CONTENT_TYPE.toMediaTypeOrNull(),
            body.toString()
        )

        val path = urlTemplate.replace(
            ":contextId",
            metadata.contextId.toString()
        );

        val request = Request.Builder()
            .url(config.ppgCoreEndpoint + path)
            .post(requestBody)
            .build()

        mClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {}

            override fun onFailure(call: Call, e: IOException) {
                Log.w(mTag, e)
            }
        })
    }

    companion object {
        private const val CONTENT_TYPE = "application/json"

        private const val KEY_TIME_STAMP = "ts"
        private const val KEY_CONTEXT_ID = "contextId"
        private const val KEY_MESSAGE_ID = "messageId"
        private const val KEY_ACTION = "action"
        private const val KEY_FOREIGN_ID = "foreignId"
    }
}