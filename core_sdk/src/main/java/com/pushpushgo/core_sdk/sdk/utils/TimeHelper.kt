package com.pushpushgo.core_sdk.sdk.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TimeHelper {

    companion object {
        private val df: DateFormat by lazy {
            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            df.timeZone = TimeZone.getTimeZone("UTC")
            df
        }

        fun toIso8601String(date: Date): String = df.format(date)

        fun fromIso8601String(raw: String): Date = df.parse(raw) ?: throw java.lang.RuntimeException("invalid iso8601 format raw=$raw")
    }

}