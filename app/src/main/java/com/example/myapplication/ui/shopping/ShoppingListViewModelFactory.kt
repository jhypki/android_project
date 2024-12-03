package com.example.myapplication.ui.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.db.MealPlanDao
import com.example.myapplication.db.RecipeDao
import com.example.myapplication.db.ShoppingListDao

class ShoppingListViewModelFactory(
    private val mealPlanDao: MealPlanDao,
    private val recipeDao: RecipeDao,
    private val shoppingListDao: ShoppingListDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShoppingListViewModel(mealPlanDao, recipeDao, shoppingListDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
