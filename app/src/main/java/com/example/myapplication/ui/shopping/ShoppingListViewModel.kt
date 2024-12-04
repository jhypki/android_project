package com.example.myapplication.ui.shopping

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.db.MealPlanDao
import com.example.myapplication.db.RecipeDao
import com.example.myapplication.db.ShoppingListDao
import com.example.myapplication.models.ShoppingListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class ShoppingListViewModel(
    private val mealPlanDao: MealPlanDao,
    private val recipeDao: RecipeDao,
    private val shoppingListDao: ShoppingListDao
) : ViewModel() {

    fun generateShoppingListForDates(selectedDates: List<String>): LiveData<Map<String, List<ShoppingListItem>>> {
        return liveData {
            val ingredientMap = mutableMapOf<String, MutableList<ShoppingListItem>>()
            for (date in selectedDates) {
                val meals = mealPlanDao.getMealsForDate(date).firstOrNull() ?: emptyList()

                for (meal in meals) {
                    val ingredients = recipeDao.getIngredientsForRecipe(meal.recipeId).firstOrNull() ?: emptyList()
                    for (ingredient in ingredients) {
                        val category = ingredient.category
                        ingredientMap.putIfAbsent(category, mutableListOf())
                        ingredientMap[category]?.add(
                            ShoppingListItem(
                                name = ingredient.name,
                                quantity = ingredient.quantity,
                                unit = ingredient.unit,
                                category = category
                            )
                        )
                    }
                }
            }
            emit(ingredientMap)
        }
    }



    fun saveShoppingList(items: List<ShoppingListItem>) {
        viewModelScope.launch {
            items.forEach { shoppingListDao.insertItem(it) }
        }
    }

    fun updateShoppingItem(item: ShoppingListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            shoppingListDao.update(item)
        }
    }

    fun getShoppingList(): LiveData<List<ShoppingListItem>> {
        return shoppingListDao.getAllShoppingListItems().asLiveData()
    }

    fun addIngredient(item: ShoppingListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            shoppingListDao.insert(item)
        }
    }

    fun removeShoppingItem(item: ShoppingListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            shoppingListDao.delete(item)
        }
    }



}
