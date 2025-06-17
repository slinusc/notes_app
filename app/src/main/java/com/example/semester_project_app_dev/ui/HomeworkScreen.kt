package com.example.semester_project_app_dev.ui

import HomeworkCard
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.semester_project_app_dev.R
import com.example.semester_project_app_dev.data.Course
import com.example.semester_project_app_dev.data.HomeworkItem

@Composable
fun HomeworkScreen(
    course: Course,
    homeworks: List<HomeworkItem>,
    onBack: () -> Unit,
    onToggleDone: (HomeworkItem) -> Unit,
    onAddHomework: () -> Unit
) {
    val undoneCount = homeworks.count { !it.isDone }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Spacer(Modifier.height(60.dp))

            // Header: Folder + Counter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(135.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.folder_homework),
                    contentDescription = "Homework Folder",
                    modifier = Modifier
                        .weight(1.3f)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Homework Counter
                Box(
                    modifier = Modifier
                        .weight(0.7f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.hw_undone),
                        contentDescription = "Undone Homework",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        text = undoneCount.toString().padStart(2, '0'),
                        modifier = Modifier.offset(y = (-5).dp), // shift text upward slightly
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 48.sp,
                            color = Color(0xFFFF038D)
                        )
                    )
                }


            }

            Spacer(Modifier.height(12.dp))

            // Course Info
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = course.name.uppercase(),
                    style = MaterialTheme.typography.headlineLarge
                        .copy(fontWeight = FontWeight.Bold, fontSize = 36.sp),
                    modifier = Modifier.padding(start = 16.dp),
                    color = Color.Black
                )
                Text(
                    text = course.teacher,
                    style = MaterialTheme.typography.labelLarge
                        .copy(fontWeight = FontWeight.Light, fontSize = 20.sp),
                    modifier = Modifier.padding(start = 16.dp),
                    color = Color.Black
                )
            }

            Spacer(Modifier.height(12.dp))

            // Homework List
            homeworks.forEach { hw ->
                HomeworkCard(item = hw, onToggleDone = { onToggleDone(hw) })
            }

            Spacer(Modifier.height(100.dp))
        }

        // Back Button
        PressableImage(
            imageRes = R.drawable.back_arrow,
            contentDescription = "Back",
            width = 32.dp,
            height = 32.dp,
            onClick = onBack,
            modifier = Modifier
                .padding(top = 16.dp, start = 35.dp)
                .align(Alignment.TopStart)
                .zIndex(2f)
        )

        // Add Homework ("+" Button)
        PressableImage(
            imageRes = R.drawable.plus,
            contentDescription = "Add Homework",
            width = 32.dp,
            height = 32.dp,
            onClick = onAddHomework,
            modifier = Modifier
                .padding(top = 16.dp, end = 35.dp)
                .align(Alignment.TopEnd)
        )
    }
}
