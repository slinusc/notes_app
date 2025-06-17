package com.example.semester_project_app_dev.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.semester_project_app_dev.data.Course
import com.example.semester_project_app_dev.data.User
import com.example.semester_project_app_dev.state.MenuState

@Preview(showBackground = true)
@Composable
fun PreviewStartScreen() {
    StartScreen(
        onSignUpClick = {},
        onLoginClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(
        onLogin = { _, _, _ -> },
        onBack = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    SignUpScreen(
        onSignUp = { _, _, _, _, _ -> },
        onBack = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCourseOverviewScreen() {
    CourseOverviewScreen(
        name = "Jane",
        surname = "Doe",
        school = "ZHAW",
        semester = "5",
        courses = listOf(
            Course(name = "Math", teacher = "Dr. Gauss", day = "Monday", time = "09:00", userId = 1),
            Course(name = "Physics", teacher = "Dr. Einstein", day = "Tuesday", time = "11:00", userId = 1)
        ),
        onBack = {},
        onCourseSelected = {},
        onAddNewCourse = {},
        onSettings = {},
        menuState = MenuState()
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewEditUserScreen() {
    EditUserScreen(
        initialName = "Jane",
        initialSurname = "Doe",
        initialSchool = "NTUT",
        initialSemester = "5",
        onSave = { _, _, _, _ -> },
        onBack = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeworkScreen() {
    HomeworkScreen(
        course = Course(name = "Biology", teacher = "Dr. Darwin", day = "Friday", time = "12:00", userId = 1),
        homeworks = listOf(
            com.example.semester_project_app_dev.data.HomeworkItem(
                id = 1,
                courseId = 1,
                title = "Cell Structure Review",
                details = "Label the parts of a cell.\nWrite their functions.",
                dueDay = "Friday",
                isDone = false
            ),
            com.example.semester_project_app_dev.data.HomeworkItem(
                id = 2,
                courseId = 1,
                title = "DNA vs RNA",
                details = "Compare and contrast DNA and RNA.",
                dueDay = "Monday",
                isDone = true
            )
        ),
        onBack = {},
        onToggleDone = {},
        onAddHomework = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewMeetingScreen() {
    MeetingScreen(
        course = Course(name = "History", teacher = "Dr. Herodotus", day = "Thursday", time = "10:00", userId = 1),
        onBack = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewEditCourseScreen() {
    EditCourseScreen(
        course = Course(name = "Chemistry", teacher = "Dr. Curie", day = "Wednesday", time = "14:00", userId = 1),
        onSave = {},
        onBack = {},
        onDelete = {},
        isNew = false
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewNewCourseScreen() {
    EditCourseScreen(
        course = Course(name = "", teacher = "", day = "", time = "", userId = 1),
        onSave = {},
        onBack = {},
        isNew = true
    )
}
