package com.msoula.catlife.feature_calendar.domain.use_case

import com.msoula.catlife.feature_calendar.data.repository.CalendarEventDataSource

class FetchLastInsertedEventIdUseCase(private val eventDataSource: CalendarEventDataSource) {

    suspend operator fun invoke(): Long? = eventDataSource.getLastInsertedEventId()
}
