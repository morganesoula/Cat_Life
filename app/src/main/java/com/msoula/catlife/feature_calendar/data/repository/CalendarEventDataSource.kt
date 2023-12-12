package com.msoula.catlife.feature_calendar.data.repository

import commsoulacatlifedatabase.CustomEventEntity
import kotlinx.coroutines.flow.Flow

interface CalendarEventDataSource {
    suspend fun insertCalendarEventRepository(
        title: String,
        description: String?,
        place: String,
        placeLat: String,
        placeLng: String,
        startDate: Long,
        endDate: Long,
        startTime: String,
        endTime: String,
        allDay: Boolean,
        id: Long?
    )

    suspend fun deleteCalendarEventByIdRepository(eventId: Int)
    fun getCalendarEventsOnDayD(currentDate: Long): Flow<List<CustomEventEntity>>
    suspend fun getCalendarEventById(eventId: Int): CustomEventEntity?
    fun fetchNextEventsToCome(currentDate: Long): Flow<List<CustomEventEntity>>
    fun getAllEvents(): Flow<List<CustomEventEntity>>
    suspend fun getLastInsertedEventId(): Long?
}
