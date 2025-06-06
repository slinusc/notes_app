// File: app/src/main/java/com/example/semester_project_app_dev/MainActivity.kt

package com.example.semester_project_app_dev

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.semester_project_app_dev.data.AppDatabase
import com.example.semester_project_app_dev.data.Course
import com.example.semester_project_app_dev.data.User
import com.example.semester_project_app_dev.ui.CourseDetailScreen
import com.example.semester_project_app_dev.ui.CourseOverviewScreen
import com.example.semester_project_app_dev.ui.LoginScreen
import com.example.semester_project_app_dev.ui.SignUpScreen
import com.example.semester_project_app_dev.ui.StartScreen
import com.example.semester_project_app_dev.ui.theme.SemesterProjectAppDevTheme
import com.example.semester_project_app_dev.ui.EditUserScreen
import com.example.semester_project_app_dev.ui.EditCourseScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Toast
import com.example.semester_project_app_dev.state.rememberMenuState


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SemesterProjectAppDevTheme {
                val context = LocalContext.current
                val db = AppDatabase.getInstance(context)
                val userDao = db.userDao()
                val courseDao = db.courseDao()
                val coroutineScope = rememberCoroutineScope()

                var currentScreen by remember { mutableStateOf("start") }
                var loggedInUser by remember { mutableStateOf<User?>(null) }
                var selectedCourse by remember { mutableStateOf<Course?>(null) }
                var isSubmitting by remember { mutableStateOf(false) }
                var selectedCourseIndex by remember { mutableIntStateOf(0) }
                var courseForEdit  by remember { mutableStateOf<Course?>(null) }   // for EditCourse


                // Auto-login if credentials exist
                LaunchedEffect(Unit) {
                    val sharedPrefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    val savedName = sharedPrefs.getString("name", null)
                    val savedSurname = sharedPrefs.getString("surname", null)
                    val savedPassword = sharedPrefs.getString("password", null)

                    if (!savedName.isNullOrBlank() && !savedSurname.isNullOrBlank() && !savedPassword.isNullOrBlank()) {
                        // IMPORTANT: authenticate and update UI state should happen on Main thread; here it's safe since we're in LaunchedEffect on Main
                        val user = userDao.authenticate(savedName, savedSurname, savedPassword)
                        if (user != null) {
                            loggedInUser = user
                            currentScreen = "overview"
                        }
                    }
                }

                // Collect courses only for the logged-in user (userId is Long)
                val coursesFlow = remember(loggedInUser) {
                    loggedInUser?.let { user ->
                        courseDao.getAllCoursesFlowForUser(user.id)
                    }
                }
                val coursesList by coursesFlow?.collectAsState(initial = emptyList())
                    ?: remember { mutableStateOf(emptyList<Course>()) }

                when (currentScreen) {
                    // Start Screen
                    "start" -> StartScreen(
                        onSignUpClick = { currentScreen = "signup" },
                        onLoginClick = { currentScreen = "login" }
                    )

                    // Login Screen
                    "login" -> LoginScreen(
                        onLogin = { name, surname, password ->
                            coroutineScope.launch(Dispatchers.IO) {
                                val user = userDao.authenticate(name, surname, password)
                                if (user != null) {
                                    // WARNING: updating Compose state from IO context can cause unpredictable behavior; switch to Main before updating
                                    withContext(Dispatchers.Main) {
                                        val sharedPrefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                                        sharedPrefs.edit()
                                            .putString("name", name)
                                            .putString("surname", surname)
                                            .putString("password", password)
                                            .apply()
                                        loggedInUser = user
                                        currentScreen = "overview"
                                    }
                                }
                            }
                        },
                        onBack = { currentScreen = "start" }
                    )

                    // SignUp Screen: register user and seed default courses
                    "signup" -> SignUpScreen(
                        onSignUp = { name, surname, school, semester, password ->
                            if (isSubmitting) return@SignUpScreen
                            isSubmitting = true

                            coroutineScope.launch {
                                withContext(Dispatchers.IO) {
                                    val existing = userDao.findUser(name, surname)
                                    if (existing == null) {
                                        val newUser = User(
                                            name = name,
                                            surname = surname,
                                            school = school,
                                            semester = semester,
                                            password = password
                                        )

                                        val userId = userDao.insertUser(newUser).toInt()

                                        val baseCourse = Course(
                                            name = "MATH",
                                            teacher = "Dr. Friedrich Gauss",
                                            day = "Tuesday",
                                            time = "09:00 - 12:00",
                                            isUrgent = true,
                                            userId = userId
                                        )
                                        val baseCourse2 = Course(
                                            name = "PHYSICS",
                                            teacher = "Dr. Albert Einstein",
                                            day = "Wednesday",
                                            time = "10:00 - 13:00",
                                            isUrgent = false,
                                            userId = userId
                                        )
                                        val baseCourse3 = Course(
                                            name = "CHEMISTRY",
                                            teacher = "Dr. Marie Curie",
                                            day = "Thursday",
                                            time = "11:00 - 14:00",
                                            isUrgent = false,
                                            userId = userId
                                        )
                                        val baseCourse4 = Course(
                                            name = "BIOLOGY",
                                            teacher = "Dr. Charles Darwin",
                                            day = "Friday",
                                            time = "12:00 - 15:00",
                                            isUrgent = false,
                                            userId = userId
                                        )

                                        courseDao.insertCourse(baseCourse)
                                        courseDao.insertCourse(baseCourse2)
                                        courseDao.insertCourse(baseCourse3)
                                        courseDao.insertCourse(baseCourse4)

                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(context, "Account created!", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(context, "User already exists.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }

                                withContext(Dispatchers.Main) {
                                    isSubmitting = false
                                    currentScreen = "start"
                                }
                            }
                        },
                        onBack = { currentScreen = "start" }
                    )

                    // Overview Screen: show courses for the current user
                    "overview" -> loggedInUser?.let { user ->
                        val overviewMenuState = rememberMenuState()   //  ←  add this line
                        CourseOverviewScreen(
                            name = user.name,
                            surname = user.surname,
                            school = user.school,
                            semester = user.semester,
                            courses = coursesList,
                            onBack = {
                                // Logout: clear prefs and state
                                val sharedPrefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                                sharedPrefs.edit().clear().apply()
                                loggedInUser = null
                                currentScreen = "start"
                            },
                            onCourseSelected = { course ->
                                selectedCourse = course
                                selectedCourseIndex = coursesList.indexOf(course) // NEW
                                currentScreen = "courseDetail"
                            },
                            onAddNewCourse = {
                                currentScreen = "newCourse"
                            },
                            menuState  = overviewMenuState,
                            onSettings = { currentScreen = "editUser" }
                        )
                    }

                    "editUser" -> loggedInUser?.let { u ->
                        EditUserScreen(
                            initialName     = u.name,
                            initialSurname  = u.surname,
                            initialSchool   = u.school,
                            initialSemester = u.semester,
                            onSave = { n, s, sc, sem ->
                                // update DB on IO thread then…
                                loggedInUser = loggedInUser?.copy(
                                    name = n, surname = s, school = sc, semester = sem
                                )
                                currentScreen = "overview"
                            },
                            onBack = { currentScreen = "overview" }
                        )
                    }


                    "editCourse" -> courseForEdit?.let { c ->
                        EditCourseScreen(
                            course = c,
                            onSave = { updated ->
                                coroutineScope.launch(Dispatchers.IO) {
                                    val updatedWithUser = updated.copy(userId = loggedInUser!!.id)
                                    courseDao.insertCourse(updatedWithUser)  // Room will upsert due to @PrimaryKey

                                    withContext(Dispatchers.Main) {
                                        selectedCourse = updatedWithUser
                                        currentScreen = "courseDetail"
                                    }
                                }
                            },
                            onBack = {
                                currentScreen = "courseDetail"
                            }
                        )
                    }

                    // New Course Screen: create a new course

                    "newCourse" -> loggedInUser?.let { user ->
                        val newCourse = Course(
                            name = "",
                            teacher = "",
                            day = "",
                            time = "",
                            isUrgent = false,
                            pages = emptyList(),
                            todoList = emptyList(),
                            userId = user.id
                        )

                        EditCourseScreen(
                            course = newCourse,
                            onSave = { created ->
                                coroutineScope.launch(Dispatchers.IO) {
                                    courseDao.insertCourse(created)
                                    withContext(Dispatchers.Main) {
                                        selectedCourse = created
                                        currentScreen = "courseDetail"
                                    }
                                }
                            },
                            onBack = {
                                // 🔧 Clear state if necessary, then return
                                selectedCourse = null
                                currentScreen = "overview"
                            }
                        )
                    }



                    // CourseDetail Screen: edit or save a course
                    // ──────────────────────────────────────────────────────────────
                    "courseDetail" -> {
                        val detailMenuState = rememberMenuState()      // ➊ NEW

                        selectedCourse?.let { course ->
                            CourseDetailScreen(
                                course   = course,
                                index    = selectedCourseIndex,

                                // ➋ NEW arguments ↓↓↓
                                menuState = detailMenuState,
                                onSettings = {
                                    courseForEdit = course          // keep a reference if you need it
                                    currentScreen = "editCourse"    // switch to the new screen
                                },

                                onCourseChange = { updatedCourse ->     // existing
                                    coroutineScope.launch(Dispatchers.IO) {
                                        if (updatedCourse.name.isNotBlank()) {
                                            val updatedWithUser = updatedCourse.copy(userId = loggedInUser!!.id)
                                            courseDao.insertCourse(updatedWithUser)
                                        }
                                        withContext(Dispatchers.Main) {
                                            selectedCourse = null
                                            currentScreen  = "overview"
                                        }
                                    }
                                },
                                onBack = {                               // existing
                                    selectedCourse = null
                                    currentScreen  = "overview"
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}

// CODE STRUCTURE NOTES:
// 1) Duplicate onBack parameter removed from SignUpScreen call.
// 2) Consider using a sealed class or enum for "currentScreen" instead of raw strings to avoid typos.
// 3) The DAO "authenticate" is called twice during sign-up; a unique constraint in the database could prevent duplicates more efficiently.
// 4) Avoid reading/writing SharedPreferences and Room DAO on the UI thread; ensure withContext(Dispatchers.IO) wraps all DB logic.
// 5) For better scalability, extract navigation logic into a dedicated Navigator or use a library (e.g. Jetpack Navigation Compose).
