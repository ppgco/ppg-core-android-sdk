package com.pushpushgo.example.presentation

import android.content.Intent
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

        when (ppgClient.check()) {
            PermissionState.ALLOWED -> {
                Log.d(mTag, "Notifications allowed")
                ppgClient.getSubscription {
                    Log.d(mTag, it.toJSON())
                }
            }
            PermissionState.DENIED -> {
                Log.d(mTag, "Notifications denied")
            }
            PermissionState.RATIONALE -> {
                Log.d(mTag, "Notifications rationale")
                ppgClient.getSubscription {
                    Log.d(mTag, it.toJSON())
                }
            }
            PermissionState.ASK -> {
                Log.d(mTag, "Notifications ask")
                PermissionsUtils.requestPermissions(this) {
                    Log.d(mTag, "Notification ask result: $it")
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d(mTag, "onResume in main activity intent: $intent")
        ppgClient.onReceive(this, intent)
    }

}

