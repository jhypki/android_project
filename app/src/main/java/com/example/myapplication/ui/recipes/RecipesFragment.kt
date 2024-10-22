package com.example.myapplication.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRecipesBinding

class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private val recipeViewModel: RecipeViewModel by activityViewModels()

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

        recipeViewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            val recipeNames = recipes.map { it.name }
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, recipeNames)
            binding.recipeListView.adapter = adapter
        }

        // Handle recipe item click
        binding.recipeListView.setOnItemClickListener { _, _, position, _ ->
            val bundle = Bundle().apply {
                putInt("recipeId", position)  // Pass the selected recipe position
            }
            findNavController().navigate(R.id.recipeDetailFragment, bundle)  // Navigate with bundle
        }

        // Navigate to AddRecipeFragment
        binding.addRecipeButton.setOnClickListener {
            findNavController().navigate(R.id.addRecipeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
