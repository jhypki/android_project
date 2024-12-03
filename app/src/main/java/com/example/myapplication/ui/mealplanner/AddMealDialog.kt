package com.example.myapplication.ui.mealplanner

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.models.MealPlan
import com.example.myapplication.models.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddMealDialog(
    private val date: String,
    private val onMealAdded: (MealPlan) -> Unit
) : DialogFragment() {

    private lateinit var mealTypeSpinner: Spinner
    private lateinit var recipeSpinner: Spinner
    private var recipes: List<Recipe> = emptyList()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_meal, null)

        mealTypeSpinner = view.findViewById(R.id.spinnerMealType)
        recipeSpinner = view.findViewById(R.id.spinnerRecipes)

        // Meal types
        val mealTypes = listOf("Breakfast", "Lunch", "Dinner", "Snack")
        val mealTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mealTypes
        )
        mealTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mealTypeSpinner.adapter = mealTypeAdapter

        // Recipes
        lifecycleScope.launch {
            recipes = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(requireContext()).recipeDao().getAllRecipes().first()
            }
            val recipeNames = recipes.map { it.name }
            val recipeAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                recipeNames
            )
            recipeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            recipeSpinner.adapter = recipeAdapter
        }

        builder.setView(view)
            .setPositiveButton("Add") { _, _ ->
                val selectedMealType = mealTypeSpinner.selectedItem.toString()
                val selectedRecipeIndex = recipeSpinner.selectedItemPosition

                if (recipes.isNotEmpty()) {
                    val selectedRecipe = recipes[selectedRecipeIndex]
                    val mealPlan = MealPlan(
                        date = date, // Use the passed date
                        mealType = selectedMealType,
                        recipeId = selectedRecipe.id
                    )
                    onMealAdded(mealPlan)
                }
            }
            .setNegativeButton("Cancel", null)

        return builder.create()
    }
}
