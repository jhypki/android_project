package com.example.myapplication.ui.mealplanner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemMealBinding
import com.example.myapplication.models.MealPlan
import com.example.myapplication.models.MealWithRecipe

class MealAdapter(private var meals: List<MealWithRecipe>) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    class MealViewHolder(private val binding: ItemMealBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(meal: MealWithRecipe) {
            binding.mealTypeTextView.text = meal.mealType
            binding.recipeNameTextView.text = meal.recipeName // Use recipe name instead of ID
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = ItemMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(meals[position])
    }

    override fun getItemCount(): Int = meals.size

    fun updateMeals(newMeals: List<MealWithRecipe>) {
        meals = newMeals
        notifyDataSetChanged()
    }
}

