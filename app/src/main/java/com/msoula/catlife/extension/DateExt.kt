package com.msoula.catlife.extension

import android.content.Context
import com.msoula.catlife.R
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import java.text.DateFormat
import java.time.Instant
import java.time.format.FormatStyle
import java.util.Calendar
import java.util.Locale

fun Long?.toDateString(context: Context, allowFutureDate: Boolean): String {
    return this?.let { time ->
        val currentTime = Instant.now().toEpochMilli()

        return if (!allowFutureDate) {
            if (time >= currentTime)
                context.getString(R.string.inconsistent)
            else
                time.formatDate()
        } else {
            time.formatDate()
        }
    } ?: ""
}

fun Long?.toDateString(): String = this?.formatDate() ?: ""

private fun Long.formatDate(): String {
    val formatter = DateFormat.getDateInstance(FormatStyle.MEDIUM.ordinal, Locale.getDefault())
    val calendar = Calendar.getInstance()

    calendar.timeInMillis = this
    return formatter.format(calendar.time)
}

fun Long?.accordingToLocale(context: Context): String =
    longToDate(this, context.resources.configuration.locales[0])

fun Long.convertToKotlinLocalDate(): LocalDate {
    val instant = kotlinx.datetime.Instant.fromEpochMilliseconds(this)
    return instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
}

fun LocalDate.toMilliseconds(): Long {
    return this.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}

private fun longToDate(timestamp: Long?, locale: Locale): String {
    return timestamp?.let {
        DateFormat.getDateInstance(FormatStyle.MEDIUM.ordinal, locale).format(it)
    } ?: ""
}

