package com.example.myapplication.db

import androidx.room.*
import com.example.myapplication.models.Ingredient
import com.example.myapplication.models.Recipe
import com.example.myapplication.models.RecipeWithIngredients
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: Ingredient)

    @Transaction
    suspend fun insertRecipeWithIngredients(recipe: Recipe, ingredients: List<Ingredient>) {
        val recipeId = insertRecipe(recipe)
        ingredients.forEach { ingredient ->
            insertIngredient(ingredient.copy(recipeId = recipeId.toInt()))
        }
    }

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM ingredients WHERE recipeId = :recipeId")
    fun getIngredientsForRecipe(recipeId: Int): Flow<List<Ingredient>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    fun getRecipeWithIngredients(recipeId: Int): Flow<RecipeWithIngredients>

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    fun getRecipeById(recipeId: Int): Flow<Recipe?>

    @Query("SELECT name FROM recipes WHERE id = :recipeId")
    suspend fun getRecipeNameById(recipeId: Int): String
}
