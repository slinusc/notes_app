package com.example.semester_project_app_dev.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.semester_project_app_dev.data.AppDatabase
import com.example.semester_project_app_dev.data.User
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getInstance(application).userDao()

    fun registerUser(name: String, surname: String, password: String) {
        viewModelScope.launch {
            val user = User(name = name, surname = surname, password = password)
            userDao.insert(user)
        }
    }

    fun loginUser(
        name: String,
        surname: String,
        password: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val user = userDao.login(name, surname, password)
            onResult(user != null)
        }
    }
}

