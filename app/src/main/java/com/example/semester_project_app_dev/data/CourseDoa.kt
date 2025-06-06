// File: app/src/main/java/com/example/semester_project_app_dev/data/CourseDao.kt

package com.example.semester_project_app_dev.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course)

    @Delete
    suspend fun deleteCourse(course: Course)

    @Update
    suspend fun updateCourse(course: Course)

    @Query("SELECT * FROM courses WHERE userId = :userId ORDER BY id ASC")
    fun getAllCoursesFlowForUser(userId: Int): Flow<List<Course>>

    // A helper to see if the table is empty (used for seeding)
    @Query("SELECT COUNT(*) FROM courses")
    suspend fun countCourses(): Int

    // A helper to delete any completely blank rows (if you ever need it)
    @Query("DELETE FROM courses WHERE name = '' OR name IS NULL")
    suspend fun deleteEmptyNameRows()

    @Query("SELECT * FROM courses WHERE id = :id")
    suspend fun getCourseById(id: Int): Course?
}
