package com.example.myapplication.ui.shopping

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.ShoppingListItem

class ShoppingListAdapter(
    private val onItemUpdate: (ShoppingListItem) -> Unit,
    private val onItemRemove: (ShoppingListItem) -> Unit
) : RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {

    private var items: List<ShoppingListItem> = listOf()

    inner class ShoppingListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.item_name)
        val checkBox: CheckBox = view.findViewById(R.id.checkbox_purchased)
        val removeButton: Button = view.findViewById(R.id.button_remove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shopping_list, parent, false)
        return ShoppingListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val item = items[position]
        holder.nameTextView.text = "${item.name} (${item.quantity} ${item.unit})"
        holder.checkBox.isChecked = item.isPurchased

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            val updatedItem = item.copy(isPurchased = isChecked)
            onItemUpdate(updatedItem)
        }

        holder.removeButton.setOnClickListener {
            onItemRemove(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<ShoppingListItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun getItems(): List<ShoppingListItem> = items

}
