package com.example.semester_project_app_dev.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semester_project_app_dev.R

@Composable
fun NotesOverviewScreen(
    name: String,
    surname: String,
    school: String,
    semester: String,
    onBack: () -> Unit
) {
    val avatarPainter = painterResource(R.drawable.avatar)
    val backgroundPainter = painterResource(R.drawable.bg_notebook)
    val backPainter = painterResource(R.drawable.img_home_btn)
    val menuIcon = painterResource(R.drawable.menu_icon)

    var isMenuOpen by remember { mutableStateOf(false) }

    val subjects = listOf(
        SubjectCardData("MATH", "Mr. Andrew Miller", "Monday", "08:00 – 09:30", R.drawable.artboard_21),
        SubjectCardData("ENGLISH", "Ms. Sara Bannett", "Tuesday", "13:00 – 15:00", R.drawable.artboard_20),
        SubjectCardData("BIOLOGY", "Dr. Nina Patel", "Monday", "09:45 – 11:45", R.drawable.artboard_24),
        SubjectCardData("HISTORY", "Mr. Daniel Kim", "Tuesday", "09:45 – 11:45", R.drawable.artboard_25),
        SubjectCardData("MUSIC", "Mr. Liam O'Connor", "Wednesday", "09:45 – 11:45", R.drawable.artboard_23),
        SubjectCardData("PHYSICS", "Ms. Angela Torres", "Wednesday", "13:00 – 15:00", R.drawable.artboard_22)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        Image(
            painter = backgroundPainter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        // Back Button (Top Left)
        Image(
            painter = backPainter,
            contentDescription = "Back",
            modifier = Modifier
                .padding(16.dp)
                .size(32.dp)
                .align(Alignment.TopStart)
                .clickable { onBack() }
        )

        // Menu Button (Top Right)
        Image(
            painter = menuIcon,
            contentDescription = "Menu",
            modifier = Modifier
                .padding(16.dp)
                .size(32.dp)
                .align(Alignment.TopEnd)
                .clickable { isMenuOpen = true }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = avatarPainter,
                contentDescription = "Avatar",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "$name $surname", style = MaterialTheme.typography.headlineSmall, fontSize = 28.sp)
            Text(text = school, fontSize = 18.sp)
            Text(text = "Semester $semester", fontSize = 18.sp)

            Spacer(modifier = Modifier.height(50.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(subjects) { subject ->
                    Image(
                        painter = painterResource(subject.bgRes),
                        contentDescription = subject.name,
                        modifier = Modifier
                            .height(130.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }

        // Grey overlay when menu is open
        if (isMenuOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable { isMenuOpen = false }
            )
        }

        // Slide-in full sidebar image
        AnimatedVisibility(
            visible = isMenuOpen,
            enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)),
            exit = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Image(
                painter = painterResource(R.drawable.sidebar),
                contentDescription = "Sidebar",
                modifier = Modifier
                    .fillMaxHeight()
                    .width(118.dp)
            )
        }

    }
}

@Composable
fun IconWithLabel(@DrawableRes iconRes: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = label,
            modifier = Modifier.size(40.dp)
        )
        Text(text = label, style = MaterialTheme.typography.labelMedium)
    }
}

data class SubjectCardData(
    val name: String,
    val teacher: String,
    val day: String,
    val time: String,
    val bgRes: Int,
    val urgent: Boolean = false
)
