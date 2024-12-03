package com.example.myapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InventoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val quantity: String,
    val unit: String
)
