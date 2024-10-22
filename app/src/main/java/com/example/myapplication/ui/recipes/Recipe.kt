package com.example.myapplication.ui.recipes

data class Recipe(
    val name: String,
    val ingredients: List<Ingredient>
)

data class Ingredient(
    val name: String,
    val quantity: String,
    val unit: String
)
