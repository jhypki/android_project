package com.example.myapplication.ui.mealplanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.db.MealPlanDao
import com.example.myapplication.models.DayPlan
import com.example.myapplication.models.MealPlan
import com.example.myapplication.models.MealWithRecipe
import com.example.myapplication.utils.DateUtils
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MealPlannerViewModel(private val mealPlanDao: MealPlanDao) : ViewModel() {
    val weeklyPlans: LiveData<List<DayPlan>> = liveData {
        val plans = mutableListOf<DayPlan>()
        for (i in 0..6) {
            val date = DateUtils.getDate(i)
            val meals = mealPlanDao.getMealsForDate(date).firstOrNull() ?: emptyList()
            plans.add(DayPlan(date, meals))
        }
        emit(plans)
    }

    fun addMeal(mealPlan: MealPlan) {
        viewModelScope.launch {
            mealPlanDao.insertMealPlan(mealPlan)
        }
    }

    fun getMealsForDate(date: String): LiveData<List<MealWithRecipe>> {
        return mealPlanDao.getMealsWithRecipeNamesForDate(date).asLiveData()
    }
}
