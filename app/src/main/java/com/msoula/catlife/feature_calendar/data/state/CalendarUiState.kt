package com.msoula.catlife.feature_calendar.data.state

import com.msoula.catlife.extension.convertToKotlinLocalDate
import com.msoula.catlife.globalCurrentDay
import kotlinx.datetime.LocalDate

data class CalendarUiState(
    val openDeleteAlert: Boolean = false,
    val eventDeleted: Boolean = false,
    val itemId: Int = -1,
    val currentDaySelected: LocalDate = globalCurrentDay.convertToKotlinLocalDate()
)
