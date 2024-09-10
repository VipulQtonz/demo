package com.example.programingdemo.data

import java.io.Serializable

class ChatItem(
    val profileImageResId: Int,
    val displayName: String,
    val message: String,
    val messageTime: String,
    val messageCount: Int,
    val messageReadRecepe: Int
) : Serializable
