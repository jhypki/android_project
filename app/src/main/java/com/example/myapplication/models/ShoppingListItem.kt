package com.example.myapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list_items")
data class ShoppingListItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val quantity: String,
    val unit: String,
    val category: String,
    var isPurchased: Boolean = false
)
