package com.example.programingdemo.chatApplication.api

data class Message(
    val token: String,  // Use this for targeting specific devices
    val notification: NotificationContent,
    val data: Map<String, String> // Additional data you want to send
)