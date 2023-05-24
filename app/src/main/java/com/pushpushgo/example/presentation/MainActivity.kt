package com.pushpushgo.example.presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.pushpushgo.core_sdk.sdk.client.PpgCoreClient
import com.pushpushgo.core_sdk.sdk.utils.PermissionState
import com.pushpushgo.core_sdk.sdk.utils.PermissionsUtils
import com.pushpushgo.example.presentation.navigation.SetupNavGraph
import com.pushpushgo.example.presentation.ui.PushpushgoExampleTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mTag: String = javaClass.name

    private val ppgClient: PpgCoreClient by lazy {
        PpgCoreClient(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        ppgClient.onReceive(this, intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(mTag, "onCreate in main activity intent: $intent")

        ppgClient.onReceive(this, intent)

        setContent {
            PushpushgoExampleTheme {
                SetupNavGraph(navController = rememberNavController())
            }
        }

       ppgClient.register(this) {
           it?.let { subscription -> Log.d(mTag, subscription.toJSON()) }
       }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionsUtils.PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("Allow", "Allow")
                } else {
                    Log.d("Deny", "Deny")
                }
                return
            }
        }
    }
    override fun onResume() {
        super.onResume()
        Log.d(mTag, "onResume in main activity intent: $intent")
        ppgClient.onReceive(this, intent)
    }

}

