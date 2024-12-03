package com.example.myapplication.models

data class DayPlan(
    val date: String, // The date for the day plan in "YYYY-MM-DD" format
    val meals: List<MealPlan> // List of meals for the day
)
