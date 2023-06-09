package com.pushpushgo.core_sdk.sdk.fcm

import com.google.firebase.messaging.FirebaseMessaging
import com.pushpushgo.core_sdk.sdk.PpgConfig
import com.pushpushgo.core_sdk.sdk.client.AbstractProvider
import com.pushpushgo.core_sdk.sdk.client.Subscription
import com.pushpushgo.core_sdk.sdk.client.SubscriptionTypes

class FcmProvider(private val config: PpgConfig) : AbstractProvider() {

    private val firebase: FirebaseMessaging by lazy {
        FirebaseMessaging.getInstance()
    }

    override fun getSubscription(callback: (token: Subscription) -> Unit) {
        firebase.token.addOnSuccessListener {
            callback(
                Subscription(
                    token = it,
                    project = config.fcmProjectId,
                    type = SubscriptionTypes.FCM
                )
            )
        }
    }

    override fun deleteSubscription(callback: (result: Boolean) -> Unit) {
        firebase
            .deleteToken()
            .addOnCompleteListener {
                callback(it.isSuccessful)
            }
    }

}
