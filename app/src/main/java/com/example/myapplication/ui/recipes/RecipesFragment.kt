package com.example.myapplication.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRecipesBinding
import com.example.myapplication.db.RecipeDatabase

class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private val recipeDao by lazy {
        RecipeDatabase.getDatabase(requireContext()).recipeDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe and display the recipes list
        recipeDao.getAllRecipes().asLiveData().observe(viewLifecycleOwner) { recipes ->
            val recipeNames = recipes.map { it.name }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, recipeNames)
            binding.recipeListView.adapter = adapter

            // Set click listener to navigate to RecipeDetailFragment with recipeId
            binding.recipeListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedRecipeId = recipes[position].id
                val bundle = Bundle().apply {
                    putInt("recipeId", selectedRecipeId)
                }
                findNavController().navigate(R.id.action_recipesFragment_to_recipeDetailFragment, bundle)
            }
        }

        // Navigate to AddRecipeFragment when addRecipeButton is clicked
        binding.addRecipeButton.setOnClickListener {
            findNavController().navigate(R.id.action_recipesFragment_to_addRecipeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
