// File: app/src/main/java/com/example/semester_project_app_dev/data/UserDao.kt

package com.example.semester_project_app_dev.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE name = :name AND surname = :surname AND password = :password LIMIT 1")
    suspend fun authenticate(name: String, surname: String, password: String): User?

    // Adds a one-time fetch of all users as a List
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    // Existing Flow-based getter
    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getAllUsersFlow(): Flow<List<User>>

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUser(id: Int)

    @Query("SELECT * FROM users WHERE name = :name AND surname = :surname LIMIT 1")
    suspend fun findUser(name: String, surname: String): User?

}
