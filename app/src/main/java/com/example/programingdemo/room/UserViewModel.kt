package com.example.programingdemo.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(private val userDao: UserDao) : ViewModel() {

    val allUsers: LiveData<List<User>> = userDao.getAllUsers()

    fun insert(user: User) = viewModelScope.launch {
        userDao.insert(user)
    }

    fun update(user: User) = viewModelScope.launch {
        userDao.update(user)
    }

    fun delete(user: User) = viewModelScope.launch {
        userDao.delete(user)
    }
}
