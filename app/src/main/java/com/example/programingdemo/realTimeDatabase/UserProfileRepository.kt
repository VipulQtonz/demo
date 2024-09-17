package com.example.programingdemo.realTimeDatabase

import com.example.programingdemo.MyApp.Companion.firebaseDatabase
import com.example.programingdemo.utlis.Const.ADDRESS_NEW
import com.example.programingdemo.utlis.Const.DATE_OF_BIRTH
import com.example.programingdemo.utlis.Const.EMAIL_NEW
import com.example.programingdemo.utlis.Const.NAME
import com.example.programingdemo.utlis.Const.USER_DATA
import kotlinx.coroutines.tasks.await

class UserProfileRepository {
    private val realTimeDatabase =
        firebaseDatabase.reference.child(USER_DATA)

    suspend fun fetchUserProfiles(): List<UserProfileInfo> {
        return try {
            val snapshot = realTimeDatabase.get().await()
            val dataList = mutableListOf<UserProfileInfo>()
            for (dataSnapshot in snapshot.children) {
                val id = dataSnapshot.key ?: continue
                val nameSnapshot = dataSnapshot.child(NAME).getValue(Name::class.java) ?: Name()
                val addressSnapshot =
                    dataSnapshot.child(ADDRESS_NEW).getValue(Address::class.java) ?: Address()
                val email = dataSnapshot.child(EMAIL_NEW).getValue(String::class.java) ?: ""
                val dateOfBirth =
                    dataSnapshot.child(DATE_OF_BIRTH).getValue(String::class.java) ?: ""
                if (email.isNotEmpty() && dateOfBirth.isNotEmpty()) {
                    dataList.add(
                        UserProfileInfo(
                            id,
                            nameSnapshot,
                            email,
                            addressSnapshot,
                            dateOfBirth
                        )
                    )
                }
            }
            dataList
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun deleteUserProfile(id: String): Boolean {
        return try {
            realTimeDatabase.child(id).removeValue().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateUserProfile(userProfile: UserProfileInfo): Boolean {
        return try {
            realTimeDatabase.child(userProfile.id).setValue(userProfile).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun addUserProfile(userProfile: UserProfileInfo): Boolean {
        return try {
            val newNoteRef = realTimeDatabase.push()
            newNoteRef.setValue(userProfile).await()
            newNoteRef.key
            true
        } catch (e: Exception) {
            false
        }
    }
}
