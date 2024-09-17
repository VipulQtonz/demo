package com.example.programingdemo.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.programingdemo.utlis.Const.USERS

@Entity(tableName = USERS)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String
)