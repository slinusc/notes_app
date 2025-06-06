package com.example.semester_project_app_dev.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.semester_project_app_dev.R
import com.example.semester_project_app_dev.data.Course


@Composable
fun EditCourseScreen(
    course: Course,
    onSave: (Course) -> Unit,
    onBack: () -> Unit = {}
) {
    /* ── editable fields ─────────────────────────────────────────── */
    var name    by rememberSaveable { mutableStateOf(course.name) }
    var teacher by rememberSaveable { mutableStateOf(course.teacher) }
    var day     by rememberSaveable { mutableStateOf(course.day) }
    var time    by rememberSaveable { mutableStateOf(course.time) }

    /* ── drawables ──────────────────────────────────────────────── */
    val bgNotebook   = painterResource(R.drawable.bg_notebook)
    val titlePainter = painterResource(R.drawable.edit_course)         // “Edit course”
    // val titlePainter2 = painterResource(R.drawable.edit_course2)     // “Create course”
    val folderPainter= painterResource(R.drawable.folder_edit_course)  // big blue folder
    val backPainter  = painterResource(R.drawable.back_arrow)
    val savePainter  = painterResource(R.drawable.save)             // blue “save” pill

    Box(Modifier.fillMaxSize()) {
        /* background */
        Image(
            painter = bgNotebook,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        /* back button */
        Image(
            painter = backPainter,
            contentDescription = "Back",
            modifier = Modifier
                .padding(15.dp)
                .size(30.dp)
                .align(Alignment.TopStart)
                .clickable(onClick = onBack)
                .zIndex(1f) // ensure it is above the scrollable content
        )

        /* scrollable form */
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /* header row: title + folder icon */
            Box(Modifier.fillMaxWidth()) {
                Image(
                    painter = titlePainter,
                    contentDescription = "Edit course title",
                    modifier = Modifier
                        .padding(top = 40.dp, start = 15.dp)
                        .height(40.dp)
                        .align(Alignment.TopStart),
                    contentScale = ContentScale.Fit
                )
                Image(
                    painter = folderPainter,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(130.dp)
                        .align(Alignment.TopEnd),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(Modifier.height(40.dp))

            /* taped text fields */
            TapedTextField(
                value = name,
                onValueChange = { name = it },
                bgRes = R.drawable.course, // "Course name"
                placeholder = ""
            )
            Spacer(Modifier.height(12.dp))

            TapedTextField(
                value = teacher,
                onValueChange = { teacher = it },
                bgRes = R.drawable.teacher, // "Teacher"
                placeholder = ""
            )
            Spacer(Modifier.height(16.dp))

            TapedTextField(
                value = day,
                onValueChange = { day = it },
                bgRes = R.drawable.day, // "Day"
                placeholder = ""
            )
            Spacer(Modifier.height(18.dp))

            TapedTextField(
                value = time,
                onValueChange = { time = it },
                bgRes = R.drawable.time, // "Time"
                placeholder = ""
            )

            Spacer(Modifier.height(32.dp))

            /* save button */
            val ready = listOf(name, teacher, day, time).all { it.isNotBlank() }

            if (ready) {
                PressableImage(
                    imageRes = R.drawable.sign_up,
                    contentDescription = "Save",
                    onClick = {
                        onSave(
                            course.copy(
                                name = name,
                                teacher = teacher,
                                day = day,
                                time = time
                            )
                        )
                    },
                    modifier = Modifier
                        .width(180.dp)
                        .height(50.dp)
                )
            } else {
                Image(
                    painter = savePainter,
                    contentDescription = "Save (disabled)",
                    modifier = Modifier
                        .width(180.dp)
                        .height(50.dp)
                )
            }
        }
    }
}
