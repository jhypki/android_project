package com.example.myapplication.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddRecipeBinding
import kotlinx.coroutines.launch
import com.example.myapplication.db.RecipeDatabase
import com.example.myapplication.models.Recipe
import com.example.myapplication.models.Ingredient

class AddRecipeFragment : Fragment() {

    private var _binding: FragmentAddRecipeBinding? = null
    private val binding get() = _binding!!

    private val recipeDao by lazy {
        RecipeDatabase.getDatabase(requireContext()).recipeDao()
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
                    val quantity = ingredientRow.findViewById<EditText>(R.id.ingredientQuantity).text.toString()
                    val unit = ingredientRow.findViewById<Spinner>(R.id.unitSpinner).selectedItem.toString()

                    if (ingredientName.isNotEmpty() && quantity.isNotEmpty()) {
                        ingredients.add(Ingredient(recipeId = recipeId, name = ingredientName, quantity = quantity, unit = unit))
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

        // Initialize the Spinner with units array
        val unitSpinner = ingredientRow.findViewById<Spinner>(R.id.unitSpinner)
        val unitsArray = resources.getStringArray(R.array.units_array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, unitsArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        unitSpinner.adapter = adapter

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

