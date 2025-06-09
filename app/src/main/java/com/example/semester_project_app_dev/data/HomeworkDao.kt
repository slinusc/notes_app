package com.example.semester_project_app_dev.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeworkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomework(item: HomeworkItem)

    @Delete
    suspend fun deleteHomework(item: HomeworkItem)

    @Update
    suspend fun updateHomework(item: HomeworkItem)

    @Query("SELECT * FROM homework WHERE courseId = :courseId ORDER BY id ASC")
    fun getHomeworkForCourse(courseId: Int): Flow<List<HomeworkItem>>
}
