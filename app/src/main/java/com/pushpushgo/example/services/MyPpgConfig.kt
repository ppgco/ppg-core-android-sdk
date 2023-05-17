package com.pushpushgo.example.services

import android.graphics.Color
import com.pushpushgo.core_sdk.sdk.PpgConfig
import com.pushpushgo.example.R

class MyPpgConfig: PpgConfig(
    defaultNotificationIcon = R.drawable.ic_launcher_foreground,
    defaultNotificationIconColor = Color.RED,
    ppgCoreEndpoint = "https://api-core.pushpushgo.com/v1",
    fcmProjectId = "test-64257",
    hmsAppId = "1234567890",
    defaultChannelId = "MyId1",
    defaultChannelName = "PushPushGoExample",
    defaultChannelVibrationPattern = longArrayOf(0, 1000, 500, 1500, 1000),
    defaultChannelSound = R.raw.magic_tone,
    defaultChannelLightsColor = Color.MAGENTA
)