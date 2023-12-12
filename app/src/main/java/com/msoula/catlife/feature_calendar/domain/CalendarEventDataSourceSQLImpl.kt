package com.msoula.catlife.feature_calendar.domain

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.msoula.CatLifeDatabase
import com.msoula.catlife.feature_calendar.data.repository.CalendarEventDataSource
import commsoulacatlifedatabase.CustomEventEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CalendarEventDataSourceSQLImpl(
    db: CatLifeDatabase
) : CalendarEventDataSource {

    private val queries = db.eventQueries

    override suspend fun insertCalendarEventRepository(
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
    ) {
        withContext(Dispatchers.IO) {
            queries.insertEvent(
                id = id,
                title = title,
                description = description,
                place = place,
                placeLat = placeLat,
                placeLng = placeLng,
                startDate = startDate,
                endDate = endDate,
                startTime = startTime,
                endTime = endTime,
                allDay = allDay
            )
        }
    }

    override suspend fun deleteCalendarEventByIdRepository(eventId: Int) {
        withContext(Dispatchers.IO) {
            queries.deleteEventById(eventId.toLong())
        }
    }

    override fun getCalendarEventsOnDayD(currentDate: Long): Flow<List<CustomEventEntity>> {
        return queries.getEventsAccordingToDay(currentDate).asFlow().mapToList(Dispatchers.IO)
    }

    override suspend fun getCalendarEventById(eventId: Int): CustomEventEntity? {
        return withContext(Dispatchers.IO) {
            queries.getEventById(eventId.toLong()).executeAsOneOrNull()
        }
    }

    override fun fetchNextEventsToCome(currentDate: Long): Flow<List<CustomEventEntity>> {
        return queries.fetchNextEventsToCome(currentDate, currentDate.plus(1209600000L)).asFlow()
            .mapToList(Dispatchers.IO)
    }

    override fun getAllEvents(): Flow<List<CustomEventEntity>> {
        return queries.getAllEvents().asFlow().mapToList(Dispatchers.IO)
    }

    override suspend fun getLastInsertedEventId(): Long? =
        queries.lastInsertRowId().executeAsOneOrNull()
}