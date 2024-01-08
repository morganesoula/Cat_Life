package com.msoula.catlife.feature_calendar.data.repository

import commsoulacatlifedatabase.CustomEventEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCalendarDataSource : CalendarEventDataSource {

    private val listOfEvents = mutableListOf<CustomEventEntity>()

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
        listOfEvents.add(
            CustomEventEntity(
                id ?: 0, title, description, place, placeLat, placeLng, startDate, endDate, startTime, endTime, allDay
            )
        )
    }

    override fun getCalendarEventsOnDayD(currentDate: Long): Flow<List<CustomEventEntity>> =
        flow { emit(listOfEvents.filter { it.startDate == currentDate }) }

    override suspend fun getCalendarEventById(eventId: Int): CustomEventEntity? =
        listOfEvents.firstOrNull { it.id == eventId.toLong() }


    override suspend fun deleteCalendarEventByIdRepository(eventId: Int) {
        listOfEvents.removeIf { it.id == eventId.toLong() }
    }

    override fun fetchNextEventsToCome(currentDate: Long): Flow<List<CustomEventEntity>> =
        flow { emit(listOfEvents.filter { it.startDate == currentDate && it.startDate < (currentDate + 1209600000L) }) }

    override fun getAllEvents(): Flow<List<CustomEventEntity>> =
        flow { emit(listOfEvents) }

    override suspend fun getLastInsertedEventId(): Long? {
        return listOfEvents.lastOrNull()?.id
    }

}
