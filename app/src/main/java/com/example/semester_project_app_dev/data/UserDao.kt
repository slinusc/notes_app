package com.example.semester_project_app_dev.data

import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE name = :name AND surname = :surname AND password = :password LIMIT 1")
    suspend fun authenticate(name: String, surname: String, password: String): User?
}
