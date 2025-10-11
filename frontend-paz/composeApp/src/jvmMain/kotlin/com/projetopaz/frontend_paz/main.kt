package com.projetopaz.frontend_paz

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "frontend_paz",
    ) {
        App()
    }
}