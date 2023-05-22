package com.pushpushgo.core_sdk.sdk

import android.content.Context
import android.util.Log

open class PpgConfig(
    private val context: Context
    ) {
    val defaultNotificationIcon: Int
        get() = getResourceId("drawable", "default_notification_icon")

    val defaultNotificationIconColor: Int
        get() = getResourceId("color", "notification_icon_color")

    val ppgCoreEndpoint: String
        get() = getStringResource("string", "default_ppg_core_endpoint", "https://api-core.pushpushgo.com/v1")

    val fcmProjectId: String
        get() = getStringResource("string", "default_fcm_project_id")

    val hmsAppId: String
        get() = getStringResource("string", "default_hms_app_id")

    val defaultChannelId: String
        get() = getStringResource("string", "default_channel_id", "ppg_default_channel")

    val defaultChannelName: String
        get() = getStringResource("string", "default_channel_name", "PPG default channel")

    val defaultChannelBadgeEnabled: Boolean
        get() = getBooleanResource("bool", "default_channel_badge_enabled", false)

    val defaultChannelVibrationEnabled: Boolean
        get() = getBooleanResource("bool", "default_channel_vibration_enabled", false)

    val defaultChannelSound: Int
        get() {
            val soundId = getStringResource("string", "default_channel_sound", "")
            if (soundId == "") {
                return 0
            }
            return getResourceId("raw", soundId)
        }

    val defaultChannelVibrationPattern: LongArray
        get() = getLongArrayResource("array", "default_vibration_pattern", longArrayOf())

    val defaultChannelLightsEnabled: Boolean
        get() = getBooleanResource("bool", "default_channel_lights_enabled", false)

    val defaultChannelLightsColor: Int
        get() = getResourceId("color", "default_lights_color")

    private fun getResourceId(type: String, name: String): Int {
        return try {
            context.resources.getIdentifier(name, type, context.packageName)
        } catch (error: Exception) {
            Log.e("Cannot find resource ID", "$name, with type $type in ${context.packageName}")
            0
        }
    }

    private fun getStringResource(type: String, name: String, defaultValue: String = ""): String {
        return try {
            val resourceId = getResourceId(type, name)
            return if (resourceId != 0) context.getString(resourceId) else defaultValue
        } catch (error: Exception) {
            Log.e("Cannot find resource ID", "$name, with type $type in ${context.packageName}")
            defaultValue
        }

    }

    private fun getBooleanResource(type: String, name: String, defaultValue: Boolean): Boolean {
        return try {
            val resourceId = getResourceId(type, name)
            return if (resourceId != 0) context.resources.getBoolean(resourceId) else defaultValue
        } catch (error: Exception) {
            Log.e("Cannot find resource ID", "$name, with type $type in ${context.packageName}")
            defaultValue
        }

    }

    private fun getLongArrayResource(type: String, name: String, defaultValue: LongArray): LongArray {
        return try {
            val resourceId = getResourceId(type, name)
            val stringArray =
                if (resourceId != 0) context.resources.getStringArray(resourceId) else null
            return stringArray?.mapNotNull { it.toLongOrNull() }?.toLongArray()
                ?: return defaultValue
        } catch (error: Exception) {
            Log.e("Cannot find resource ID", "$name, with type $type in ${context.packageName}")
            defaultValue
        }

    }
}