package com.msoula.catlife.feature_calendar.data.state

import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import java.time.LocalDate
import java.time.LocalTime

sealed interface AddEditEventFormEvent {

    data class OnEventTitleChanged(val title: String) : AddEditEventFormEvent
    data class OnEventDescriptionChanged(val description: String) : AddEditEventFormEvent
    data class OnEventPlaceChanged(val place: String) : AddEditEventFormEvent
    data class OnEventPlaceSelected(
        val placeId: String,
        val address: String,
        val token: AutocompleteSessionToken
    ) : AddEditEventFormEvent

    data class OnEventStartDateChanged(val startDate: LocalDate) : AddEditEventFormEvent
    data class OnEventEndDateChanged(val endDate: LocalDate) : AddEditEventFormEvent
    data class OnEventStartTimeChanged(val startTime: LocalTime) : AddEditEventFormEvent
    data class OnEventEndTimeChanged(val endTime: LocalTime) : AddEditEventFormEvent
    data class OnEventAllDayChecked(val allDay: Boolean) : AddEditEventFormEvent
    data class OnCurrentDateSelected(val selectedDate: Long) : AddEditEventFormEvent
    data object ClearMapData : AddEditEventFormEvent
    data object ClearPredictions : AddEditEventFormEvent
    data object Submit : AddEditEventFormEvent
}
