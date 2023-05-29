package com.pushpushgo.core_sdk.sdk.notification

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.google.gson.Gson
import com.pushpushgo.core_sdk.sdk.HttpApiClient
import com.pushpushgo.core_sdk.sdk.IHttpApiClient
import com.pushpushgo.core_sdk.sdk.NotificationActionType
import com.pushpushgo.core_sdk.sdk.PpgConfig
import com.pushpushgo.core_sdk.sdk.events.NotificationDelivered
import com.pushpushgo.core_sdk.sdk.utils.HttpUtils
import kotlin.random.Random


class PpgNotificationService(
    private val context: Context
) {
    private val config = PpgConfig(context)
    private val notificationManager by lazy { NotificationManagerCompat.from(context) }
    private val httpClient: IHttpApiClient by lazy { HttpApiClient(config) }

    private fun getUniqueNotificationId() = Random.nextInt(0, Int.MAX_VALUE)

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): NotificationChannel {
        val soundUri: Uri = if (config.defaultChannelSound != 0) {
            Uri.parse("android.resource://" + context.packageName + "/"
                    + config.defaultChannelSound)
        } else {
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        val attributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
        val remoteVibrationPattern = config.defaultChannelVibrationPattern

        return NotificationChannel(
            config.defaultChannelId,
            config.defaultChannelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setShowBadge(config.defaultChannelBadgeEnabled)
            enableVibration(config.defaultChannelVibrationEnabled)
            vibrationPattern = remoteVibrationPattern
            setSound(soundUri, attributes)
            enableLights(config.defaultChannelLightsEnabled)
            lightColor = config.defaultChannelLightsColor
        }
    }

    private fun createClickIntent(
        context: Context,
        remoteMessage: RemotePpgMessage,
        actionName: String?,
    ): PendingIntent {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            ?: throw java.lang.RuntimeException("Unable to instantiate")

        intent
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .apply {

                Log.d("", "createClickIntent RemotePpgMessage.PPG_ACTION_IDENTIFIER ${RemotePpgMessage.PPG_INTENT_IDENTIFIER}");
                data =
                    (Uri.parse("content://" + RemotePpgMessage.INTENT_ACTION_NAME_KEY + "/" + actionName))
                putExtra(RemotePpgMessage.INTENT_DATA_KEY, Gson().toJson(remoteMessage))
                putExtra(RemotePpgMessage.INTENT_ACTION_NAME_KEY, actionName)
                putExtra(RemotePpgMessage.PPG_INTENT_IDENTIFIER, NotificationActionType.PUSH_CLICK.toString())
            }


        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createOnCloseIntent(
        context: Context,
        remoteMessage: RemotePpgMessage,
    ): PendingIntent {


        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            ?: throw java.lang.RuntimeException("Unable to instantiate")

        intent
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .apply {
                putExtra(RemotePpgMessage.INTENT_DATA_KEY, Gson().toJson(remoteMessage))
                putExtra(RemotePpgMessage.PPG_INTENT_IDENTIFIER, NotificationActionType.PUSH_CLOSE.toString())
            }

        return PendingIntent.getActivity(
            context,
            getUniqueNotificationId(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun fetchImageToIconCompat(url: String?): IconCompat {
        val emptyBitmap: IconCompat = IconCompat.createWithBitmap(
            Bitmap.createBitmap(
                EMPTY_BITMAP_WIDTH,
                EMPTY_BITMAP_WIDTH,
                Bitmap.Config.ARGB_8888
            )
        )

        if (url == null) {
            return emptyBitmap
        }

        val bitmap = HttpUtils.getBitmapFromURL(url) ?: return emptyBitmap

        return IconCompat.createWithBitmap(bitmap)
    }


    private fun buildNotificationInternal(
        remoteMessage: RemotePpgMessage,
        context: Context
    ): Notification {

        val notificationBuilder =
            NotificationCompat.Builder(context, config.defaultChannelId)

        // Setup default behaviour and bind handler on close
        notificationBuilder
            .setAutoCancel(true)
            .setDeleteIntent(this.createOnCloseIntent(context, remoteMessage))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder
                .setSmallIcon(config.defaultNotificationIcon).color = config.defaultNotificationIconColor
        } else {
            fetchImageToIconCompat(remoteMessage.smallIcon).let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    notificationBuilder
                        .setSmallIcon(it)
                }
            }
        }


        // Always after click on push handle default action
        notificationBuilder.setContentIntent(
            createClickIntent(
                context,
                remoteMessage,
                RemotePpgMessage.DEFAULT_ACTION_NAME
            )
        )

        // Add each action data
        for (action in remoteMessage.actions) {
            // If icon exists then get bitmap and set icon as action icon
            notificationBuilder
                .addAction(
                    NotificationCompat.Action(
                        fetchImageToIconCompat(action.icon),
                        action.title,
                        createClickIntent(
                            context,
                            remoteMessage,
                            action.action
                        )
                    )
                )
        }

        notificationBuilder
            .setContentTitle(remoteMessage.title)
            .setContentText(remoteMessage.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setWhen(System.currentTimeMillis())

        remoteMessage.icon?.let {
            val bitmap = HttpUtils.getBitmapFromURL(it)
            notificationBuilder
                .setLargeIcon(bitmap)
        }

        if ((remoteMessage.body?.length ?: 0) > 80) {
            notificationBuilder
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(remoteMessage.body)
                )
        }

        remoteMessage.image?.let {
            val bitmap = HttpUtils.getBitmapFromURL(it)
            notificationBuilder
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                )
        }

        remoteMessage.badge?.let {
            notificationBuilder
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setNumber(it)
        }

        // Support for API < 26
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            var defaults = 0
            if (config.defaultChannelVibrationPattern.isNotEmpty()) {
                val vibrationPattern = config.defaultChannelVibrationPattern
                notificationBuilder.setVibrate(vibrationPattern)
            } else {
                defaults = Notification.DEFAULT_VIBRATE
            }

            if (config.defaultChannelSound != 0) {
                val soundUri = Uri.parse("android.resource://" + context.packageName + "/"
                        + config.defaultChannelSound)
                notificationBuilder.setSound(
                    soundUri
                )
            } else {
                defaults = defaults or Notification.DEFAULT_SOUND
            }

            if (config.defaultChannelLightsEnabled) {
                notificationBuilder.setLights(config.defaultChannelLightsColor, 500, 500)
            } else {
                defaults = defaults or Notification.DEFAULT_LIGHTS
            }
            notificationBuilder.setDefaults(defaults)
        }


        return notificationBuilder.build()
    }

    fun notify(notification: PpgNotification) {
        when (notification) {
            is PpgNotification.Data -> {

                if (
                    ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Consider calling
                    // ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.createNotificationChannel(
                        createNotificationChannel()
                    )
                }

                notificationManager.notify(
                    notification.notification.getNotificationId(),
                    buildNotificationInternal(notification.notification, context)
                )

                httpClient.registerDelivered(
                    NotificationDelivered(
                        notification.notification.getMessageMetadata()
                    )
                )
            }

            is PpgNotification.Silent -> {
                httpClient.registerDelivered(
                    NotificationDelivered(
                        notification.metadata
                    )
                )
            }

            is PpgNotification.Unknown -> {
                TODO("Handle Unknown Message")
            }
        }
    }

    companion object {
        const val EMPTY_BITMAP_WIDTH = 1
    }
}