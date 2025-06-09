package com.example.semester_project_app_dev.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.semester_project_app_dev.R
import com.example.semester_project_app_dev.data.Course

@Composable
fun HomeworkScreen(
    course: Course,
    onBack: () -> Unit,
) {

    var isHomework3Done by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Spacer(Modifier.height(60.dp))

            // Header Row: Homework Folder wider than Undone HW
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

                Image(
                    painter = painterResource(
                        if (isHomework3Done) R.drawable.hw_undone3 else R.drawable.undone_hw
                    ),
                    contentDescription = "Undone Homework",
                    modifier = Modifier
                        .weight(0.7f)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(Modifier.height(12.dp))

            // Course Info
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = course.name.uppercase(),
                    style = MaterialTheme.typography.headlineLarge
                        .copy(fontWeight = FontWeight.Bold, fontSize = 36.sp),
                    modifier = Modifier.padding(start = 16.dp)
                )
                Text(
                    text = course.teacher,
                    style = MaterialTheme.typography.labelLarge
                        .copy(fontWeight = FontWeight.Light, fontSize = 20.sp),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            // Homework Card Placeholders
            Image(
                painter = painterResource(R.drawable.hw1),
                contentDescription = "Homework 1",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(vertical = 6.dp),
                contentScale = ContentScale.FillWidth
            )

            Image(
                painter = painterResource(R.drawable.hw2),
                contentDescription = "Homework 2",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(vertical = 6.dp),
                contentScale = ContentScale.FillWidth
            )

            val interactionSource = remember { MutableInteractionSource() }

            Image(
                painter = painterResource(
                    if (isHomework3Done) R.drawable.hw3_unchecked else R.drawable.hw_done
                ),
                contentDescription = "Homework 3",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(vertical = 6.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null // disables ripple
                    ) { isHomework3Done = !isHomework3Done },
                contentScale = ContentScale.FillWidth
            )


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

        PressableImage(
            imageRes = R.drawable.plus,
            contentDescription = "Menu",
            width = 32.dp,
            height = 32.dp,
            onClick = { /* TODO: handle menu */ },
            modifier = Modifier
                .padding(top = 16.dp, end = 35.dp)
                .align(Alignment.TopEnd)
        )

    }
}
