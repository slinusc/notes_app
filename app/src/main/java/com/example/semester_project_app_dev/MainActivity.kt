package com.example.semester_project_app_dev

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.semester_project_app_dev.data.AppDatabase
import com.example.semester_project_app_dev.data.User
import com.example.semester_project_app_dev.ui.*
import com.example.semester_project_app_dev.ui.theme.SemesterProjectAppDevTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SemesterProjectAppDevTheme {
                val context = LocalContext.current
                val sharedPrefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                val coroutineScope = rememberCoroutineScope()

                var currentScreen by remember { mutableStateOf("start") }
                var loggedInUser by remember { mutableStateOf<User?>(null) }

                // Auto-login if credentials are saved
                LaunchedEffect(Unit) {
                    val savedName = sharedPrefs.getString("name", null)
                    val savedSurname = sharedPrefs.getString("surname", null)
                    val savedPassword = sharedPrefs.getString("password", null)

                    if (savedName != null && savedSurname != null && savedPassword != null) {
                        val db = AppDatabase.getInstance(context)
                        val user = db.userDao().authenticate(savedName, savedSurname, savedPassword)
                        if (user != null) {
                            loggedInUser = user
                            currentScreen = "overview"
                        }
                    }
                }

                when (currentScreen) {
                    "start" -> StartScreen(
                        onSignUpClick = { currentScreen = "signup" },
                        onLoginClick = { currentScreen = "login" }
                    )

                    "login" -> LoginScreen(
                        onLogin = { name, surname, password ->
                            val db = AppDatabase.getInstance(context)
                            coroutineScope.launch {
                                val user = db.userDao().authenticate(name, surname, password)
                                if (user != null) {
                                    // Save credentials
                                    sharedPrefs.edit()
                                        .putString("name", name)
                                        .putString("surname", surname)
                                        .putString("password", password)
                                        .apply()

                                    loggedInUser = user
                                    currentScreen = "overview"
                                }
                            }
                        },
                        onBack = { currentScreen = "start" }
                    )

                    "signup" -> SignUpScreen(
                        onSignUp = { name, surname, school, semester, password ->
                            // Save credentials after signup
                            sharedPrefs.edit()
                                .putString("name", name)
                                .putString("surname", surname)
                                .putString("password", password)
                                .apply()

                            currentScreen = "start"
                        },
                        onBack = { currentScreen = "start" }
                    )

                    "overview" -> loggedInUser?.let { user ->
                        NotesOverviewScreen(
                            name = user.name,
                            surname = user.surname,
                            school = user.school,
                            semester = user.semester,
                            onBack = {
                                // Logout: clear saved credentials
                                sharedPrefs.edit().clear().apply()
                                loggedInUser = null
                                currentScreen = "start"
                            }
                        )
                    }
                }
            }
        }
    }
}
