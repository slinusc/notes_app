// File: app/src/main/java/com/example/semester_project_app_dev/ui/CourseCard.kt

package com.example.semester_project_app_dev.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semester_project_app_dev.R
import com.example.semester_project_app_dev.data.Course

/**
 * A single “course card” that can represent:
 *  • An existing course (course != null), or
 *  • An “Add New Course” placeholder (course == null).
 *
 * The `index` parameter selects which drawable to use.
 * The bottom accent strip has been removed per request.
 */

@Composable
fun CourseCard(
    course: Course?,
    index: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    textScale: Float = 1f  // default: normal size
) {
    val folderDrawables: List<Int> = listOf(
        R.drawable.folder1,
        R.drawable.folder2,
        R.drawable.folder3,
        R.drawable.folder4,
        R.drawable.folder5,
        R.drawable.folder6
    )

    val addDrawables: List<Int> = listOf(
        R.drawable.folder1_add,
        R.drawable.folder2_add,
        R.drawable.folder3_add,
        R.drawable.folder4_add,
        R.drawable.folder5_add,
        R.drawable.folder6_add
    )

    val backgroundRes: Int = if (course == null) {
        addDrawables[index % addDrawables.size]
    } else {
        folderDrawables[index % folderDrawables.size]
    }

    Box(
        modifier = modifier
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(backgroundRes),
            contentDescription = course?.name ?: "Add Course",
            modifier = Modifier.fillMaxSize()
        )

        if (course != null) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = (12 * textScale * textScale * textScale).dp, top = (28 * textScale).dp)
            ) {
                Text(
                    text = course.name,
                    fontSize = (28 * textScale).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height((8 * textScale).dp))
                Text(
                    text = course.teacher,
                    fontSize = (12 * textScale).sp,
                    color = Color.Black
                )
                Text(
                    text = "${course.day} ${course.time}",
                    fontSize = (10 * textScale).sp,
                    color = Color.Black
                )
            }

            if (course.isUrgent) {
                Image(
                    painter = painterResource(R.drawable.urgent),
                    contentDescription = "Urgent",
                    modifier = Modifier
                        .size((36 * textScale).dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-15).dp, y = 0.dp)
                )
            }
        }
    }
}
