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
    private var groupedItems: Map<String, List<ShoppingListItem>> = emptyMap(),
    private val onItemUpdate: (ShoppingListItem) -> Unit,
    private val onItemRemove: (ShoppingListItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val categoryKeys: List<String>
        get() = groupedItems.keys.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == CATEGORY_VIEW_TYPE) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category_header, parent, false)
            CategoryViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_shopping_list, parent, false)
            ItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val categoryIndex = getCategoryIndex(position)
        val category = categoryKeys[categoryIndex]
        val items = groupedItems[category] ?: emptyList()

        if (holder is CategoryViewHolder) {
            holder.categoryTextView.text = category
        } else if (holder is ItemViewHolder) {
            val itemIndex = position - getCategoryStartIndex(categoryIndex) - 1
            val item = items[itemIndex]
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
    }

    override fun getItemViewType(position: Int): Int {
        val categoryIndex = getCategoryIndex(position)
        val startIndex = getCategoryStartIndex(categoryIndex)
        return if (position == startIndex) CATEGORY_VIEW_TYPE else ITEM_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return categoryKeys.sumOf { 1 + (groupedItems[it]?.size ?: 0) }
    }

    fun updateItems(newGroupedItems: Map<String, List<ShoppingListItem>>) {
        groupedItems = newGroupedItems
        notifyDataSetChanged()
    }

    fun getFlattenedItems(): List<ShoppingListItem> {
        return groupedItems.values.flatten()
    }

    private fun getCategoryIndex(position: Int): Int {
        var currentIndex = 0
        for (i in categoryKeys.indices) {
            currentIndex += 1 + (groupedItems[categoryKeys[i]]?.size ?: 0)
            if (position < currentIndex) return i
        }
        throw IllegalArgumentException("Invalid position: $position")
    }

    private fun getCategoryStartIndex(categoryIndex: Int): Int {
        return categoryKeys.take(categoryIndex).sumOf { 1 + (groupedItems[it]?.size ?: 0) }
    }

    companion object {
        const val CATEGORY_VIEW_TYPE = 0
        const val ITEM_VIEW_TYPE = 1
    }

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryTextView: TextView = view.findViewById(R.id.categoryTextView)
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.item_name)
        val checkBox: CheckBox = view.findViewById(R.id.checkbox_purchased)
        val removeButton: Button = view.findViewById(R.id.button_remove)
    }
}
