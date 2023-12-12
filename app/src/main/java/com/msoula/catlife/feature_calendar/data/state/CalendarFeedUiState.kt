package com.msoula.catlife.feature_calendar.data.state

import commsoulacatlifedatabase.CustomEventEntity

interface CalendarFeedUiState {
    object Loading : CalendarFeedUiState
    object Empty : CalendarFeedUiState
    data class Success(val calendarFeed: List<CustomEventEntity>) : CalendarFeedUiState
}