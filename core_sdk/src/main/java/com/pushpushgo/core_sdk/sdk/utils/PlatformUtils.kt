package com.pushpushgo.core_sdk.sdk.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import java.util.Locale

class PlatformUtils {

    companion object {
        private fun isHuaweiDevice(): Boolean {
            val manufacturer = Build.MANUFACTURER
            val brand = Build.BRAND
            return manufacturer.lowercase(Locale.getDefault())
                .contains("huawei") || brand.lowercase(Locale.getDefault()).contains("huawei")
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun Intent.setOpenSettingsForApiLarger25(context: Context) {
            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
        }

        private fun Intent.setOpenSettingsForApiBetween21And25(context: Context) {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            putExtra("app_package", context.packageName)
            putExtra("app_uid", context.applicationInfo.uid)
        }

        private fun Intent.setOpenSettingsForApiLess21(context: Context) {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            addCategory(Intent.CATEGORY_DEFAULT)
            data = Uri.parse("package:${context.packageName}")
        }

        fun getPlatform(): PlatformEnum {
            if (isHuaweiDevice()) {
                return PlatformEnum.Huawei
            }

            return PlatformEnum.Android
        }

        fun isHmsIncluded() = try {
            checkNotNull(com.huawei.hms.aaid.HmsInstanceId::class.java.name)
            checkNotNull(com.huawei.agconnect.AGConnectOptionsBuilder::class.java.name)
            true
        } catch (e: NoClassDefFoundError) {
            false
        }

        fun isFcmIncluded() = try {
            checkNotNull(com.google.firebase.messaging.FirebaseMessaging::class.java)
            true
        } catch (e: Throwable) {
            false
        }

        fun openSettings(context: Context) {
            val intent = Intent()
            when {
                Build.VERSION.SDK_INT > Build.VERSION_CODES.O -> intent.setOpenSettingsForApiLarger25(
                    context
                )
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> intent.setOpenSettingsForApiBetween21And25(
                    context
                )
                else -> intent.setOpenSettingsForApiLess21(context)
            }

            context.startActivity(intent)
        }
    }

}