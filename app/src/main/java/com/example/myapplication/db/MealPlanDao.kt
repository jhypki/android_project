package com.example.myapplication.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.models.MealPlan
import com.example.myapplication.models.MealWithRecipe
import kotlinx.coroutines.flow.Flow

@Dao
interface MealPlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealPlan(mealPlan: MealPlan)

    @Delete
    suspend fun deleteMealPlan(mealPlan: MealPlan)

    @Query("SELECT * FROM meal_plans WHERE date = :date")
    fun getMealsForDate(date: String): Flow<List<MealPlan>>

    @Query("SELECT * FROM meal_plans")
    fun getAllMealPlans(): Flow<List<MealPlan>>

    @Query("""
        SELECT meal_plans.date, meal_plans.mealType, meal_plans.recipeId, recipes.name AS recipeName 
        FROM meal_plans 
        INNER JOIN recipes ON meal_plans.recipeId = recipes.id 
        WHERE meal_plans.date = :date
    """)
    fun getMealsWithRecipeNamesForDate(date: String): Flow<List<MealWithRecipe>>

}