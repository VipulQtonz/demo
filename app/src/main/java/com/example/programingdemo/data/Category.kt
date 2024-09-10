package com.example.programingdemo.data

data class Category(
    val categoryName: String,
    val items: List<ItemOne>,
    var isExpanded: Boolean = false
)