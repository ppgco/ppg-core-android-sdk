package com.pushpushgo.core_sdk.sdk.client

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.pushpushgo.core_sdk.sdk.PpgMessageIntentHandler
import com.pushpushgo.core_sdk.sdk.PpgConfig
import com.pushpushgo.core_sdk.sdk.fcm.FcmProvider
import com.pushpushgo.core_sdk.sdk.hms.HmsProvider
import com.pushpushgo.core_sdk.sdk.utils.PermissionState
import com.pushpushgo.core_sdk.sdk.utils.PermissionsUtils
import com.pushpushgo.core_sdk.sdk.utils.PlatformEnum
import com.pushpushgo.core_sdk.sdk.utils.PlatformUtils

class PpgCoreClient(
    private val activity: AppCompatActivity,
    private val config: PpgConfig
) {
    private val receiver by lazy { PpgMessageIntentHandler(config) }

    private var provider: AbstractProvider = when (PlatformUtils.getPlatform()) {
        PlatformEnum.Android -> {
            if (!PlatformUtils.isFcmIncluded()) {
                throw java.lang.RuntimeException("No FCM configuration provided please add firebase to your dependencies")
            }
            FcmProvider(config)
        }
        PlatformEnum.Huawei -> {
            if (!PlatformUtils.isHmsIncluded()) {
                throw java.lang.RuntimeException("No HMS configuration provided please add hms push to your dependencies")
            }
            HmsProvider(config, activity)
        }
    }

    fun check(): PermissionState {
        return PermissionsUtils.check(activity)
    }

    fun getSubscription(callback: (subscription: Subscription) -> Unit) {
        return this.provider.getSubscription(callback)
    }

    fun deleteSubscription(callback: (wasDeleted: Boolean) -> Unit) {
        return this.provider.deleteSubscription(callback)
    }

    fun onReceive(context: Context, intent: Intent?) {
        receiver.handleIntent(context, intent)
    }
}
