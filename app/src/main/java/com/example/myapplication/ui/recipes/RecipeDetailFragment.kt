package com.example.myapplication.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.databinding.FragmentRecipeDetailBinding
import com.example.myapplication.db.AppDatabase

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private val recipeViewModel: RecipeViewModel by activityViewModels {
        val recipeDao = AppDatabase.getDatabase(requireContext()).recipeDao() // Updated database reference
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

        // Get the recipeId from arguments
        val recipeId = arguments?.getInt("recipeId") ?: return

        // Observe the recipe details
        recipeViewModel.getRecipeById(recipeId).observe(viewLifecycleOwner) { recipe ->
            binding.recipeName.text = recipe?.name ?: "Recipe not found"
        }

        // Observe the ingredients for the recipe
        recipeViewModel.getIngredientsForRecipe(recipeId).observe(viewLifecycleOwner) { ingredients ->
            val ingredientDetails = ingredients.map { "${it.name}: ${it.quantity} ${it.unit}" }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, ingredientDetails)
            binding.ingredientsListView.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
