package com.example.programingdemo.firebase


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programingdemo.utlis.Const.USER_DATA
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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

    fun updateUserProfile(userProfileInfo: UserProfileInfo) {
        viewModelScope.launch {
            val success = repository.updateUserProfile(userProfileInfo)
            if (success) fetchData()
        }
    }

    fun addUserProfile(userProfileInfo: UserProfileInfo) {
        val realTimeDatabase: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child(USER_DATA)

        viewModelScope.launch {
            val newProfileRef = realTimeDatabase.push()
            val newKey = newProfileRef.key

            if (newKey != null) {
                val success = repository.addUserProfile(userProfileInfo)
                if (success) fetchData()
            }
        }
    }

}
