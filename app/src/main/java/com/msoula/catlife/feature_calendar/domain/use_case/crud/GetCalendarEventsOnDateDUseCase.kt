package com.msoula.catlife.feature_calendar.domain.use_case.crud

import com.msoula.catlife.feature_calendar.data.repository.CalendarEventDataSource
import commsoulacatlifedatabase.CustomEventEntity
import kotlinx.coroutines.flow.Flow

class GetCalendarEventsOnDateDUseCase(private val repository: CalendarEventDataSource) {

    operator fun invoke(currentDate: Long): Flow<List<CustomEventEntity>> {
        return repository.getCalendarEventsOnDayD(currentDate)
    }

}
