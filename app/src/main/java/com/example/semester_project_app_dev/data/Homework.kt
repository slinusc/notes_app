package com.example.semester_project_app_dev.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "homework")
data class HomeworkItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val courseId: Int, // ← foreign key to Course
    val title: String,
    val details: String,
    val dueDay: String,
    val isDone: Boolean = false
)
