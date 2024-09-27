package com.example.programingdemo.data

data class UserMessage(
    val userId: String = "",
    val userName: String = "",
    val message: String = "",
    var isCurrentUser: Boolean? = false
)
