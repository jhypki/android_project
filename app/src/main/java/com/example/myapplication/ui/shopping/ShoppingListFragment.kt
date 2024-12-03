package com.example.myapplication.ui.shopping

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentShoppingListBinding
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.models.ShoppingListItem
import java.util.Calendar

class ShoppingListFragment : Fragment() {

    private lateinit var binding: FragmentShoppingListBinding
    private val selectedDates = mutableListOf<String>()

    private val shoppingListViewModel: ShoppingListViewModel by activityViewModels {
        val db = AppDatabase.getDatabase(requireContext())
        ShoppingListViewModelFactory(db.mealPlanDao(), db.recipeDao(), db.shoppingListDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shoppingListAdapter = ShoppingListAdapter(
            onItemUpdate = { updatedItem ->
                shoppingListViewModel.updateShoppingItem(updatedItem)
            },
            onItemRemove = { itemToRemove ->
                shoppingListViewModel.removeShoppingItem(itemToRemove)
                Toast.makeText(requireContext(), "${itemToRemove.name} removed.", Toast.LENGTH_SHORT).show()
            }
        )

        binding.shoppingListView.layoutManager = LinearLayoutManager(requireContext())
        binding.shoppingListView.adapter = shoppingListAdapter
        binding.saveButton.setOnClickListener {
            val itemsToSave = shoppingListAdapter.getItems() // Retrieve items from the adapter
            shoppingListViewModel.saveShoppingList(itemsToSave)
            Toast.makeText(requireContext(), "Shopping list saved to the database.", Toast.LENGTH_SHORT).show()
        }

        binding.addIngredientButton.setOnClickListener {
            AddIngredientDialog { ingredient ->
                shoppingListViewModel.addIngredient(ingredient) // Save ingredient to the database
                Toast.makeText(requireContext(), "${ingredient.name} added to shopping list.", Toast.LENGTH_SHORT).show()
            }.show(parentFragmentManager, "AddIngredientDialog")
        }


        shoppingListViewModel.getShoppingList().observe(viewLifecycleOwner) { items ->
            shoppingListAdapter.updateItems(items)
        }

        binding.datePickerButton.setOnClickListener {
            showDatePicker()
        }

        binding.generateButton.setOnClickListener {
            generateShoppingList(shoppingListAdapter)
        }
    }


    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val date = "$year-${month + 1}-$dayOfMonth"
                selectedDates.add(date)
                Toast.makeText(requireContext(), "Selected Date: $date", Toast.LENGTH_SHORT).show()
                Log.d("ShoppingListFragment", "Selected Dates: $selectedDates")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun generateShoppingList(adapter: ShoppingListAdapter) {
        if (selectedDates.isEmpty()) {
            Toast.makeText(requireContext(), "Please select at least one date.", Toast.LENGTH_SHORT).show()
            return
        }

        shoppingListViewModel.generateShoppingListForDates(selectedDates).observe(viewLifecycleOwner) { ingredientMap ->
            val items = ingredientMap.values.flatten()
            adapter.updateItems(items)
            Toast.makeText(requireContext(), "Shopping list generated.", Toast.LENGTH_SHORT).show()
        }
    }
}
