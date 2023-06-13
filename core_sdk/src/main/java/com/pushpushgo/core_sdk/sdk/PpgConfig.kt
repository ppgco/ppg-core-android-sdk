package com.pushpushgo.core_sdk.sdk

import android.content.Context
import android.util.Log

open class PpgConfig(
    private val context: Context
    ) {
    data class ChannelConfig(
        val channelId: String,
        val channelName: String,
        val badgeEnabled: Boolean,
        val vibrationEnabled: Boolean,
        val vibrationPattern: LongArray,
        val lightsEnabled: Boolean,
        val lightsColor: Int,
        val channelSound: Int
    )
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


    fun getChannelConfig(channelResourcesName: String): ChannelConfig {
        val channelId = getStringResource("string", channelResourcesName + "_channel_id", "ppg_default_channel")
        val channelName = getStringResource("string", channelResourcesName + "_channel_name", "PPG default channel")
        val badgeEnabled = getBooleanResource("bool", channelResourcesName + "_channel_badge_enabled", false)
        val vibrationEnabled = getBooleanResource("bool", channelResourcesName + "_channel_vibration_enabled", false)
        val vibrationPattern = getLongArrayResource("array", channelResourcesName + "_channel_vibration_pattern", longArrayOf())
        val lightsEnabled = getBooleanResource("bool", channelResourcesName + "_channel_lights_enabled", false)
        val lightsColor = getResourceId("color", channelResourcesName + "_lights_color")
        var channelSound = 0
        val soundId = getStringResource("string", channelResourcesName + "_channel_sound", "")
        if (soundId != ""){
            channelSound = getResourceId("raw", soundId)
        }


        return ChannelConfig(channelId, channelName, badgeEnabled, vibrationEnabled, vibrationPattern, lightsEnabled, lightsColor, channelSound)
    }

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
            Log.d("Cannot find resource ID", "$name, with type $type in ${context.packageName}")
            defaultValue
        }

    }

    private fun getBooleanResource(type: String, name: String, defaultValue: Boolean): Boolean {
        return try {
            val resourceId = getResourceId(type, name)
            return if (resourceId != 0) context.resources.getBoolean(resourceId) else defaultValue
        } catch (error: Exception) {
            Log.d("Cannot find resource ID", "$name, with type $type in ${context.packageName}")
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
            Log.d("Cannot find resource ID", "$name, with type $type in ${context.packageName}")
            defaultValue
        }

    }
}