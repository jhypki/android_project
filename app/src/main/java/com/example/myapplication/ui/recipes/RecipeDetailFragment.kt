package com.example.myapplication.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.databinding.FragmentRecipeDetailBinding

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private val recipeViewModel: RecipeViewModel by activityViewModels()

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

        // Retrieve the passed recipeId from the arguments
        val recipeId = arguments?.getInt("recipeId") ?: 0

        // Fetch the selected recipe using the recipeId
        recipeViewModel.recipes.value?.let { recipes ->
            val selectedRecipe = recipes[recipeId]

            // Set the recipe name
            binding.recipeName.text = selectedRecipe.name

            // Display ingredients in a ListView
            val ingredientDetails = selectedRecipe.ingredients.map {
                "${it.name}: ${it.quantity} ${it.unit}"
            }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                ingredientDetails
            )
            binding.ingredientsListView.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
