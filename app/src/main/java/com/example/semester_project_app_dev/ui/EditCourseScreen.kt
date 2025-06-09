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
    onBack: () -> Unit = {},
    onDelete: (() -> Unit)? = null,
    isNew: Boolean     // ← add this parameter
)
{
    /* ── editable fields ─────────────────────────────────────────── */
    var name    by rememberSaveable { mutableStateOf(course.name) }
    var teacher by rememberSaveable { mutableStateOf(course.teacher) }
    var day     by rememberSaveable { mutableStateOf(course.day) }
    var time    by rememberSaveable { mutableStateOf(course.time) }

    /* ── drawables ──────────────────────────────────────────────── */
    val bgNotebook   = painterResource(R.drawable.bg_notebook)
    val titlePainter2 = painterResource(R.drawable.create_course)     // “Create course”
    val folderPainter= painterResource(R.drawable.folder_edit_course)  // big blue folder
    val savePainter  = painterResource(R.drawable.save)             // blue “save” pill
    val titlePainter = if (isNew)
        painterResource(R.drawable.create_course)
    else
        painterResource(R.drawable.edit_course)
    var showConfirmDialog by remember { mutableStateOf(false) }


    Box(Modifier.fillMaxSize()) {



        /* background */
        Image(
            painter = bgNotebook,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        /* back button */
        PressableImage(
            imageRes = R.drawable.back_arrow, // assuming backPainter = painterResource(R.drawable.back_arrow)
            contentDescription = "Back",
            width = 32.dp,
            height = 32.dp,
            onClick = onBack,
            modifier = Modifier
                .padding(top = 16.dp, start = 30.dp)
                .align(Alignment.TopStart)
                .zIndex(1f)
        )

        /* scrollable form */
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /* header row: title + folder icon */
            Box(Modifier.fillMaxWidth()) {
                Image(
                    painter = titlePainter,
                    contentDescription = if (isNew) "Create course title" else "Edit course title",
                    modifier = Modifier
                        .padding(top = 60.dp, start = 15.dp)
                        .height(40.dp)
                        .align(Alignment.TopStart),
                    contentScale = ContentScale.Fit
                )

                Image(
                    painter = folderPainter,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(140.dp)
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
                placeholder = "",
                contentPadding = PaddingValues(start = 36.dp, top = 42.dp)
            )
            Spacer(Modifier.height(12.dp))

            TapedTextField(
                value = teacher,
                onValueChange = { teacher = it },
                bgRes = R.drawable.teacher, // "Teacher"
                placeholder = "",
                contentPadding = PaddingValues(start = 36.dp, top = 42.dp)
            )
            Spacer(Modifier.height(12.dp))

            TapedTextField(
                value = day,
                onValueChange = { day = it },
                bgRes = R.drawable.day, // "Day"
                placeholder = "",
                contentPadding = PaddingValues(start = 36.dp, top = 38.dp)
            )
            Spacer(Modifier.height(12.dp))

            TapedTextField(
                value = time,
                onValueChange = { time = it },
                bgRes = R.drawable.time, // "Time"
                placeholder = "",
                contentPadding = PaddingValues(start = 36.dp, top = 38.dp)
            )

            Spacer(Modifier.height(32.dp))

            /* save button */
            val ready = listOf(name, teacher, day, time).all { it.isNotBlank() }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (ready) {
                    PressableImage(
                        imageRes = R.drawable.save,
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
                            .height(45.dp),
                        pressEffect = PressEffect.Light
                    )
                } else {
                    Image(
                        painter = savePainter,
                        contentDescription = "Save (disabled)",
                        modifier = Modifier
                            .height(45.dp)
                    )
                }

                if (!isNew) {
                    PressableImage(
                        imageRes = R.drawable.delete,
                        contentDescription = "Delete",
                        onClick = { showConfirmDialog = true },
                        modifier = Modifier.height(45.dp),
                        pressEffect = PressEffect.Light
                    )
                }

            }
            if (showConfirmDialog) {
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { showConfirmDialog = false },
                    confirmButton = {
                        androidx.compose.material3.TextButton(onClick = {
                            showConfirmDialog = false
                            onDelete?.invoke()
                        }) {
                            androidx.compose.material3.Text("Delete")
                        }
                    },
                    dismissButton = {
                        androidx.compose.material3.TextButton(onClick = {
                            showConfirmDialog = false
                        }) {
                            androidx.compose.material3.Text("Cancel")
                        }
                    },
                    title = { androidx.compose.material3.Text("Confirm Deletion") },
                    text = { androidx.compose.material3.Text("Are you sure you want to delete this course? This action cannot be undone.") }
                )
            }

        }
    }
}
