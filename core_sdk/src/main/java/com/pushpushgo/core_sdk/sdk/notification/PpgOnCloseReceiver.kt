package com.pushpushgo.core_sdk.sdk.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pushpushgo.core_sdk.sdk.PpgConfig
import com.pushpushgo.core_sdk.sdk.PpgMessageIntentHandler

open class PpgOnCloseReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val config = context?.let { PpgConfig(it) }
        if (config != null) {
            val intentHandler = PpgMessageIntentHandler(config)
            intentHandler.handleIntent(context, intent)
        }
    }
}