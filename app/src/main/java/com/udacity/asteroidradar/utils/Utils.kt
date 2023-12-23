package com.udacity.asteroidradar.utils

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


fun getToday():String{
    val date = Date()

    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val today: String = dateFormat.format(date)

    Timber.i("Today: ${today}")
    return today
}

fun getNextWeek():String{
    val date = Date()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    val calendarNextWeek: Calendar = Calendar.getInstance()
    calendarNextWeek.time = date
    calendarNextWeek.add(Calendar.WEEK_OF_YEAR, 1)

    val nextWeek: Date = calendarNextWeek.time

    val formattedNextWeek: String = dateFormat.format(nextWeek)

    Timber.i("Next week's date: $formattedNextWeek")
    return formattedNextWeek
}