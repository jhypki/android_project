package com.example.myapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_plans")
data class MealPlan(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,          // String for the date
    val mealType: String,      // String for the meal type
    val recipeId: Int          // Int for the recipe ID
)
