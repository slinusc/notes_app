package com.example.semester_project_app_dev.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.semester_project_app_dev.R
import com.example.semester_project_app_dev.state.MenuState
import com.example.semester_project_app_dev.ui.PressableImage
import androidx.compose.ui.zIndex

@Composable
fun SlideInMenu(
    menuState: MenuState,            // ← name : Type   (no “= …” here)
    onSettingsClick: () -> Unit,
    onInfoClick: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize().zIndex(3f)) {
        if (menuState.isOpen) {
            // Semi-transparent background overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable { menuState.close() }
            )
        }

        AnimatedVisibility(
            visible = menuState.isOpen,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            ),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(118.dp)
            ) {
                // Background image
                Image(
                    painter = painterResource(R.drawable.empty_sidebar),
                    contentDescription = "Sidebar",
                    modifier = Modifier
                        .fillMaxSize()
                )

                // Icons stacked on top
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 40.dp, start = 52.dp)
                ) {
                    PressableImage(
                        imageRes = R.drawable.settings,
                        contentDescription = "Settings",
                        width = 40.dp,
                        height = 40.dp,
                        onClick = {
                            onSettingsClick()          // ← delegate to parent screen
                            menuState.close()
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PressableImage(
                        imageRes = R.drawable.info,
                        contentDescription = "Info",
                        width = 40.dp,
                        height = 40.dp,
                        onClick = { /* TODO: Handle info */ }
                    )
                }
            }
        }
    }
}
