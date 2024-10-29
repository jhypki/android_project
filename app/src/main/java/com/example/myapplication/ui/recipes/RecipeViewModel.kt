package com.example.myapplication.ui.recipes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.myapplication.db.RecipeDao
import com.example.myapplication.models.Recipe
import com.example.myapplication.models.Ingredient

class RecipeViewModel(private val recipeDao: RecipeDao) : ViewModel() {

    val recipes: LiveData<List<Recipe>> = recipeDao.getAllRecipes().asLiveData()

    fun getIngredientsForRecipe(recipeId: Int): LiveData<List<Ingredient>> {
        return recipeDao.getIngredientsForRecipe(recipeId).asLiveData()
    }

    fun getRecipeById(recipeId: Int): LiveData<Recipe?> {
        return recipeDao.getRecipeById(recipeId).asLiveData()
    }
}
