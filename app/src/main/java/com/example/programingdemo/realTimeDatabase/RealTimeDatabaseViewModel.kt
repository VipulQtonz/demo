package com.example.programingdemo.realTimeDatabase


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RealTimeDatabaseViewModel : ViewModel() {
    private val repository = UserProfileRepository()
    private val _userProfileInfoList = MutableLiveData<List<UserProfileInfo>>()
    val userProfileInfoList: LiveData<List<UserProfileInfo>> get() = _userProfileInfoList

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            val dataList = repository.fetchUserProfiles()
            _userProfileInfoList.postValue(dataList)
        }
    }

    fun deleteUserProfile(id: String) {
        viewModelScope.launch {
            val success = repository.deleteUserProfile(id)
            if (success) fetchData()
        }
    }

    fun updateUserProfile(id: String, newName: String) {
        viewModelScope.launch {
            val success = repository.updateUserProfile(id, newName)
            if (success) fetchData()
        }
    }

    fun addUserProfile(name: String) {
        viewModelScope.launch {
            val newNote = UserProfileInfo(
                id = "",
                name = Name(name, "jadav"),
                email = "vipuljadav542@gmail.com",
                address = Address("Rajkot", "Gujarat", 360023),
                dateOfBirth = "15-01-2003"
            )
            val success = repository.addUserProfile(newNote)
            if (success) fetchData()
        }
    }
}
