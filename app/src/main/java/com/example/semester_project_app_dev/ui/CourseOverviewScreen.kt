// File: app/src/main/java/com/example/semester_project_app_dev/ui/CourseOverviewScreen.kt

package com.example.semester_project_app_dev.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.semester_project_app_dev.R
import com.example.semester_project_app_dev.data.Course
import com.example.semester_project_app_dev.state.MenuState
import com.example.semester_project_app_dev.ui.components.SlideInMenu

/**
 * Shows a grid of exactly:
 *  • one CourseCard for each Course in `courses: List<Course>`,
 *  • followed by exactly one “Add New Course” tile at the end.
 */
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
    val backPainter = painterResource(R.drawable.img_home_btn)
    val menuIcon = painterResource(R.drawable.menu_icon)

    fun navigateToEditUser() { onSettings() }   // ← call the lambda, not navController

    Box(modifier = Modifier.fillMaxSize()) {
        // 1) Background image (fills entire screen)
        Image(
            painter = backgroundPainter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        // 2) Back button (top‐left)
        Image(
            painter = backPainter,
            contentDescription = "Back",
            modifier = Modifier
                .padding(16.dp)
                .size(32.dp)
                .align(Alignment.TopStart)
                .clickable { onBack() }
        )

        // 3) Menu button (top‐right)
        Image(
            painter = menuIcon,
            contentDescription = "Menu",
            modifier = Modifier
                .padding(16.dp)
                .size(32.dp)
                .align(Alignment.TopEnd)
                .clickable { menuState.toggle() }
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // — Avatar + user name + school + semester —
            Image(
                painter = avatarPainter,
                contentDescription = "Avatar",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
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

            Spacer(modifier = Modifier.height(50.dp))

            // — 4) Grid of CourseCards plus EXACTLY ONE “Add” tile at end —
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // a) For _each_ Course in the list, show a folder
                items(courses) { course ->
                    // Determine the index of this course so CourseCard picks the same color/drawable
                    val index = courses.indexOf(course)
                    CourseCard(
                        course = course,
                        index = index,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        onClick = { onCourseSelected(course) }
                    )
                }
                // b) Then exactly one “Add New Course” tile:
                item {
                    // Use index = courses.size so it picks the drawable after the last
                    CourseCard(
                        course = null,
                        index = courses.size,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        onClick = { onAddNewCourse() }
                    )
                }
            }
        }

        SlideInMenu(
            menuState = menuState,
            onSettingsClick = { navigateToEditUser() }
        )


    }
}
