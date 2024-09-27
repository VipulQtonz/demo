package com.example.programingdemo.chatApplication.api

data class Message(
    val token: String,
    val notification: NotificationContent,
    val data: Map<String, String>
)