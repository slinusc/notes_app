package com.example.semester_project_app_dev.state

import androidx.compose.runtime.*

class MenuState {
    var isOpen by mutableStateOf(false)
        private set

    fun open() { isOpen = true }
    fun close() { isOpen = false }
    fun toggle() { isOpen = !isOpen }
}

@Composable
fun rememberMenuState(): MenuState {
    return remember { MenuState() }
}
