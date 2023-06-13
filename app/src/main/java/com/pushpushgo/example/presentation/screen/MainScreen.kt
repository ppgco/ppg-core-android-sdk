package com.pushpushgo.example.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pushpushgo.core_sdk.sdk.notification.Action
import com.pushpushgo.example.presentation.MainViewModel
import com.pushpushgo.core_sdk.sdk.notification.PpgNotification
import com.pushpushgo.core_sdk.sdk.notification.RemotePpgMessage
import com.pushpushgo.core_sdk.sdk.notification.SilentPpgMessage
import com.pushpushgo.example.presentation.navigation.MY_ARG
import com.pushpushgo.example.presentation.navigation.MY_URI

import com.pushpushgo.example.presentation.navigation.Screen
import java.util.*

val pushSilent: PpgNotification = PpgNotification.Silent(
    SilentPpgMessage(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        externalData = ""
    )
)

val pushDataTitleBody: PpgNotification = PpgNotification.Data(
    RemotePpgMessage(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        "Title of message",
        "Body of message",
        externalData = ""
    )
)

val pushDataWithIcon: PpgNotification = PpgNotification.Data(
    RemotePpgMessage(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        "Title of message",
        "Body of message",
        "https://ppg-image.pushpushgo.com/i/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1IjoiaHR0cHM6Ly9mYXN0bHkucGljc3VtLnBob3Rvcy9pZC8xMy8zMDAvMjAwLmpwZz9obWFjPTJMdjBfc0ZaT1ZfZ3dXWHdtOHduV2FqZkZnVDVEQkdFQ3RMMVRWSldiQjQiLCJ3Ijo2NCwiaCI6NjQsImUiOiJXRUJQIn0.77hXHDAeIq-xBwiZ8G2zi-HEu986tCprnJ0brFFrq-w",
        externalData = ""
    )
)

val pushDataWithIconImageOneAction: PpgNotification = PpgNotification.Data(
    RemotePpgMessage(
        messageId = UUID.randomUUID().toString(),
        contextId = UUID.randomUUID().toString(),
        foreignId = UUID.randomUUID().toString(),
        title = "Title of message",
        body = "Body of message, Body of message, Body of message, Body of message, Body of message, Body of message, Body of message, Body of message, Body of message, Body of message, Body of message",
        icon = "https://ppg-image.pushpushgo.com/i/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1IjoiaHR0cHM6Ly9mYXN0bHkucGljc3VtLnBob3Rvcy9pZC8xMy8zMDAvMjAwLmpwZz9obWFjPTJMdjBfc0ZaT1ZfZ3dXWHdtOHduV2FqZkZnVDVEQkdFQ3RMMVRWSldiQjQiLCJ3Ijo2NCwiaCI6NjQsImUiOiJXRUJQIn0.77hXHDAeIq-xBwiZ8G2zi-HEu986tCprnJ0brFFrq-w",
        image = "https://ppg-image.pushpushgo.com/i/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1IjoiaHR0cHM6Ly9pbWctOWdhZy1mdW4uOWNhY2hlLmNvbS9waG90by9hS0VFV0ExXzQ2MHN3cC53ZWJwIiwidyI6MTAwMCwiaCI6MTAwMCwiZSI6IldFQlAifQ.b6P_zB3LNy4R8EsxvxENnHXVmgCelcs2Kx_Ulz503ME",
        smallIcon = "https://ppg-image.pushpushgo.com/i/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1IjoiaHR0cHM6Ly9mYXN0bHkucGljc3VtLnBob3Rvcy9pZC8xMy8zMDAvMjAwLmpwZz9obWFjPTJMdjBfc0ZaT1ZfZ3dXWHdtOHduV2FqZkZnVDVEQkdFQ3RMMVRWSldiQjQiLCJ3Ijo2NCwiaCI6NjQsImUiOiJXRUJQIn0.77hXHDAeIq-xBwiZ8G2zi-HEu986tCprnJ0brFFrq-w",
        actions = listOf(
            Action(
                action = "action_1",
                actionUrl = "https://example.com",
                title = "example.com",
                icon = "https://ppg-image.pushpushgo.com/i/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1IjoiaHR0cHM6Ly9mYXN0bHkucGljc3VtLnBob3Rvcy9pZC8xMy8zMDAvMjAwLmpwZz9obWFjPTJMdjBfc0ZaT1ZfZ3dXWHdtOHduV2FqZkZnVDVEQkdFQ3RMMVRWSldiQjQiLCJ3Ijo2NCwiaCI6NjQsImUiOiJXRUJQIn0.77hXHDAeIq-xBwiZ8G2zi-HEu986tCprnJ0brFFrq-w",
            )
        ),
        externalData = ""
    )
)

val pushDataWithIconAndTwoActions: PpgNotification = PpgNotification.Data(
    RemotePpgMessage(
        messageId = UUID.randomUUID().toString(),
        contextId = UUID.randomUUID().toString(),
        foreignId = UUID.randomUUID().toString(),
        title = "Title of message",
        body = "Body of message",
        icon = "https://ppg-image.pushpushgo.com/i/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1IjoiaHR0cHM6Ly9mYXN0bHkucGljc3VtLnBob3Rvcy9pZC8xMy8zMDAvMjAwLmpwZz9obWFjPTJMdjBfc0ZaT1ZfZ3dXWHdtOHduV2FqZkZnVDVEQkdFQ3RMMVRWSldiQjQiLCJ3Ijo2NCwiaCI6NjQsImUiOiJXRUJQIn0.77hXHDAeIq-xBwiZ8G2zi-HEu986tCprnJ0brFFrq-w",
        actions = listOf(
            Action(
                action = "action_1",
                actionUrl = "https://example.com",
                title = "example.com",
                icon = "https://ppg-image.pushpushgo.com/i/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1IjoiaHR0cHM6Ly9mYXN0bHkucGljc3VtLnBob3Rvcy9pZC8xMy8zMDAvMjAwLmpwZz9obWFjPTJMdjBfc0ZaT1ZfZ3dXWHdtOHduV2FqZkZnVDVEQkdFQ3RMMVRWSldiQjQiLCJ3Ijo2NCwiaCI6NjQsImUiOiJXRUJQIn0.77hXHDAeIq-xBwiZ8G2zi-HEu986tCprnJ0brFFrq-w",
            ),
            Action(
                action = "action_2",
                actionUrl = "https://wp.pl",
                title = "wp.pl",
                icon = "https://ppg-image.pushpushgo.com/i/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1IjoiaHR0cHM6Ly9mYXN0bHkucGljc3VtLnBob3Rvcy9pZC8xMy8zMDAvMjAwLmpwZz9obWFjPTJMdjBfc0ZaT1ZfZ3dXWHdtOHduV2FqZkZnVDVEQkdFQ3RMMVRWSldiQjQiLCJ3Ijo2NCwiaCI6NjQsImUiOiJXRUJQIn0.77hXHDAeIq-xBwiZ8G2zi-HEu986tCprnJ0brFFrq-w",
            )
        ),
        externalData = ""
    )
)

val pushDataWithIconImage: PpgNotification = PpgNotification.Data(
    RemotePpgMessage(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        "Title of message",
        "Body of message",
        "https://ppg-image.pushpushgo.com/i/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1IjoiaHR0cHM6Ly9mYXN0bHkucGljc3VtLnBob3Rvcy9pZC8xMy8zMDAvMjAwLmpwZz9obWFjPTJMdjBfc0ZaT1ZfZ3dXWHdtOHduV2FqZkZnVDVEQkdFQ3RMMVRWSldiQjQiLCJ3Ijo2NCwiaCI6NjQsImUiOiJXRUJQIn0.77hXHDAeIq-xBwiZ8G2zi-HEu986tCprnJ0brFFrq-w",
        "https://ppg-image.pushpushgo.com/i/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1IjoiaHR0cHM6Ly9pbWctOWdhZy1mdW4uOWNhY2hlLmNvbS9waG90by9hS0VFV0ExXzQ2MHN3cC53ZWJwIiwidyI6MTAwMCwiaCI6MTAwMCwiZSI6IldFQlAifQ.b6P_zB3LNy4R8EsxvxENnHXVmgCelcs2Kx_Ulz503ME",
        externalData = ""
    )
)

val pushDataWithDeepLink: PpgNotification = PpgNotification.Data(
    RemotePpgMessage(
        messageId = UUID.randomUUID().toString(),
        contextId = UUID.randomUUID().toString(),
        foreignId = UUID.randomUUID().toString(),
        title = "Title of message",
        body = "Body of message",
        icon = "https://ppg-image.pushpushgo.com/i/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1IjoiaHR0cHM6Ly9mYXN0bHkucGljc3VtLnBob3Rvcy9pZC8xMy8zMDAvMjAwLmpwZz9obWFjPTJMdjBfc0ZaT1ZfZ3dXWHdtOHduV2FqZkZnVDVEQkdFQ3RMMVRWSldiQjQiLCJ3Ijo2NCwiaCI6NjQsImUiOiJXRUJQIn0.77hXHDAeIq-xBwiZ8G2zi-HEu986tCprnJ0brFFrq-w",
        image = "https://ppg-image.pushpushgo.com/i/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1IjoiaHR0cHM6Ly9pbWctOWdhZy1mdW4uOWNhY2hlLmNvbS9waG90by9hS0VFV0ExXzQ2MHN3cC53ZWJwIiwidyI6MTAwMCwiaCI6MTAwMCwiZSI6IldFQlAifQ.b6P_zB3LNy4R8EsxvxENnHXVmgCelcs2Kx_Ulz503ME",
        smallIcon = "https://ppg-image.pushpushgo.com/i/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1IjoiaHR0cHM6Ly9mYXN0bHkucGljc3VtLnBob3Rvcy9pZC8xMy8zMDAvMjAwLmpwZz9obWFjPTJMdjBfc0ZaT1ZfZ3dXWHdtOHduV2FqZkZnVDVEQkdFQ3RMMVRWSldiQjQiLCJ3Ijo2NCwiaCI6NjQsImUiOiJXRUJQIn0.77hXHDAeIq-xBwiZ8G2zi-HEu986tCprnJ0brFFrq-w",
        defaultAction = "$MY_URI/$MY_ARG=Coming from Notification",
        externalData = ""
    )
)

val pushDataWithDeepLinkInAction: PpgNotification = PpgNotification.Data(
    RemotePpgMessage(
        messageId = UUID.randomUUID().toString(),
        contextId = UUID.randomUUID().toString(),
        foreignId = UUID.randomUUID().toString(),
        title = "Title of message",
        body = "Body of message",
        icon = "https://ppg-image.pushpushgo.com/i/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1IjoiaHR0cHM6Ly9mYXN0bHkucGljc3VtLnBob3Rvcy9pZC8xMy8zMDAvMjAwLmpwZz9obWFjPTJMdjBfc0ZaT1ZfZ3dXWHdtOHduV2FqZkZnVDVEQkdFQ3RMMVRWSldiQjQiLCJ3Ijo2NCwiaCI6NjQsImUiOiJXRUJQIn0.77hXHDAeIq-xBwiZ8G2zi-HEu986tCprnJ0brFFrq-w",
        actions = listOf(
            Action(
                action = "action_1",
                actionUrl = "$MY_URI/$MY_ARG=Coming from Notification Action",
                title = "DeepLink in action",
                icon = "https://ppg-image.pushpushgo.com/i/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1IjoiaHR0cHM6Ly9mYXN0bHkucGljc3VtLnBob3Rvcy9pZC8xMy8zMDAvMjAwLmpwZz9obWFjPTJMdjBfc0ZaT1ZfZ3dXWHdtOHduV2FqZkZnVDVEQkdFQ3RMMVRWSldiQjQiLCJ3Ijo2NCwiaCI6NjQsImUiOiJXRUJQIn0.77hXHDAeIq-xBwiZ8G2zi-HEu986tCprnJ0brFFrq-w",
            )
        ),
        externalData = ""
    )
)

@Composable
fun MainScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { (mainViewModel::ppgNotify)(pushSilent) }) {
            Text(text = "push silent")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { (mainViewModel::ppgNotify)(pushDataTitleBody) }) {
            Text(text = "push data title and body")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { (mainViewModel::ppgNotify)(pushDataWithIcon) }) {
            Text(text = "push data title, body, icon")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { (mainViewModel::ppgNotify)(pushDataWithIconImageOneAction) }) {
            Text(text = "push data title, body, icon, image, 1 action")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { (mainViewModel::ppgNotify)(pushDataWithIconAndTwoActions) }) {
            Text(text = "push data title, body, icon, 2 actions")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { (mainViewModel::ppgNotify)(pushDataWithIconImage) }) {
            Text(text = "push data title, body, icon, image")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { (mainViewModel::ppgNotify)(pushDataWithDeepLink) }) {
            Text(text = "push data with deepLink")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { (mainViewModel::ppgNotify)(pushDataWithDeepLinkInAction) }) {
            Text(text = "push data with deepLink in action")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            navController.navigate(
                Screen.DeepLink.passArgument(
                    message = "Coming from Main Screen."
                )
            )
        }) {
            Text(text = "Deep Link SCREEN")
        }
    }
}