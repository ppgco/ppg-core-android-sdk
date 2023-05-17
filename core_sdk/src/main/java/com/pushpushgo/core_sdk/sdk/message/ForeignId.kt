package com.pushpushgo.core_sdk.sdk.message

data class ForeignId(
    val mId: String?
) {
    constructor() : this(null)

    fun toStringOrNull(): String? = mId
}