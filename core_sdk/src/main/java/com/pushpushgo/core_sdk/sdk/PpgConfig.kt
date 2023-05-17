package com.pushpushgo.core_sdk.sdk


open class PpgConfig(
    /**
     * Default icon of push notification
     * @sample defaultNotificationIcon = R.drawable.ic_my_super_icon
     */
    val defaultNotificationIcon: Int,
    val defaultNotificationIconColor: Int,

    /**
     * @sample ppgCoreEndpoint = "https://core.pushpushgo.com/"
     */
    val ppgCoreEndpoint: String,


    /**
     * Data from google_services.json field project_info -> project_id
     * @sample fcmProjectId = "project-test-abd32"
     */
    val fcmProjectId: String? = null,

    /**
     * Data from hms config appId
     * @sample hmsAppId = "1234567890"
     */
    val hmsAppId: String? = null,

    /**
     * Default channel identifier
     * @sample defaultChannelVibrationPattern = listOf(0, 500, 0, 1500)
     * @sample defaultChannelSound = R.raw.magic_tone
     * @sample defaultChannelLightsColor = Color.MAGENTA
     */
    val defaultChannelId: String = "default_id",
    val defaultChannelName: String = "default name",
    val defaultChannelBadgeEnabled: Boolean = true,
    val defaultChannelVibrationEnabled: Boolean = true,
    val defaultChannelVibrationPattern: LongArray = longArrayOf(),
    val defaultChannelSound: Int = 0,
    val defaultChannelLightsEnabled: Boolean = true,
    val defaultChannelLightsColor: Int = 0
    )