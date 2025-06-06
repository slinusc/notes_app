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
    onSettings: () -> Unit
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
    var current by remember { mutableIntStateOf(0) }
    var pageText by remember { mutableStateOf(pages.getOrNull(0) ?: "") }

    /* ── UI helpers ──────────────────────────────────────────────────── */
    val scroll    = rememberScrollState()
    val backIcon  = painterResource(R.drawable.img_home_btn)
    val menuIcon  = painterResource(R.drawable.menu_icon)



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
                        textScale = 1.25f,
                        modifier = Modifier.matchParentSize()   // card fills the Box
                    )

                    // Urgent toggle (lavender when off, red when on)
                    Image(
                        painter = painterResource(
                            if (isUrgent) R.drawable.urgent_on else R.drawable.urgent_off
                        ),
                        contentDescription = "Toggle urgent",
                        modifier = Modifier
                            .size(50.dp)                        // adjust to taste
                            .align(Alignment.TopEnd)            // sits over card corner
                            .offset(x = (-10).dp, y = (-3).dp)      // slight inset
                            .clickable { isUrgent = !isUrgent }
                            .zIndex(3f)                         // stays above the card
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
                    Image(
                        painterResource(R.drawable.folder_homework),
                        "Homework",
                        Modifier
                            .size(90.dp)
                            .clickable { /* TODO */ }
                    )
                    Image(
                        painterResource(R.drawable.folder_meeting),
                        "Meeting",
                        Modifier
                            .size(90.dp)
                            .clickable { /* TODO */ }
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
                )

                /* 🗑 Delete page */
                PressableImage(
                    imageRes = R.drawable.ic_delete,
                    width = 142.dp,
                    contentDescription = "Delete page",
                    onClick = {
                        // remove the current page
                        pages.removeAt(current)

                        // guarantee at least one page
                        if (pages.isEmpty()) pages.add("")

                        // adjust index if we just removed the last page
                        if (current >= pages.size) current = pages.lastIndex

                        // update editor
                        pageText = pages[current]
                    },
                )
            }

            Spacer(Modifier.height(30.dp))

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
                    .height(450.dp),
                maxLines = Int.MAX_VALUE
            )

            Spacer(Modifier.height(32.dp))
        }

        /* 2️⃣ Back button */
        Image(
            backIcon, "Back",
            Modifier
                .padding(16.dp)
                .size(32.dp)
                .align(Alignment.TopStart)
                .zIndex(2f)
                .clickable {
                    pages[current] = pageText
                    onCourseChange(
                        course.copy(
                            name = name,
                            teacher = teacher,
                            day = day,
                            time = time,
                            isUrgent = isUrgent,
                            pages = pages.toList()
                        )
                    )
                    onBack()
                }
        )

        /* 3️⃣ Menu (hamburger) button */
        Image(
            menuIcon, "Menu",
            Modifier
                .padding(16.dp)
                .size(32.dp)
                .align(Alignment.TopEnd)
                // keep below SlideInMenu when it is open
                .zIndex(if (menuState.isOpen) 0f else 2f)
                .clickable { menuState.toggle() }
        )

        /* 4️⃣ Slide-in panel */
        SlideInMenu(
            menuState = menuState,
            onSettingsClick = { onSettings() }
        )

    }
}
