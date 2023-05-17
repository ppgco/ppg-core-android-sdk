package com.pushpushgo.core_sdk.sdk.utils

import java.util.*

open class Uuid(
    private val mId: UUID
) {

    override fun toString(): String {
        return mId.toString()
    }

    override fun equals(other: Any?): Boolean {
        return other is Uuid && mId == other.mId
    }

    companion object {
        inline fun <reified O : Uuid> create(): O {
            return O::class.java
                .getConstructor(UUID::class.java)
                .newInstance(UUID.randomUUID())
        }
    }
}