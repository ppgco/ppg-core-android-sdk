package com.pushpushgo.example.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.pushpushgo.core_sdk.sdk.notification.PpgNotification
import com.pushpushgo.core_sdk.sdk.notification.PpgNotificationService
import com.pushpushgo.example.services.MyPpgConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    private val ppgNotificationService by lazy {
        PpgNotificationService(MyPpgConfig(), application)
    }

    fun ppgNotify(payload: PpgNotification) {
        ppgNotificationService.notify(payload)
    }
}