package com.msoula.catlife.feature_calendar.domain.use_cases.crud

import com.msoula.catlife.feature_calendar.data.repository.FakeCalendarDataSource
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetCalendarEventsOnDateDUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.InsertCalendarEventUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetCalendarsOnDateDUseCaseTest {

    private lateinit var insertCalendarUseCase: InsertCalendarEventUseCase
    private lateinit var getCalendarsOnDateDUseCase: GetCalendarEventsOnDateDUseCase
    private lateinit var fakeCalendarRepository: FakeCalendarDataSource

    @Before
    fun setUp() {
        fakeCalendarRepository = FakeCalendarDataSource()
        insertCalendarUseCase = InsertCalendarEventUseCase(fakeCalendarRepository)
        getCalendarsOnDateDUseCase = GetCalendarEventsOnDateDUseCase(fakeCalendarRepository)
    }

    @Test
    fun `insert, fetch, should exist`() = runBlocking {

        insertCalendarUseCase.invoke(
            id = 1,
            place = "Random place",
            description = "Random description",
            endDate = 1663200000000L,
            endTime = "00:00",
            startTime = "00:00",
            startDate = 1663027200000L,
            title = "Random title",
            allDay = false,
            placeLat = "0.0",
            placeLng = "0.0"
        )

        assertTrue(getCalendarsOnDateDUseCase.invoke(1663027200000L).first().size == 1)
    }
}
