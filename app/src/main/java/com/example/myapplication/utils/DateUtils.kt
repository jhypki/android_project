package com.example.myapplication.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun getDate(daysFromToday: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, daysFromToday)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(calendar.time)
    }
}
