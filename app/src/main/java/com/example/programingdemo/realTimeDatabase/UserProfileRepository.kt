package com.example.programingdemo.realTimeDatabase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class UserProfileRepository {
    private val realTimeDatabase: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("Notes")

    suspend fun fetchUserProfiles(): List<UserProfileInfo> {
        return try {
            val snapshot = realTimeDatabase.get().await()
            val dataList = mutableListOf<UserProfileInfo>()
            for (dataSnapshot in snapshot.children) {
                val id = dataSnapshot.key ?: continue
                val nameSnapshot = dataSnapshot.child("name").getValue(Name::class.java) ?: Name()
                val addressSnapshot =
                    dataSnapshot.child("address").getValue(Address::class.java) ?: Address()
                val email = dataSnapshot.child("email").getValue(String::class.java) ?: ""
                val dateOfBirth =
                    dataSnapshot.child("dateOfBirth").getValue(String::class.java) ?: ""

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

    suspend fun updateUserProfile(id: String, newName: String): Boolean {
        return try {
            val updates = mapOf("name/firstName" to newName)
            realTimeDatabase.child(id).updateChildren(updates).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun addUserProfile(userProfile: UserProfileInfo): Boolean {
        return try {
            val newNoteRef = realTimeDatabase.push()
            newNoteRef.setValue(userProfile).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
