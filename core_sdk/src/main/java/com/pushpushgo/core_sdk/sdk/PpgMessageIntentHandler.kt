package com.pushpushgo.core_sdk.sdk

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.pushpushgo.core_sdk.sdk.events.NotificationActionClicked
import com.pushpushgo.core_sdk.sdk.events.NotificationClosed
import com.pushpushgo.core_sdk.sdk.notification.RemotePpgMessage

open class PpgMessageIntentHandler(
    private val config: PpgConfig
) {
    private val mTag: String = "CoreBroadcastReceiver"

    private val httpClient: IHttpApiClient by lazy {
        HttpApiClient(config)
    }

    private fun startBrowserActivity(context: Context, url: Uri) {
        val openBrowserIntent = Intent(Intent.ACTION_VIEW, url)
        openBrowserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(openBrowserIntent)
    }

    private fun startAppActivityOnStack(context: Context, url: Uri) {
        val openAppIntent = Intent(Intent.ACTION_VIEW, url)
        val appPendingIntent: PendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(openAppIntent)
            getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
        appPendingIntent.send()
    }

    private fun internalHandleClickAction(
        context: Context,
        remotePpgMessage: RemotePpgMessage,
        actionName: String
    ) {
        Log.d(mTag, "REMOTE PPG MESSAGE: $remotePpgMessage")

        httpClient.registerClicked(
            NotificationActionClicked(
                remotePpgMessage.getMessageMetadata(),
                actionName
            )
        )

        val actionUrl = remotePpgMessage.getClickUrlForAction(actionName)
        val mainUrl = remotePpgMessage.getDefaultActionUrl()
        val url = if (actionName == RemotePpgMessage.DEFAULT_ACTION_NAME) mainUrl else actionUrl


        when {
            url != null && (url.toString().startsWith(protocolHttps) || url.toString().startsWith(
                protocolHttp
            )) -> {
                startBrowserActivity(context, url)
            }
            url != null && url.toString().startsWith(protocolApp) -> {
                startAppActivityOnStack(context, url)
            }
            else -> {
                Log.d(
                    mTag,
                    "Omit starting activity for open browser/app from url. Start main activity instead"
                )
            }
        }
    }

    private fun handleCloseAction(_context: Context, remotePpgMessage: RemotePpgMessage) {
        httpClient.registerClosed(
            NotificationClosed(
                remotePpgMessage.getMessageMetadata()
            )
        )
    }

    fun handleIntent(
        context: Context?,
        intent: Intent?
    ) {
        if (context == null) {
            Log.d(mTag, "Context is not pass to onReceive method omit next steps")
            return
        }

        if (intent == null) {
            Log.d(mTag, "Intent is not pass to onReceive method omit next steps")
            return
        }

        val ppgIntentIdentifier =
            intent.getStringExtra(RemotePpgMessage.PPG_INTENT_IDENTIFIER) ?: run {
                return
            }

        intent.removeExtra(RemotePpgMessage.PPG_INTENT_IDENTIFIER)

        val serializedData: String = intent.getStringExtra(RemotePpgMessage.INTENT_DATA_KEY)
            ?: run {
                Log.w(mTag, "Intent data not passed")
                return
            }

        Log.d(mTag, "serializedData: $serializedData")

        val ppgRemoteMessage: RemotePpgMessage = Gson().fromJson(
            serializedData,
            RemotePpgMessage::class.java
        )

        when (NotificationActionType.valueOf(ppgIntentIdentifier)) {
            NotificationActionType.PUSH_CLOSE -> {
                onNotificationClose(context, ppgRemoteMessage)
            }
            NotificationActionType.PUSH_CLICK -> {
                intent.getStringExtra(RemotePpgMessage.INTENT_ACTION_NAME_KEY)?.let { actionName ->
                    onNotificationClick(context, ppgRemoteMessage, actionName)
                }
            }
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancel(ppgRemoteMessage.getNotificationId())
    }

    protected open fun onNotificationClose(
        context: Context,
        ppgRemoteMessage: RemotePpgMessage
    ) {
        handleCloseAction(context, ppgRemoteMessage)
    }

    protected open fun onNotificationClick(
        context: Context,
        ppgRemoteMessage: RemotePpgMessage,
        actionName: String
    ) {
        internalHandleClickAction(context, ppgRemoteMessage, actionName)
    }

    companion object {
        const val protocolApp = "app://"
        const val protocolHttps = "https://"
        const val protocolHttp = "http://"
    }
}