// File: app/src/main/java/com/example/semester_project_app_dev/ui/CourseOverviewScreen.kt

package com.example.semester_project_app_dev.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.semester_project_app_dev.R
import com.example.semester_project_app_dev.data.Course
import com.example.semester_project_app_dev.state.MenuState
import com.example.semester_project_app_dev.ui.components.SlideInMenu

@Composable
fun CourseOverviewScreen(
    name: String,
    surname: String,
    school: String,
    semester: String,
    courses: List<Course>,            // ← pass in your DB‐loaded courses
    onBack: () -> Unit,
    onCourseSelected: (Course) -> Unit,
    onAddNewCourse: () -> Unit,
    menuState: MenuState,                // ← NEW
    onSettings: () -> Unit,
) {
    val avatarPainter = painterResource(R.drawable.avatar)
    val backgroundPainter = painterResource(R.drawable.bg_notebook)

    fun navigateToEditUser() { onSettings() }   // ← call the lambda, not navController

    Box(modifier = Modifier.fillMaxSize()) {
        // 1) Background image (fills entire screen)
        Image(
            painter = backgroundPainter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        // 2) Back button (top‐left)
        PressableImage(
            imageRes = R.drawable.back_arrow, // assuming `backIcon = painterResource(R.drawable.back_icon)`
            contentDescription = "Back",
            width = 32.dp,
            height = 32.dp,
            onClick = {
                onBack()
            },
            modifier = Modifier
                .padding(top = 16.dp, start = 30.dp)
                .align(Alignment.TopStart)
                .zIndex(2f)
        )

        PressableImage(
            imageRes = R.drawable.menu_icon,
            contentDescription = "Menu",
            width = 32.dp,
            height = 32.dp,
            onClick = { menuState.toggle() },
            modifier = Modifier
                .padding(top = 16.dp, end = 35.dp)
                .align(Alignment.TopEnd)
                .zIndex(if (menuState.isOpen) 0f else 2f)
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, start = 35.dp, end = 35.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // — Avatar + user name + school + semester —
            Image(
                painter = avatarPainter,
                contentDescription = "Avatar",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "$name $surname",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = school,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Semester $semester",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(30.dp))

            // — 4) Grid of CourseCards plus EXACTLY ONE “Add” tile at end —
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val allItems = courses + Course( // placeholder for "+"
                    id = -1, name = "", teacher = "", day = "", time = "",
                    isUrgent = false, pages = emptyList(), currentPageIndex = 0,
                    todoList = emptyList(), userId = -1
                )
                val rows = allItems.chunked(2)

                rows.forEachIndexed { rowIndex, row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        row.forEachIndexed { colIndex, course ->
                            val index = rowIndex * 2 + colIndex
                            val isAddTile = course.id == -1

                            CourseCard(
                                course = if (isAddTile) null else course,
                                index = index,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(130.dp),
                                onClick = {
                                    if (isAddTile) onAddNewCourse() else onCourseSelected(course)
                                }
                            )
                        }

                        if (row.size == 1) {
                            Spacer(modifier = Modifier.weight(1f)) // Fill empty column
                        }
                    }
                }
            }

        }

        SlideInMenu(
            menuState = menuState,
            onSettingsClick = { navigateToEditUser() }
        )
    }
}
