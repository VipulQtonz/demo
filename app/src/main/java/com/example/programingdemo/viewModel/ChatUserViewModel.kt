package com.example.programingdemo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.programingdemo.MyApp.Companion.firebaseAuth
import com.example.programingdemo.data.UserData
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class ChatUserViewModel : ViewModel() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val _userListLiveData = MutableLiveData<List<UserData>>()
    val userListLiveData: LiveData<List<UserData>> = _userListLiveData

    fun fetchAllUsers() {
        val currentUserId = firebaseAuth.currentUser?.uid
        db.collection("ChatUser").addSnapshotListener { snapshots, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            val users = ArrayList<UserData>()
            for (document in snapshots!!) {
                val user = document.toObject(UserData::class.java)
                if (document.id != currentUserId) {
                    users.add(user)
                }
            }
            _userListLiveData.postValue(users)
        }
    }
}
