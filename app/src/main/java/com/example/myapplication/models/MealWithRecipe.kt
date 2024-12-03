package com.example.myapplication.models

data class MealWithRecipe(
    val date: String,
    val mealType: String,
    val recipeId: Int,
    val recipeName: String
)
