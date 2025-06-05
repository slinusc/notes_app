package com.example.semester_project_app_dev.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.semester_project_app_dev.R

// Define the Kanit font family
val Kanit = FontFamily(
    Font(R.font.kanit_light, FontWeight.Light),
    Font(R.font.kanit_regular, FontWeight.Normal),
    Font(R.font.kanit_medium, FontWeight.Medium)
)

// Unified Typography definition
val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Kanit,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Kanit,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Kanit,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Kanit,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp
    )
)
