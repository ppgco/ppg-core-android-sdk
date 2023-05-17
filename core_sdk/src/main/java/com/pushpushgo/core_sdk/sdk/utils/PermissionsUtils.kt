package com.pushpushgo.core_sdk.sdk.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat

open class PermissionsUtils(
    private val activity: AppCompatActivity
) {
    private val TAG = this::class.java.name
    private var notificationManager = NotificationManagerCompat.from(activity)

    private fun areNotificationsEnabled(): Boolean = notificationManager.areNotificationsEnabled()

    companion object {
        fun check(activity: AppCompatActivity): PermissionState {
            val instance = PermissionsUtils(activity);
            return instance.checkPermissions();
        }

        fun requestPermissions(activity: AppCompatActivity, callback: ActivityResultCallback<Boolean>) {
            activity
                .registerForActivityResult(ActivityResultContracts.RequestPermission(), callback)
                .launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun shouldRequestViaUiAction(): Boolean = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            )
        else -> false
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun canPostNotification(): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissions(): PermissionState {
        when {
            areNotificationsEnabled() -> {
                Log.d(TAG, "areNotificationsEnabled -> notifications enabled")
                return PermissionState.ALLOWED
            }
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU -> {
                Log.d(TAG, "notTiramisu -> notifications allowed")
                return PermissionState.ALLOWED
            }
            canPostNotification() -> {
                Log.d(TAG, "canPostNotification -> notifications allowed")
                return PermissionState.ALLOWED
            }
            shouldRequestViaUiAction() -> {
                Log.d(TAG, "shouldRequestViaUiAction -> notifications rationale")
                return PermissionState.RATIONALE
            }
            else -> {
                return PermissionState.ASK
            }
        }
    }
}