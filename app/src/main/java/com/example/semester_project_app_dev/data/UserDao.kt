package com.example.semester_project_app_dev.data

import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE name = :name AND surname = :surname AND password = :password LIMIT 1")
    suspend fun login(name: String, surname: String, password: String): User?
}
