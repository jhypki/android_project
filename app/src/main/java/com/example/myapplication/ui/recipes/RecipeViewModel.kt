package com.example.myapplication.ui.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecipeViewModel : ViewModel() {

    private val _recipes = MutableLiveData<MutableList<Recipe>>(mutableListOf())
    val recipes: LiveData<MutableList<Recipe>> = _recipes

    // Function to add a new recipe to the list
    fun addRecipe(recipe: Recipe) {
        _recipes.value?.add(recipe)
        _recipes.value = _recipes.value // Trigger LiveData update
    }
}
