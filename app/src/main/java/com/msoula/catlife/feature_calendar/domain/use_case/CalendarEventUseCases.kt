package com.msoula.catlife.feature_calendar.domain.use_case

import com.msoula.catlife.feature_calendar.domain.use_case.crud.DeleteCalendarEventByIdUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetAllEventUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetCalendarEventByIdUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetCalendarEventsOnDateDUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetNextEventsUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.InsertCalendarEventUseCase

data class CalendarEventUseCases(
    val insertCalendarEventUseCase: InsertCalendarEventUseCase,
    val getCalendarEventsAccordingToDate: GetCalendarEventsOnDateDUseCase,
    val getCalendarEventById: GetCalendarEventByIdUseCase,
    val deleteCalendarEventByIdUseCase: DeleteCalendarEventByIdUseCase,
    val fetchNextEventsFromCurrentDate: GetNextEventsUseCase,
    val getAllEventsCalendars: GetAllEventUseCase,
    val fetchLastInsertedEvent: FetchLastInsertedEventIdUseCase
)
