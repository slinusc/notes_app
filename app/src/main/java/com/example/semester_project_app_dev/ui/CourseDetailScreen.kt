package com.example.semester_project_app_dev.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.semester_project_app_dev.R
import com.example.semester_project_app_dev.data.Course
import com.example.semester_project_app_dev.state.MenuState
import com.example.semester_project_app_dev.ui.components.SlideInMenu

@Composable
fun CourseDetailScreen(
    course: Course,
    index: Int,
    menuState: MenuState,                 // ← NEW  (must be supplied)
    onCourseChange: (Course) -> Unit,
    onBack: () -> Unit,
    onHomeworkClick: (Course) -> Unit,
    onMeetingClick: (Course) -> Unit,
    onSettings: (Course) -> Unit

    )
 {
    /* ── Editable fields ─────────────────────────────────────────────── */
    var name     by remember { mutableStateOf(course.name) }
    var teacher  by remember { mutableStateOf(course.teacher) }
    var day      by remember { mutableStateOf(course.day) }
    var time     by remember { mutableStateOf(course.time) }
    var isUrgent by remember { mutableStateOf(course.isUrgent) }

    /* ── Pages state ─────────────────────────────────────────────────── */
    val pages = remember {
        mutableStateListOf<String>().apply { addAll(course.pages) }
    }.also {
        if (it.isEmpty()) it.add("")          // ← always keep ≥ 1 page
    }
     var current by remember { mutableIntStateOf(course.currentPageIndex) }
     var pageText by remember { mutableStateOf(pages.getOrNull(0) ?: "") }

    /* ── UI helpers ──────────────────────────────────────────────────── */
    val scroll    = rememberScrollState()

     /* ── Delete page dialog ──────────────────────────────────────────── */
     var showPageDeleteDialog by remember { mutableStateOf(false) }

     /* ────────────────────────────────────────────────────────────────── */

    Box(Modifier.fillMaxSize()) {

        /* 1️⃣ Scrollable content (body) */
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Spacer(Modifier.height(60.dp))

            /* CourseCard + folders … */
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                verticalAlignment = Alignment.Top
            ) {
                /* inside the big Row at the top — replace the previous CourseCard line */

                Box(
                    modifier = Modifier
                        .weight(0.65f)
                        .fillMaxHeight()
                ) {
                    // Main card
                    CourseCard(
                        course = course.copy(
                            name = name,
                            teacher = teacher,
                            day = day,
                            time = time,
                            isUrgent = isUrgent,
                        ),
                        index = index,
                        onClick = {},
                        textScale = 1.3f,
                        modifier = Modifier.matchParentSize()   // card fills the Box
                    )

                    // Urgent toggle (lavender when off, red when on)
                    PressableImage(
                        imageRes = if (isUrgent) R.drawable.urgent_on else R.drawable.urgent_off,
                        contentDescription = "Toggle urgent",
                        width = 50.dp,
                        height = 50.dp,
                        pressEffect = PressEffect.Light,
                        onClick = { isUrgent = !isUrgent },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-15).dp, y = (-3).dp)
                            .zIndex(3f)
                    )

                }

                Spacer(Modifier.width(8.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(0.35f)
                        .fillMaxHeight()
                ) {
                    PressableImage(
                        R.drawable.folder_homework,
                        contentDescription = "Homework",
                        onClick = {
                            pages[current] = pageText
                            onHomeworkClick(
                                course.copy(
                                    name = name,
                                    teacher = teacher,
                                    day = day,
                                    time = time,
                                    isUrgent = isUrgent,
                                    pages = pages.toList()
                                )
                            )
                        },
                        modifier = Modifier.size(90.dp),
                        pressEffect = PressEffect.Light
                    )

                    PressableImage(
                        R.drawable.folder_meeting,
                        contentDescription = "Exam",
                        onClick = {
                            pages[current] = pageText
                            onMeetingClick(
                                course.copy(
                                    name = name,
                                    teacher = teacher,
                                    day = day,
                                    time = time,
                                    isUrgent = isUrgent,
                                    pages = pages.toList()
                                )
                            )
                        },
                        modifier = Modifier.size(93.dp),
                        pressEffect = PressEffect.Light
                    )

                }
            }

            Spacer(Modifier.height(30.dp))

            /* ── New page / Delete page buttons ─────────────────────── */
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp)
            ) {
                /* + New page */
                PressableImage(
                    imageRes = R.drawable.ic_add,
                    contentDescription = "New page",
                    onClick = {
                        pages[current] = pageText          // save current text
                        pages.add("")                      // blank page
                        current = pages.lastIndex
                        pageText = ""
                    },

                    height = 32.dp,
                )

                Spacer(Modifier.width(20.dp))

                /* 🗑 Delete page */
                PressableImage(
                    imageRes = R.drawable.ic_delete,
                    //width = 142.dp,
                    contentDescription = "Delete page",
                    onClick = {
                        showPageDeleteDialog = true
                    },
                    height = 32.dp,
                )
            }

            if (showPageDeleteDialog) {
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { showPageDeleteDialog = false },
                    confirmButton = {
                        androidx.compose.material3.TextButton(onClick = {
                            showPageDeleteDialog = false

                            // perform deletion
                            pages.removeAt(current)
                            if (pages.isEmpty()) pages.add("")
                            if (current >= pages.size) current = pages.lastIndex
                            pageText = pages[current]

                        }) {
                            androidx.compose.material3.Text("Delete")
                        }
                    },
                    dismissButton = {
                        androidx.compose.material3.TextButton(onClick = {
                            showPageDeleteDialog = false
                        }) {
                            androidx.compose.material3.Text("Cancel")
                        }
                    },
                    title = { androidx.compose.material3.Text("Delete Page") },
                    text = { androidx.compose.material3.Text("Are you sure you want to delete this page? You won't be able to recover it.") }
                )
            }


            Spacer(Modifier.height(20.dp))

            /* ── Paging controls ────────────────────────────────────── */
            Row(
                Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                TextButton(
                    enabled = current > 0,
                    onClick = {
                        pages[current] = pageText
                        current--
                        pageText = pages[current]
                    }
                ) { Text("← Prev") }

                Text("Page ${current + 1}", style = MaterialTheme.typography.titleMedium)

                TextButton(
                    enabled = current < pages.size - 1,
                    onClick = {
                        pages[current] = pageText
                        current++
                        pageText = pages[current]
                    }
                ) { Text("Next →") }
            }

            Spacer(Modifier.height(8.dp))

            /* ── Notes editor ───────────────────────────────────────── */
            OutlinedTextField(
                value = pageText,
                onValueChange = { pageText = it },
                label = { Text("Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(460.dp),
                maxLines = Int.MAX_VALUE
            )

            Spacer(Modifier.height(32.dp))
        }

        PressableImage(
        imageRes = R.drawable.img_home_btn, // assuming `backIcon = painterResource(R.drawable.back_icon)`
        contentDescription = "Back",
        width = 32.dp,
        height = 32.dp,
            onClick = {
                pages[current] = pageText
                onCourseChange(
                    course.copy(
                        name = name,
                        teacher = teacher,
                        day = day,
                        time = time,
                        isUrgent = isUrgent,
                        pages = pages.toList(),
                        currentPageIndex = current
                    )
                )
                onBack() // ✅ go back after saving
            },
        modifier = Modifier
            .padding(top = 16.dp, start = 35.dp)
            .align(Alignment.TopStart)
            .zIndex(2f)
    )


        /* Settings button (top‐right) */
        PressableImage(
            imageRes = R.drawable.settings_course,
            contentDescription = "Menu",
            width = 32.dp,
            height = 32.dp,
            onClick = {
                pages[current] = pageText
                menuState.toggle()
                onSettings(
                    course.copy(
                        name = name,
                        teacher = teacher,
                        day = day,
                        time = time,
                        isUrgent = isUrgent,
                        pages = pages.toList(),
                        currentPageIndex = current
                    )
                )
            },
            modifier = Modifier
                .padding(top = 16.dp, end = 35.dp)
                .align(Alignment.TopEnd)
                .zIndex(if (menuState.isOpen) 0f else 2f)
        )



    }
}
