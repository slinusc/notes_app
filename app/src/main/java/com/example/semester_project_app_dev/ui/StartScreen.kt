// app/src/main/kotlin/com/example/semester_project_app_dev/ui/StartScreen.kt
package com.example.semester_project_app_dev.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.semester_project_app_dev.R

@Composable
fun StartScreen(
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    // define your button shape
    val btnShape = RoundedCornerShape(12.dp)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = MaterialTheme.colors.background
    ) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            androidx.compose.foundation.Image(
                painter            = painterResource(R.drawable.logo),
                contentDescription = "App Logo",
                modifier           = Modifier.size(120.dp)
            )

            Spacer(Modifier.height(48.dp))

            // — Sign Up
            PressableImage(
                imageRes           = R.drawable.sign_up,
                contentDescription = "Sign up",
                onClick            = onSignUpClick,
                modifier           = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(btnShape)
            )
            androidx.compose.material.Text(
                text     = stringResource(R.string.unlock_potential),
                style    = MaterialTheme.typography.caption,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(Modifier.height(24.dp))

            // — Log In
            PressableImage(
                imageRes           = R.drawable.log_in,
                contentDescription = "Log in",
                onClick            = onLoginClick,
                modifier           = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(btnShape)
            )
            androidx.compose.material.Text(
                text     = stringResource(R.string.welcome_back),
                style    = MaterialTheme.typography.caption,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
