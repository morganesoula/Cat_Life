package com.msoula.catlife.feature_calendar.domain.use_case.crud

import com.msoula.catlife.core.util.GetEventByIdError
import com.msoula.catlife.core.util.Resource
import com.msoula.catlife.feature_calendar.data.repository.CalendarEventDataSource
import commsoulacatlifedatabase.CustomEventEntity

class GetCalendarEventByIdUseCase(private val repository: CalendarEventDataSource) {

    suspend operator fun invoke(eventId: Int): Resource<CustomEventEntity?> {
        return try {
            val eventById = repository.getCalendarEventById(eventId)
            Resource.Success(eventById)
        } catch (e: GetEventByIdError) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }
}
