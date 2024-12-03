package com.example.myapplication.ui.mealplanner

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.FragmentMealPlannerBinding
import com.example.myapplication.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MealPlannerFragment : Fragment() {

    private var _binding: FragmentMealPlannerBinding? = null
    private val binding get() = _binding!!

    private val mealPlanDao by lazy {
        AppDatabase.getDatabase(requireContext()).mealPlanDao()
    }

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Standard date format

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealPlannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateListView()

        binding.addMealButton.setOnClickListener {
            showDatePicker { selectedDate ->
                AddMealDialog(selectedDate) { mealPlan ->
                    lifecycleScope.launch {
                        mealPlanDao.insertMealPlan(mealPlan)
                        Toast.makeText(requireContext(), "Meal added for $selectedDate", Toast.LENGTH_SHORT).show()
                        updateListView()
                    }
                }.show(parentFragmentManager, "AddMealDialog")
            }
        }
    }

    private fun updateListView() {
        lifecycleScope.launch {
            mealPlanDao.getAllMealPlans().collect { meals ->
                val groupedMeals = meals.groupBy { it.date }
                val displayData = mutableListOf<String>()

                for ((date, dailyMeals) in groupedMeals) {
                    displayData.add("Date: $date") // Add the date as a header
                    for (meal in dailyMeals) {
                        val recipeName = withContext(Dispatchers.IO) {
                            AppDatabase.getDatabase(requireContext()).recipeDao().getRecipeNameById(meal.recipeId)
                        }
                        displayData.add("- ${meal.mealType}: $recipeName")
                    }
                }

                // Update ListView
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, displayData)
                binding.mealsListView.adapter = adapter
            }
        }

    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val selectedDate = dateFormat.format(calendar.time)
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
