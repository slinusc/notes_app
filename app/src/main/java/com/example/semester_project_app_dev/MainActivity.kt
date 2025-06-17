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
import com.example.semester_project_app_dev.ui.HomeworkScreen
import com.example.semester_project_app_dev.ui.MeetingScreen


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Toast
import com.example.semester_project_app_dev.data.HomeworkItem
import com.example.semester_project_app_dev.state.rememberMenuState
import com.example.semester_project_app_dev.ui.AddHomeworkScreen


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
                                            name = "English",
                                            teacher = "Mr. Shakespeare",
                                            day = "Tuesday",
                                            time = "09:00 - 12:00",
                                            isUrgent = false,
                                            userId = userId
                                        )
                                        val baseCourse2 = Course(
                                            name = "Physics",
                                            teacher = "Dr. Albert Einstein",
                                            day = "Wednesday",
                                            time = "10:00 - 13:00",
                                            isUrgent = true,
                                            userId = userId
                                        )
                                        val baseCourse3 = Course(
                                            name = "Chemistry",
                                            teacher = "Dr. Marie Curie",
                                            day = "Thursday",
                                            time = "11:00 - 14:00",
                                            isUrgent = false,
                                            userId = userId
                                        )
                                        val baseCourse4 = Course(
                                            name = "Biology",
                                            teacher = "Dr. Charles Darwin",
                                            day = "Friday",
                                            time = "12:00 - 15:00",
                                            isUrgent = false,
                                            userId = userId
                                        )
                                        var baseCourse5 = Course(
                                            name = "Algorithms",
                                            teacher = "Mr. Alan Turing",
                                            day = "Monday",
                                            time = "08:00 - 11:00",
                                            isUrgent = true,
                                            userId = userId
                                        )

                                        val homeworkDao = db.homeworkDao()

                                        val englishId = courseDao.insertCourse(baseCourse).toInt()
                                        homeworkDao.insertHomework(
                                            HomeworkItem(
                                                courseId = englishId,
                                                title = "Read Act 1 of Hamlet",
                                                details = "Summarize the main events and write a reflection paragraph.",
                                                dueDay = "Monday"
                                            )
                                        )

                                        val physicsId = courseDao.insertCourse(baseCourse2).toInt()
                                        homeworkDao.insertHomework(
                                            HomeworkItem(
                                                courseId = physicsId,
                                                title = "Exercise 5",
                                                details = "Solve problems 1-10.",
                                                dueDay = "Tuesday"
                                            )
                                        )

                                        val chemistryId = courseDao.insertCourse(baseCourse3).toInt()
                                        homeworkDao.insertHomework(
                                            HomeworkItem(
                                                courseId = chemistryId,
                                                title = "Lab Safety Assignment",
                                                details = "List 10 lab safety rules and describe their importance.",
                                                dueDay = "Wednesday"
                                            )
                                        )

                                        val biologyId = courseDao.insertCourse(baseCourse4).toInt()
                                        homeworkDao.insertHomework(
                                            HomeworkItem(
                                                courseId = biologyId,
                                                title = "Cell Structure Review",
                                                details = "Label the parts of a cell and write their functions.",
                                                dueDay = "Thursday"
                                            )
                                        )

                                        val algoId = courseDao.insertCourse(baseCourse5).toInt()
                                        homeworkDao.insertHomework(
                                            HomeworkItem(
                                                courseId = algoId,
                                                title = "Implement Bubble Sort",
                                                details = "Write the bubble sort algorithm and test it on 5 arrays.",
                                                dueDay = "Friday"
                                            )
                                        )

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
                        val overviewMenuState = rememberMenuState()
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


                    "homework" -> selectedCourse?.let { course ->
                        val homeworkDao = db.homeworkDao()
                        val homeworkFlow = remember(course) {
                            homeworkDao.getHomeworkForCourse(course.id)
                        }
                        val homeworkList by homeworkFlow.collectAsState(initial = emptyList())

                        HomeworkScreen(
                            course = course,
                            homeworks = homeworkList,
                            onBack = {
                                currentScreen = "courseDetail"
                            },
                            onToggleDone = { hw ->
                                coroutineScope.launch(Dispatchers.IO) {
                                    homeworkDao.updateHomework(hw.copy(isDone = !hw.isDone))
                                }
                            },
                            onAddHomework = {
                                currentScreen = "newHomework"
                            }
                        )
                    }

                    "newHomework" -> selectedCourse?.let { course ->
                        AddHomeworkScreen(
                            courseId = course.id,
                            onSave = { newHomeworkItem ->
                                coroutineScope.launch {
                                    db.homeworkDao().insertHomework(newHomeworkItem)
                                    withContext(Dispatchers.Main) {
                                        currentScreen = "homework"
                                    }
                                }
                            },
                            onCancel = { currentScreen = "homework" }
                        )
                    }

                    "meeting" -> selectedCourse?.let { course ->
                        MeetingScreen(
                            course = course,
                            onBack = {
                                currentScreen = "courseDetail"
                            }
                        )
                    }


                    "editCourse" -> courseForEdit?.let { c ->
                        EditCourseScreen(
                            course = c,
                            isNew = false,
                            onSave = { updated ->
                                coroutineScope.launch(Dispatchers.IO) {
                                    val updatedWithUser = updated.copy(userId = loggedInUser!!.id)
                                    courseDao.insertCourse(updatedWithUser)
                                    withContext(Dispatchers.Main) {
                                        selectedCourse = updatedWithUser
                                        currentScreen = "courseDetail"
                                    }
                                }
                            },
                            onDelete = {
                                coroutineScope.launch(Dispatchers.IO) {
                                    courseDao.deleteCourse(c)  // ← uses your @Delete
                                    withContext(Dispatchers.Main) {
                                        selectedCourse = null
                                        currentScreen = "overview"
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
                            isNew = true,
                            onSave = { created ->
                                coroutineScope.launch(Dispatchers.IO) {
                                    val newId = courseDao.insertCourse(created)
                                    val savedCourse = created.copy(id = newId.toInt())
                                    withContext(Dispatchers.Main) {
                                        selectedCourse = savedCourse
                                        currentScreen = "overview"
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
                        val detailMenuState = rememberMenuState()

                        selectedCourse?.let { course ->
                            CourseDetailScreen(
                                course = course,
                                index = selectedCourseIndex,
                                menuState = detailMenuState,
                                onSettings = { updatedCourse ->
                                    coroutineScope.launch(Dispatchers.IO) {
                                        val updatedWithUser = updatedCourse.copy(userId = loggedInUser!!.id)
                                        courseDao.insertCourse(updatedWithUser)
                                        withContext(Dispatchers.Main) {
                                            selectedCourse = updatedWithUser
                                            courseForEdit = updatedWithUser
                                            currentScreen = "editCourse"
                                        }
                                    }
                                },
                                onCourseChange = { updatedCourse ->
                                    coroutineScope.launch(Dispatchers.IO) {
                                        if (updatedCourse.name.isNotBlank()) {
                                            val updatedWithUser = updatedCourse.copy(userId = loggedInUser!!.id)
                                            courseDao.insertCourse(updatedWithUser)

                                            withContext(Dispatchers.Main) {
                                                selectedCourse = updatedWithUser
                                                currentScreen = "overview"
                                            }
                                        } else {
                                            withContext(Dispatchers.Main) {
                                                currentScreen = "overview"
                                            }
                                        }
                                    }
                                }

                                ,
                                onBack = {
                                    selectedCourse = null
                                    currentScreen = "overview"
                                },

                                onHomeworkClick = { updatedCourse ->
                                    coroutineScope.launch(Dispatchers.IO) {
                                        val updatedWithUser = updatedCourse.copy(userId = loggedInUser!!.id)
                                        courseDao.insertCourse(updatedWithUser)
                                        withContext(Dispatchers.Main) {
                                            selectedCourse = updatedWithUser
                                            currentScreen = "homework"
                                        }
                                    }
                                },
                                onMeetingClick = { updatedCourse ->
                                    coroutineScope.launch(Dispatchers.IO) {
                                        val updatedWithUser = updatedCourse.copy(userId = loggedInUser!!.id)
                                        courseDao.insertCourse(updatedWithUser)
                                        withContext(Dispatchers.Main) {
                                            selectedCourse = updatedWithUser
                                            currentScreen = "meeting"
                                        }
                                    }
                                }


                            )
                        }
                    }

                }
            }
        }
    }
}