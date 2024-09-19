package com.example.programingdemo.realTimeDatabase

data class UserProfileInfo(
    val id: String = "",
    val name: Name = Name(),
    val email: String = "",
    val address: Address = Address(),
    val dateOfBirth: String = ""
)
