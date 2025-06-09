package com.example.semester_project_app_dev.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "courses")
@TypeConverters(Converters::class)
data class Course(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val teacher: String,
    val day: String,
    val time: String,
    val isUrgent: Boolean = false,
    val pages: List<String> = emptyList(),
    var currentPageIndex: Int = 0,
    val todoList: List<String> = emptyList(),
    val userId: Int
)
