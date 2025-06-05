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

@Composable
fun SignUpScreen(
    onSignUp: (name: String, surname: String, school: String, year: String, semester: String, password: String) -> Unit = { _, _, _, _, _, _ -> }
) {
    var name by rememberSaveable { mutableStateOf("") }
    var surname by rememberSaveable { mutableStateOf("") }
    var school by rememberSaveable { mutableStateOf("") }
    var year by rememberSaveable { mutableStateOf("") }
    var semester by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val notebookPainter = painterResource(R.drawable.bg_notebook)
    val welcomePainter = painterResource(R.drawable.img_welcome) // reuse or replace with "sign up" variant
    val pencilPainter = painterResource(R.drawable.img_pencil)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = notebookPainter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp)
        ) {
            Image(
                painter = welcomePainter,
                contentDescription = "Hello, Sign Up headline",
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(120.dp))

            TapedTextField(value = name, onValueChange = { name = it }, bgRes = R.drawable.bg_field_name, placeholder = "Name")
            Spacer(modifier = Modifier.height(16.dp))

            TapedTextField(value = surname, onValueChange = { surname = it }, bgRes = R.drawable.bg_field_surname, placeholder = "Surname")
            Spacer(modifier = Modifier.height(16.dp))

            TapedTextField(value = school, onValueChange = { school = it }, bgRes = R.drawable.artboard_46, placeholder = "School")
            Spacer(modifier = Modifier.height(16.dp))

            TapedTextField(value = year, onValueChange = { year = it }, bgRes = R.drawable.artboard_48, placeholder = "Year")
            Spacer(modifier = Modifier.height(16.dp))

            TapedTextField(
                value = password,
                onValueChange = { password = it },
                bgRes = R.drawable.bg_field_password,
                placeholder = "Password",
                isPassword = true
            )
            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val allFilled = listOf(name, surname, school, year, semester, password).all { it.isNotBlank() }
                if (allFilled) {
                    PressableImage(
                        imageRes = R.drawable.sign_up,
                        contentDescription = "Sign up",
                        onClick = { onSignUp(name, surname, school, year, semester, password) },
                        modifier = Modifier
                            .width(180.dp)
                            .height(50.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.sign_up),
                        contentDescription = "Sign up (disabled)",
                        modifier = Modifier
                            .width(180.dp)
                            .height(50.dp)
                    )
                }
            }
        }

        Image(
            painter = pencilPainter,
            contentDescription = null,
            modifier = Modifier
                .size(width = 160.dp, height = 160.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-32).dp, y = 48.dp)
                .zIndex(1f),
            contentScale = ContentScale.Fit
        )
    }
}
