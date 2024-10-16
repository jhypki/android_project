package com.example.myapplication.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentAddRecipeBinding

class AddRecipeFragment : Fragment() {

    private var _binding: FragmentAddRecipeBinding? = null
    private val binding get() = _binding!!
    private var ingredientCount = 0
    private val unitsArray =
        listOf("grams", "kilograms", "ml", "liters", "cups", "teaspoons", "tablespoons")

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
        addNewIngredientRow()
        binding.addIngredientButton.setOnClickListener {
            addNewIngredientRow()
        }
    }

    private fun addNewIngredientRow() {
        ingredientCount++
        val ingredientRow = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val ingredientName = EditText(requireContext()).apply {
            id = View.generateViewId()
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            hint = "Ingredient $ingredientCount"
            inputType = android.text.InputType.TYPE_CLASS_TEXT
        }

        val ingredientQuantity = EditText(requireContext()).apply {
            id = View.generateViewId()
            layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f)
            hint = "Qty"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }

        val ingredientUnit = Spinner(requireContext()).apply {
            id = View.generateViewId()
            layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f)
            adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                unitsArray
            )
        }

        val removeButton = Button(requireContext()).apply {
            id = View.generateViewId()
            layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f)
            text = "X"
            setOnClickListener {
                binding.ingredientsContainer.removeView(ingredientRow)
                ingredientCount--
            }
        }
        
        ingredientRow.addView(ingredientName)
        ingredientRow.addView(ingredientQuantity)
        ingredientRow.addView(ingredientUnit)
        ingredientRow.addView(removeButton)
        binding.ingredientsContainer.addView(ingredientRow)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
