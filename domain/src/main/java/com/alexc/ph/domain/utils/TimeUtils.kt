package com.alexc.ph.domain.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Long.unixToLocalTime(): String {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val instant = Instant.ofEpochSecond(this)
        val zoneId = ZoneId.systemDefault()
        val formatter = DateTimeFormatter.ofPattern("HH:mm").withZone(zoneId)
        return formatter.format(instant)
    } else {
        val date = Date(this * 1000)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        format.timeZone = TimeZone.getDefault()
        return format.format(date)
    }
}

fun Long.formatDate(): String {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return dateFormat.format(Date(this))
}

fun isEvening(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalTime.now().isAfter(LocalTime.of(18, 0))
    } else {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val targetHour = 18
        currentHour > targetHour || (currentHour == targetHour)
    }
}