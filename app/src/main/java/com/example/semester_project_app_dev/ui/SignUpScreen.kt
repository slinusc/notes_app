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
import androidx.compose.ui.platform.LocalContext
import com.example.semester_project_app_dev.data.AppDatabase


@Composable
fun SignUpScreen(
    onSignUp: (name: String, surname: String, school: String, semester: String, password: String) -> Unit = { _, _, _, _, _ -> },
    onBack: () -> Unit = {}
) {
    var name by rememberSaveable { mutableStateOf("") }
    var surname by rememberSaveable { mutableStateOf("") }
    var school by rememberSaveable { mutableStateOf("") }
    var semester by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    val scope = rememberCoroutineScope()

    val notebookPainter = painterResource(R.drawable.bg_notebook)
    val welcomePainter = painterResource(R.drawable.img_welcome_sign_in)
    val pencilPainter = painterResource(R.drawable.img_pencil)
    val backPainter = painterResource(R.drawable.back_arrow) // ← Add your own back icon to res/drawable

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = notebookPainter,
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

            TapedTextField(value = name, onValueChange = { name = it }, bgRes = R.drawable.bg_field_name, placeholder = "")
            Spacer(modifier = Modifier.height(16.dp))

            TapedTextField(value = surname, onValueChange = { surname = it }, bgRes = R.drawable.bg_field_surname, placeholder = "")
            Spacer(modifier = Modifier.height(16.dp))

            TapedTextField(value = school, onValueChange = { school = it }, bgRes = R.drawable.bg_school, placeholder = "")
            Spacer(modifier = Modifier.height(16.dp))

            TapedTextField(value = semester, onValueChange = { semester = it }, bgRes = R.drawable.bg_semester, placeholder = "")
            Spacer(modifier = Modifier.height(16.dp))

            TapedTextField(
                value = password,
                onValueChange = { password = it },
                bgRes = R.drawable.bg_field_password,
                placeholder = "",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val allFilled = listOf(name, surname, school, semester, password).all { it.isNotBlank() }
                if (allFilled) {
                    PressableImage(
                        imageRes = R.drawable.sign_up,
                        contentDescription = "Sign up",
                        onClick = {
                            onSignUp(name, surname, school, semester, password)
                        },
                        modifier = Modifier
                            .width(180.dp)
                            .height(50.dp),
                        pressEffect = PressEffect.Light
                    )
                } else {
                    PressableImage(
                        imageRes = R.drawable.sign_up,
                        contentDescription = "Sign up",
                        onClick = {// Do nothing if fields are not filled
                        },
                        modifier = Modifier
                            .width(180.dp)
                            .height(50.dp),
                        pressEffect = PressEffect.Light
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
