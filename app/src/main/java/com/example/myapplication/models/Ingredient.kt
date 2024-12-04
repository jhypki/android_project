package com.example.myapplication.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val recipeId: Int,
    val name: String,
    val quantity: String, // e.g., "100 g", "1 cup"
    val unit: String, // e.g., "g", "ml", "cup"
    val category: String,
    val calories: Float = 0f, // Calories per unit
    val protein: Float = 0f, // Protein in grams per unit
    val fat: Float = 0f, // Fat in grams per unit
    val carbohydrates: Float = 0f // Carbs in grams per unit
)
