package com.pushpushgo.core_sdk.sdk.client

abstract class AbstractProvider {
    abstract fun getSubscription(callback: (subscription: Subscription) -> Unit)
    abstract fun deleteSubscription(callback: (wasDeleted: Boolean) -> Unit)
}