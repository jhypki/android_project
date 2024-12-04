package com.example.myapplication.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.databinding.FragmentRecipeDetailBinding
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.models.Ingredient

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private val recipeViewModel: RecipeViewModel by activityViewModels {
        val recipeDao = AppDatabase.getDatabase(requireContext()).recipeDao()
        RecipeViewModelFactory(recipeDao)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeId = arguments?.getInt("recipeId") ?: return

        // Observe the recipe name
        recipeViewModel.getRecipeById(recipeId).observe(viewLifecycleOwner) { recipe ->
            binding.recipeName.text = recipe?.name ?: "Recipe not found"
        }

        // Observe the ingredients
        recipeViewModel.getIngredientsForRecipe(recipeId).observe(viewLifecycleOwner) { ingredients ->
            displayIngredients(ingredients)
            calculateAndDisplayNutritionalValues(ingredients)
        }
    }

    private fun displayIngredients(ingredients: List<Ingredient>) {
        val container = binding.ingredientsContainer
        container.removeAllViews() // Clear any existing views

        for (ingredient in ingredients) {
            val textView = TextView(requireContext()).apply {
                text = "${ingredient.name}: ${ingredient.quantity} ${ingredient.unit}"
                textSize = 16f
                setPadding(0, 8, 0, 8)
            }
            container.addView(textView)
        }
    }

    private fun calculateAndDisplayNutritionalValues(ingredients: List<Ingredient>) {
        var totalProtein = 0f
        var totalFat = 0f
        var totalCarbohydrates = 0f

        for (ingredient in ingredients) {
            totalProtein += ingredient.protein
            totalFat += ingredient.fat
            totalCarbohydrates += ingredient.carbohydrates
        }

        val totalCalories = (totalProtein * 4) + (totalFat * 9) + (totalCarbohydrates * 4)

        binding.totalCalories.text = "Calories: ${String.format("%.2f", totalCalories)} kcal"
        binding.totalProtein.text = "Protein: ${String.format("%.2f", totalProtein)} g"
        binding.totalFat.text = "Fat: ${String.format("%.2f", totalFat)} g"
        binding.totalCarbohydrates.text = "Carbohydrates: ${String.format("%.2f", totalCarbohydrates)} g"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
