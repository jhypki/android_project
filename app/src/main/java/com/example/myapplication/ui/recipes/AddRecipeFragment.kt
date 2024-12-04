package com.example.myapplication.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddRecipeBinding
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.models.Ingredient
import com.example.myapplication.models.Recipe
import kotlinx.coroutines.launch

class AddRecipeFragment : Fragment() {

    private var _binding: FragmentAddRecipeBinding? = null
    private val binding get() = _binding!!

    private val recipeDao by lazy {
        AppDatabase.getDatabase(requireContext()).recipeDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitButton.setOnClickListener {
            saveRecipe()
        }

        binding.addIngredientButton.setOnClickListener {
            addIngredientRow()
        }

    }

    private fun saveRecipe() {
        val recipeName = binding.recipeName.text.toString()

        if (recipeName.isNotEmpty()) {
            lifecycleScope.launch {
                val recipeId = recipeDao.insertRecipe(Recipe(name = recipeName)).toInt()

                val ingredients = mutableListOf<Ingredient>()
                for (i in 0 until binding.ingredientsContainer.childCount) {
                    val ingredientRow = binding.ingredientsContainer.getChildAt(i) as ViewGroup
                    val ingredientName = ingredientRow.findViewById<EditText>(R.id.ingredientName).text.toString()
                    val quantityStr = ingredientRow.findViewById<EditText>(R.id.ingredientQuantity).text.toString()
                    val unit = ingredientRow.findViewById<Spinner>(R.id.unitSpinner).selectedItem.toString()
                    val category = ingredientRow.findViewById<Spinner>(R.id.categorySpinner).selectedItem.toString()
                    val proteinStr = ingredientRow.findViewById<EditText>(R.id.ingredientProtein).text.toString()
                    val fatStr = ingredientRow.findViewById<EditText>(R.id.ingredientFat).text.toString()
                    val carbsStr = ingredientRow.findViewById<EditText>(R.id.ingredientCarbohydrates).text.toString()

                    if (ingredientName.isNotEmpty() && quantityStr.isNotEmpty()) {
                        val quantity = quantityStr.toFloatOrNull() ?: 0f
                        val protein = proteinStr.toFloatOrNull() ?: 0f
                        val fat = fatStr.toFloatOrNull() ?: 0f
                        val carbohydrates = carbsStr.toFloatOrNull() ?: 0f
                        val calories = (protein * 4) + (fat * 9) + (carbohydrates * 4)

                        ingredients.add(
                            Ingredient(
                                recipeId = recipeId,
                                name = ingredientName,
                                quantity = quantityStr,
                                unit = unit,
                                category = category,
                                calories = calories,
                                protein = protein,
                                fat = fat,
                                carbohydrates = carbohydrates
                            )
                        )
                    }
                }

                ingredients.forEach { ingredient ->
                    recipeDao.insertIngredient(ingredient)
                }

                // Navigate back to RecipesFragment after saving
                findNavController().navigate(R.id.navigation_recipes)
            }
        }
    }

    private fun addIngredientRow() {
        val ingredientRow = LayoutInflater.from(context).inflate(R.layout.ingredient_row, binding.ingredientsContainer, false) as ViewGroup

        // Initialize the unit spinner
        val unitSpinner = ingredientRow.findViewById<Spinner>(R.id.unitSpinner)
        val unitsArray = resources.getStringArray(R.array.units_array)
        val unitAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, unitsArray)
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        unitSpinner.adapter = unitAdapter

        // Initialize the category spinner
        val categorySpinner = ingredientRow.findViewById<Spinner>(R.id.categorySpinner)
        val categoriesArray = resources.getStringArray(R.array.ingredient_categories)
        val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoriesArray)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        // Set up delete button functionality
        val deleteButton = ingredientRow.findViewById<ImageButton>(R.id.deleteIngredientButton)
        deleteButton.setOnClickListener {
            binding.ingredientsContainer.removeView(ingredientRow)
        }

        // Add the ingredientRow to ingredientsContainer
        binding.ingredientsContainer.addView(ingredientRow)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
