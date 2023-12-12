package com.msoula.catlife.feature_calendar.domain.use_case.crud

import com.msoula.catlife.feature_calendar.data.repository.CalendarEventDataSource
import commsoulacatlifedatabase.CustomEventEntity
import kotlinx.coroutines.flow.Flow

class GetNextEventsUseCase(private val repository: CalendarEventDataSource) {

    operator fun invoke(currentDate: Long): Flow<List<CustomEventEntity>> {
        // Add two weeks
        return repository.fetchNextEventsToCome(currentDate)
    }
}