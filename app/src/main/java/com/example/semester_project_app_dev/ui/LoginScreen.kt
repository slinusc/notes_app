package com.example.semester_project_app_dev.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import com.example.semester_project_app_dev.R
import com.example.semester_project_app_dev.data.AppDatabase
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLogin: (name: String, surname: String, password: String) -> Unit = { _, _, _ -> },
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    val scope = rememberCoroutineScope()

    var name by rememberSaveable { mutableStateOf("") }
    var surname by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val notebookPainter = painterResource(R.drawable.bg_notebook)
    val welcomePainter = painterResource(R.drawable.img_welcome)
    val pencilPainter = painterResource(R.drawable.img_pencil)
    val backPainter = painterResource(R.drawable.img_home_btn) // ← Add your own back icon to res/drawable

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = notebookPainter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Back button (top-left corner)
            Image(
                painter = backPainter,
                contentDescription = "Back",
                    modifier = Modifier
                        .padding(15.dp)
                        .size(30.dp)
                        .align(Alignment.TopStart)
                    .clickable(onClick = onBack)
            )

        // Main content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp)
        ) {
            Image(
                painter = welcomePainter,
                contentDescription = "Welcome back, log in headline",
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(120.dp))

            TapedTextField(
                value = name,
                onValueChange = { name = it },
                bgRes = R.drawable.bg_field_name,
                placeholder = "Name"
            )
            Spacer(modifier = Modifier.height(16.dp))

            TapedTextField(
                value = surname,
                onValueChange = { surname = it },
                bgRes = R.drawable.bg_field_surname,
                placeholder = "Surname"
            )
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
                if (name.isNotBlank() && surname.isNotBlank() && password.isNotBlank()) {
                    PressableImage(
                        imageRes = R.drawable.log_in,
                        contentDescription = "Log in",
                        onClick = {
                            scope.launch {
                                val user = db.userDao().authenticate(name, surname, password)
                                if (user != null) {
                                    onLogin(name, surname, password)
                                } else {
                                    Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.height(48.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.log_in),
                        contentDescription = "Log in (disabled)",
                        modifier = Modifier.height(48.dp)
                    )
                }
            }
        }

        // Pencil image (top-right)
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



@Composable
fun TapedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    bgRes: Int,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
) {
    val bgPainter = painterResource(bgRes)

    Box(modifier = modifier) {
        Image(
            painter = bgPainter,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth()
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.DarkGray),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(start = 48.dp, end = 20.dp, top = 45.dp),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF6E6E6E))
                    )
                }
                innerTextField()
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun LoginScreenPreview2() {
    MaterialTheme {
        LoginScreen()
    }
}