package com.example.semester_project_app_dev.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.semester_project_app_dev.R

import androidx.compose.foundation.clickable


@Composable
fun EditUserScreen(
    initialName: String,
    initialSurname: String,
    initialSchool: String,
    initialSemester: String,
    onSave: (name: String, surname: String, school: String, semester: String) -> Unit,
    onBack: () -> Unit = {}
) {
    var name by rememberSaveable { mutableStateOf(initialName) }
    var surname by rememberSaveable { mutableStateOf(initialSurname) }
    var school by rememberSaveable { mutableStateOf(initialSchool) }
    var semester by rememberSaveable { mutableStateOf(initialSemester) }

    val notebookPainter = painterResource(R.drawable.bg_notebook)
    val titlePainter = painterResource(R.drawable.edit_user)          // "Edit User" title
    val avatarPainter = painterResource(R.drawable.avatar)            // avatar face
    val backPainter = painterResource(R.drawable.back_arrow)
    val savePainter = painterResource(R.drawable.save)             // blue save button

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        Image(
            painter = notebookPainter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Back button (top-left)
        PressableImage(
            imageRes = R.drawable.back_arrow, // replace with your actual drawable if different
            contentDescription = "Back",
            width = 30.dp,
            height = 30.dp,
            onClick = onBack,
            modifier = Modifier
                .padding(top = 16.dp, start = 30.dp)
                .align(Alignment.TopStart)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp)
        ) {
            // Header
            Box(
                Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = titlePainter,
                    contentDescription = "Edit User Title",
                    modifier = Modifier
                        .padding(top = 40.dp, start = 20.dp)
                        .height(40.dp)
                        .align(Alignment.TopStart),
                    contentScale = ContentScale.Fit
                )

                Image(
                    painter = avatarPainter,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .size(100.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-4).dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            TapedTextField(value = name,     onValueChange = { name = it },     bgRes = R.drawable.bg_field_name,    placeholder = "")
            Spacer(modifier = Modifier.height(16.dp))

            TapedTextField(value = surname,  onValueChange = { surname = it },  bgRes = R.drawable.bg_field_surname, placeholder = "")
            Spacer(modifier = Modifier.height(16.dp))

            TapedTextField(value = school,   onValueChange = { school = it },   bgRes = R.drawable.bg_school,        placeholder = "")
            Spacer(modifier = Modifier.height(16.dp))

            TapedTextField(value = semester, onValueChange = { semester = it }, bgRes = R.drawable.bg_semester,      placeholder = "")

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val allFilled = listOf(name, surname, school, semester).all { it.isNotBlank() }

                if (allFilled) {
                    PressableImage(
                        imageRes = R.drawable.save,
                        contentDescription = "Save",
                        onClick = {
                            onSave(name, surname, school, semester)
                        },
                        modifier = Modifier
                            .width(180.dp)
                            .height(50.dp),
                        pressEffect = PressEffect.Light
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
}
