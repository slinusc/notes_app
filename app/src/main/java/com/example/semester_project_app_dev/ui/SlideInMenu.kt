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
                    painter = painterResource(R.drawable.sidebar),
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
                ) {

                    Box(
                        modifier = Modifier
                            .padding(top = 150.dp, start = 58.dp)
                            .size(40.dp) // base size
                    ) {
                        // Background image (purple icon)
                        Image(
                            painter = painterResource(R.drawable.purple),
                            contentDescription = "Purple Icon",
                            modifier = Modifier
                                .fillMaxSize()  // fills the 40.dp box
                        )

                        // Pressable settings icon on top
                        PressableImage(
                            imageRes = R.drawable.settings,
                            contentDescription = "Settings",
                            onClick = {
                                onSettingsClick()
                                menuState.close()
                            },
                            modifier = Modifier
                                .fillMaxSize()     // same size as purple icon
                                .zIndex(1f)        // ensure it's on top
                        )
                    }




                }
            }
        }
    }
}
