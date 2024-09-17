package com.example.programingdemo.gallery

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.programingdemo.utlis.Const.RECENT_PHOTOS

@Entity(tableName = RECENT_PHOTOS)
data class ImagePath(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val path: String
)
