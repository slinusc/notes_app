package com.example.semester_project_app_dev.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme  // ← Use Material 3
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary      = PrimaryColor,
    onPrimary    = OnPrimary,
    background   = Background,
    onBackground = OnBackground
)

private val LightColors = lightColorScheme(
    primary      = PrimaryColor,
    onPrimary    = OnPrimary,
    background   = Background,
    onBackground = OnBackground
)

@Composable
fun SemesterProjectAppDevTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography  = AppTypography,
        shapes      = AppShapes,
        content     = content
    )
}