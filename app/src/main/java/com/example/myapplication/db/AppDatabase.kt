package com.example.myapplication.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.myapplication.models.Ingredient
import com.example.myapplication.models.MealPlan
import com.example.myapplication.models.Recipe
import com.example.myapplication.models.ShoppingListItem

@Database(entities = [Recipe::class, Ingredient::class, MealPlan::class, ShoppingListItem::class], version = 4) // Update version
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
    abstract fun mealPlanDao(): MealPlanDao
    abstract fun shoppingListDao(): ShoppingListDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "meal_planner_database"
                )
                    .fallbackToDestructiveMigration() // Clear database if schema changes
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}