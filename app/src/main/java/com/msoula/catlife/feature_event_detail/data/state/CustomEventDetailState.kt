package com.msoula.catlife.feature_event_detail.data.state

data class CustomEventDetailState(
    val eventId: Int = 0,
    val eventTitle: String = "",
    val eventDescription: String = "",
    val eventPlace: String = "",
    val eventPlaceLat: Double = 0.0,
    val eventPlaceLng: Double = 0.0,
    val eventAllDay: Boolean = false,
    val eventDateStart: Long = 0L,
    val eventDateEnd: Long = 0L,
    val eventTimeStart: String = "",
    val eventTimeEnd: String = "",
    val openDeleteAlert: Boolean = false
)
