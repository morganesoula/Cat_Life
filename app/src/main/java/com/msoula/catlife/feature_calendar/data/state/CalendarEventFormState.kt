package com.msoula.catlife.feature_calendar.data.state

import com.msoula.catlife.R
import com.msoula.catlife.feature_calendar.custom_places.data.CustomPlace
import com.msoula.catlife.globalCurrentDay
import commsoulacatlifedatabase.CustomEventEntity

data class CalendarEventFormState(
    val eventId: Int? = null,
    val currentEventTitle: String = "",
    val eventTitleError: Int? = null,
    val currentEventDescription: String? = "",
    val eventDescriptionError: Int? = null,
    val currentEventPlace: String = "",
    val eventPlaceError: Int? = null,
    val currentEventPlaceLat: Double = 0.0,
    val currentEventPlaceLng: Double = 0.0,
    val currentEventStartDate: Long = globalCurrentDay,
    val eventStartDateError: Int? = null,
    val currentEventEndDate: Long = 0L,
    val eventEndDateError: Int? = null,
    val currentEventStartTime: String = "00:00",
    val currentEventEndTime: String = "00:00",
    val eventStartAndEndTimeError: Int? = null,
    val currentEventAllDayChecked: Boolean = false,
    val eventAllDayCheckedError: Int? = null,
    val listOfPredictions: List<CustomPlace>? = null,
    val enableSubmit: Boolean = false,
    val addEditEventComposableTitle: Int = R.string.add_event_title,
    val submitEventText: Int = R.string.submit_event_title,
    val lastInsertedEventId: Int? = null
)

fun CalendarEventFormState.mapToEvent(): CustomEventEntity =
    CustomEventEntity(
        allDay = this.currentEventAllDayChecked,
        id = this.eventId?.toLong() ?: -1L,
        endDate = this.currentEventEndDate,
        startDate = this.currentEventStartDate,
        startTime = this.currentEventStartTime,
        endTime = this.currentEventEndTime,
        place = this.currentEventPlace,
        title = this.currentEventTitle,
        description = this.currentEventDescription,
        placeLat = this.currentEventPlaceLat.toString(),
        placeLng = this.currentEventPlaceLng.toString()
    )
