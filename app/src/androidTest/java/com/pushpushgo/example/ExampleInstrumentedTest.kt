package com.pushpushgo.example

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.util.Random

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.pushpushgo.example", appContext.packageName)
    }

    @Test
    fun createsNotification() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val notification = NotificationCompat
            .Builder(context, "master")
            .setSmallIcon(R.drawable.ic_add_24)
            .setContentTitle(null)
            .setContentText(null)
            .build()

        NotificationManagerCompat.from(context).notify(Random().nextInt(), notification)
    }

}