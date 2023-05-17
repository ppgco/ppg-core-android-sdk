package com.pushpushgo.core_sdk.sdk.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.nfc.Tag
import android.util.Log
import java.net.URL

internal class HttpUtils {
    companion object {
        const val TAG = "HttpUtils"
        fun getBitmapFromURL(urlString: String): Bitmap? {
            var bitmap: Bitmap? = null

            try {
                val url = URL(urlString)
                val inputStream = url.openConnection().getInputStream()
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                Log.w(TAG, e.toString())
            }

            return bitmap
        }
    }
}