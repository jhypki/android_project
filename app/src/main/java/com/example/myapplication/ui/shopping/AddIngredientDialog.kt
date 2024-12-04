package com.example.myapplication.ui.shopping

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import com.example.myapplication.models.ShoppingListItem

class AddIngredientDialog(
    private val onIngredientAdded: (ShoppingListItem) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_ingredient, null)

        val nameInput = view.findViewById<EditText>(R.id.editIngredientName)
        val quantityInput = view.findViewById<EditText>(R.id.editIngredientQuantity)
        val unitInput = view.findViewById<EditText>(R.id.editIngredientUnit)
        val categoryInput = view.findViewById<Spinner>(R.id.spinnerIngredientCategory)

        val categories = resources.getStringArray(R.array.ingredient_categories)
        categoryInput.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        )

        builder.setView(view)
            .setPositiveButton("Add") { _, _ ->
                val name = nameInput.text.toString()
                val quantity = quantityInput.text.toString()
                val unit = unitInput.text.toString()
                val category = categoryInput.selectedItem.toString()

                if (name.isNotEmpty() && quantity.isNotEmpty()) {
                    onIngredientAdded(
                        ShoppingListItem(
                            name = name,
                            quantity = quantity,
                            unit = unit,
                            category = category
                        )
                    )
                }
            }
            .setNegativeButton("Cancel", null)

        return builder.create()
    }
}
