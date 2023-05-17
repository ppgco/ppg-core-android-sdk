package com.pushpushgo.core_sdk.sdk.hms

import android.content.Context
import com.pushpushgo.core_sdk.sdk.PpgConfig
import com.pushpushgo.core_sdk.sdk.client.AbstractProvider
import com.pushpushgo.core_sdk.sdk.client.Subscription
import com.pushpushgo.core_sdk.sdk.client.SubscriptionTypes
import com.huawei.hms.aaid.HmsInstanceId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HmsProvider(config: PpgConfig, private val context: Context) : AbstractProvider() {
    private val hmsAppId =
        config.hmsAppId ?: throw java.lang.RuntimeException("config.hmsAppId is required")

    override fun getSubscription(callback: (token: Subscription) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val token = HmsInstanceId.getInstance(context)
                    .getToken(hmsAppId, "HCM")
                callback(
                    Subscription(
                        token = token,
                        project = hmsAppId,
                        type = SubscriptionTypes.HMS
                    )
                )
            } catch (e: Exception) {
                // handle exception
            }
        }
    }

    override fun deleteSubscription(callback: (result: Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                HmsInstanceId.getInstance(context)
                    .deleteToken(hmsAppId, "HCM")
                callback(true)
            } catch (e: Exception) {
                callback(false)
            }
        }
    }
}