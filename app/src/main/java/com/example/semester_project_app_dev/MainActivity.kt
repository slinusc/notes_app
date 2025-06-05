package com.example.semester_project_app_dev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.semester_project_app_dev.ui.LoginScreen
import com.example.semester_project_app_dev.ui.StartScreen
import com.example.semester_project_app_dev.ui.SignUpScreen
import com.example.semester_project_app_dev.ui.theme.SemesterProjectAppDevTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SemesterProjectAppDevTheme {
                var currentScreen by remember { mutableStateOf("start") }

                when (currentScreen) {
                    "start" -> StartScreen(
                        onSignUpClick = { currentScreen = "signup" },
                        onLoginClick  = { currentScreen = "login" }
                    )

                    "login" -> LoginScreen(
                        onLogin = { name, surname, password ->
                            // Handle login here
                            currentScreen = "start" // optional navigation back
                        }
                    )

                    "signup" -> SignUpScreen(
                        onSignUp = { name, surname, school, semester, password ->
                            // Handle sign-up here
                            currentScreen = "start" // or navigate elsewhere
                        }
                    )
                }
            }
        }
    }
}

