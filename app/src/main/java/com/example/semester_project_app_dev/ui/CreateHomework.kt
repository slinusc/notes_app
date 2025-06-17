package com.example.semester_project_app_dev.ui

import androidx.compose.foundation.Image
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
import com.example.semester_project_app_dev.data.HomeworkItem
import com.example.semester_project_app_dev.ui.PressableImage
import com.example.semester_project_app_dev.ui.PressEffect

@Composable
fun CreateHomeworkScreen(
    courseId: Int,
    onSave: (HomeworkItem) -> Unit,
    onCancel: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var dueDay by rememberSaveable { mutableStateOf("") }
    var details by rememberSaveable { mutableStateOf("") }

    val bgPainter = painterResource(R.drawable.bg_notebook)
    val folderPainter = painterResource(R.drawable.folder_homework)
    val savePainter = painterResource(R.drawable.save)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = bgPainter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        PressableImage(
            imageRes = R.drawable.back_arrow,
            contentDescription = "Back",
            width = 32.dp,
            height = 32.dp,
            onClick = onCancel,
            modifier = Modifier
                .padding(top = 16.dp, start = 30.dp)
                .align(Alignment.TopStart)
                .zIndex(1f)
        )

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(R.drawable.create_homework),
                    contentDescription = "Create Homework Title",
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

            TapedTextField(
                value = title,
                onValueChange = { title = it },
                bgRes = R.drawable.hw_title, // reuse course name field style
                placeholder = "",
                contentPadding = PaddingValues(start = 36.dp, top = 42.dp)
            )
            Spacer(Modifier.height(12.dp))

            TapedTextField(
                value = dueDay,
                onValueChange = { dueDay = it },
                bgRes = R.drawable.day,
                placeholder = "",
                contentPadding = PaddingValues(start = 36.dp, top = 42.dp)
            )
            Spacer(Modifier.height(12.dp))

            TapedTextField(
                value = details,
                onValueChange = { details = it },
                bgRes = R.drawable.details,
                placeholder = "",
                contentPadding = PaddingValues(start = 36.dp, top = 38.dp)
            )

            Spacer(Modifier.height(32.dp))

            val ready = listOf(title, dueDay, details).all { it.isNotBlank() }

            if (ready) {
                PressableImage(
                    imageRes = R.drawable.save,
                    contentDescription = "Save",
                    onClick = {
                        onSave(
                            HomeworkItem(
                                courseId = courseId,
                                title = title,
                                dueDay = dueDay,
                                details = details
                            )
                        )
                    },
                    modifier = Modifier.height(45.dp),
                    pressEffect = PressEffect.Light
                )
            } else {
                Image(
                    painter = savePainter,
                    contentDescription = "Save (disabled)",
                    modifier = Modifier.height(45.dp)
                )
            }
        }
    }
}
