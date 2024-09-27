package com.example.programingdemo.data

import java.io.Serializable

class UserData(
    val userId: String = "",
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    val token:String=""
):Serializable