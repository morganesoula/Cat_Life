package com.msoula.catlife.feature_calendar.presentation

import androidx.lifecycle.SavedStateHandle
import com.msoula.catlife.feature_calendar.data.repository.FakeCalendarDataSource
import com.msoula.catlife.feature_calendar.domain.use_case.CalendarEventUseCases
import com.msoula.catlife.feature_calendar.domain.use_case.FetchLastInsertedEventIdUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.DeleteCalendarEventByIdUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetAllEventUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetCalendarEventByIdUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetCalendarEventsOnDateDUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetNextEventsUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.InsertCalendarEventUseCase
import com.msoula.catlife.feature_inventory.presentation.util.CoroutineTestRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModelTest {

    @JvmField
    @Rule
    var mainCoroutineDispatcher = CoroutineTestRule()

    private lateinit var viewModel: CalendarViewModel
    private lateinit var fakeCalendarRepository: FakeCalendarDataSource

    @Before
    fun setUp() {
        fakeCalendarRepository = FakeCalendarDataSource()

        runTest {
            fakeCalendarRepository.insertCalendarEventRepository(
                id = 1,
                title = "testTitle1",
                description = "testDescription1",
                place = "testPlace1",
                startDate = 1662975198363L,
                endDate = 0L,
                startTime = "00:00",
                endTime = "00:00",
                allDay = false,
                placeLat = "0",
                placeLng = "0"
            )
        }

        viewModel = CalendarViewModel(
            savedStateHandle = SavedStateHandle(mapOf("selectedStartDate" to 0L)),
            calendarEventUseCases = CalendarEventUseCases(
                insertCalendarEventUseCase = InsertCalendarEventUseCase(fakeCalendarRepository),
                getCalendarEventsAccordingToDate = GetCalendarEventsOnDateDUseCase(
                    fakeCalendarRepository
                ),
                getCalendarEventById = GetCalendarEventByIdUseCase(fakeCalendarRepository),
                deleteCalendarEventByIdUseCase = DeleteCalendarEventByIdUseCase(
                    fakeCalendarRepository
                ),
                fetchNextEventsFromCurrentDate = GetNextEventsUseCase(fakeCalendarRepository),
                getAllEventsCalendars = GetAllEventUseCase(fakeCalendarRepository),
                fetchLastInsertedEvent = FetchLastInsertedEventIdUseCase(fakeCalendarRepository)
            ),
            Dispatchers.IO
        )
    }

    @Test
    fun `get event according to currentDay, return event`() = runTest {
        assertTrue(fakeCalendarRepository.getCalendarEventsOnDayD(1662875098363L).first().isEmpty())
        assertTrue(fakeCalendarRepository.getCalendarEventsOnDayD(1662975198363L).first().size == 1)

        fakeCalendarRepository.insertCalendarEventRepository(
            // September 11th
            id = 2,
            title = "testTitle2",
            description = "testDescription2",
            place = "testPlace2",
            startDate = 1662875098363L,
            endDate = 0L,
            startTime = "00:00",
            endTime = "00:00",
            allDay = false,
            placeLat = "0",
            placeLng = "0"
        )

        this.advanceUntilIdle()

        assertTrue(fakeCalendarRepository.getCalendarEventsOnDayD(1662875098363L).first().size == 1)
    }
}
