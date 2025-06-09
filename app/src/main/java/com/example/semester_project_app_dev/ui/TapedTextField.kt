package com.example.semester_project_app_dev.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun TapedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    bgRes: Int,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(start = 48.dp, end = 20.dp, top = 45.dp)
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
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text), // ← Add this
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(contentPadding),
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