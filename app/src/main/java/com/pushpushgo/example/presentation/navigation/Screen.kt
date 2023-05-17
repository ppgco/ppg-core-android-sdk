package com.pushpushgo.example.presentation.navigation

sealed class Screen (val route: String) {
    object Main : Screen(route = "main")
    object DeepLink : Screen(route = "deeplink/{$MY_ARG}") {
        fun passArgument(message: String) = "deeplink/$message"
    }
}
