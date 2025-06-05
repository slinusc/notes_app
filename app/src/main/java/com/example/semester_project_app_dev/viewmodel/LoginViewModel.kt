package com.example.semester_project_app_dev.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.semester_project_app_dev.data.AppDatabase
import com.example.semester_project_app_dev.data.User
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getInstance(application).userDao()

    fun registerUser(
        name: String,
        surname: String,
        school: String,
        year: String,
        semester: String,
        password: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val existing = userDao.authenticate(name, surname, password)
            if (existing == null) {
                val user = User(
                    name = name,
                    surname = surname,
                    school = school,
                    semester = semester,
                    password = password
                )
                userDao.insertUser(user)
                onResult(true)
            } else {
                onResult(false) // already exists
            }
        }
    }

    fun loginUser(
        name: String,
        surname: String,
        password: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val user = userDao.authenticate(name, surname, password)
            onResult(user != null)
        }
    }

    fun getAllUsers(onResult: (List<User>) -> Unit) {
        viewModelScope.launch {
            val users = userDao.getAllUsers()
            onResult(users)
        }
    }


}


