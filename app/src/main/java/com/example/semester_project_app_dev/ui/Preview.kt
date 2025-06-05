package com.example.semester_project_app_dev.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.semester_project_app_dev.ui.theme.SemesterProjectAppDevTheme

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SemesterProjectAppDevTheme {
        StartScreen(onSignUpClick = {}, onLoginClick = {})
    }
}
