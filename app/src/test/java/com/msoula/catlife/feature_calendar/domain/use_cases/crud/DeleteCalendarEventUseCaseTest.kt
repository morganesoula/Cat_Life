package com.msoula.catlife.feature_calendar.domain.use_cases.crud

import com.msoula.catlife.feature_calendar.data.repository.FakeCalendarDataSource
import com.msoula.catlife.feature_calendar.domain.use_case.crud.DeleteCalendarEventByIdUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetCalendarEventsOnDateDUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.InsertCalendarEventUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeleteCalendarEventUseCaseTest {

    private lateinit var insertCalendarEventUseCase: InsertCalendarEventUseCase
    private lateinit var getCalendarEventsOnDateDUseCase: GetCalendarEventsOnDateDUseCase
    private lateinit var deleteCalendarEventUseCase: DeleteCalendarEventByIdUseCase
    private lateinit var fakeRepository: FakeCalendarDataSource

    @Before
    fun setUp() {
        fakeRepository = FakeCalendarDataSource()
        insertCalendarEventUseCase = InsertCalendarEventUseCase(fakeRepository)
        getCalendarEventsOnDateDUseCase = GetCalendarEventsOnDateDUseCase(fakeRepository)
        deleteCalendarEventUseCase = DeleteCalendarEventByIdUseCase(fakeRepository)
    }

    @Test
    fun `insert, fetch, delete, fetch, should be removed`() = runBlocking {
        insertCalendarEventUseCase.invoke(
            id = 1,
            place = "Random place",
            description = "Random description",
            endDate = 1663200000000L,
            endTime = "00:00",
            startTime = "00:00",
            startDate = 1663027200000L,
            title = "Random title",
            allDay = false,
            placeLng = "0",
            placeLat = "0"
        )

        assertTrue(getCalendarEventsOnDateDUseCase.invoke(1663027200000L).first().size == 1)

        deleteCalendarEventUseCase.invoke(1)
        assertTrue(getCalendarEventsOnDateDUseCase.invoke(1663027200000L).first().isEmpty())
    }
}
