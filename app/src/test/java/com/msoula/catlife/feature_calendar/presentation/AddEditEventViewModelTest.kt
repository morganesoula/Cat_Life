package com.msoula.catlife.feature_calendar.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.android.libraries.places.api.net.PlacesClient
import com.msoula.catlife.feature_calendar.custom_places.domain.use_case.FetchPlaceUseCase
import com.msoula.catlife.feature_calendar.data.repository.FakeCalendarDataSource
import com.msoula.catlife.feature_calendar.data.state.AddEditEventFormEvent
import com.msoula.catlife.feature_calendar.data.state.CalendarEventFormState
import com.msoula.catlife.feature_calendar.domain.use_case.CalendarEventUseCases
import com.msoula.catlife.feature_calendar.domain.use_case.FetchLastInsertedEventIdUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.DeleteCalendarEventByIdUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetAllEventUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetCalendarEventByIdUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetCalendarEventsOnDateDUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetNextEventsUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.InsertCalendarEventUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.EventValidationUseCases
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidateAllDayDate
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidateDescription
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidateEndDate
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidatePlace
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidateStartDate
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidateStartEndDate
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidateStartEndDateTime
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidateTitle
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalTime

class AddEditEventViewModelTest {

    private lateinit var viewModel: AddEditEventViewModel
    private lateinit var fakeCalendarRepository: FakeCalendarDataSource
    private lateinit var state: StateFlow<CalendarEventFormState>

    private lateinit var placesClients: PlacesClient

    @Before
    fun setUp() {
        val savedStateHandle = SavedStateHandle(listOf("eventUpdateId" to 0, "currentDateSelected" to 1L).toMap())
        placesClients = mockk()

        fakeCalendarRepository = FakeCalendarDataSource()

        viewModel = AddEditEventViewModel(
            savedStateHandle,
            Dispatchers.IO,
            CalendarEventUseCases(
                InsertCalendarEventUseCase(fakeCalendarRepository),
                GetCalendarEventsOnDateDUseCase(fakeCalendarRepository),
                GetCalendarEventByIdUseCase(fakeCalendarRepository),
                DeleteCalendarEventByIdUseCase(fakeCalendarRepository),
                GetNextEventsUseCase(fakeCalendarRepository),
                GetAllEventUseCase(fakeCalendarRepository),
                FetchLastInsertedEventIdUseCase(fakeCalendarRepository)
            ),
            EventValidationUseCases(
                ValidateStartDate(),
                ValidateEndDate(),
                ValidateDescription(),
                ValidateTitle(),
                ValidatePlace(),
                ValidateStartEndDate(),
                ValidateStartEndDateTime(),
                ValidateAllDayDate()
            ),
            FetchPlaceUseCase(placesClients)
        )
        state = viewModel.state
    }

    @Test
    fun onUiEventTitleTest() = runTest {
        state.test {
            assertTrue(expectMostRecentItem().currentEventTitle.isEmpty())
        }

        viewModel.onUiEvent(AddEditEventFormEvent.OnEventTitleChanged("titleTest")) { }
        state = viewModel.state

        state.test {
            assertTrue(expectMostRecentItem().currentEventTitle == "titleTest")
        }
    }

    @Test
    fun onUiEventDescriptionTest() = runTest {
        state.test {
            expectMostRecentItem().currentEventDescription?.let { assertTrue(it.isEmpty()) }
        }

        viewModel.onUiEvent(AddEditEventFormEvent.OnEventDescriptionChanged("descriptionTest")) { }
        state = viewModel.state

        state.test {
            assertTrue(expectMostRecentItem().currentEventDescription == "descriptionTest")
        }
    }

    @Test
    fun onUiEventAllDayTest() = runTest {
        state.test {
            assertFalse(expectMostRecentItem().currentEventAllDayChecked)
        }

        viewModel.onUiEvent(AddEditEventFormEvent.OnEventAllDayChecked(true)) { }
        state = viewModel.state

        state.test {
            assertTrue(expectMostRecentItem().currentEventAllDayChecked)
        }
    }

    @Test
    fun onUiEventStartTimeTest() = runTest {
        state.test {
            assertTrue(expectMostRecentItem().currentEventStartTime == "00:00")
        }

        viewModel.onUiEvent(AddEditEventFormEvent.OnEventStartTimeChanged(LocalTime.of(12, 3))) { }
        state = viewModel.state

        state.test {
            assertTrue(expectMostRecentItem().currentEventStartTime == "12:03")
        }
    }

    @Test
    fun onUiEventEndTimeTest() = runTest {
        state.test {
            assertTrue(expectMostRecentItem().currentEventEndTime == "00:00")
        }

        viewModel.onUiEvent(AddEditEventFormEvent.OnEventEndTimeChanged(LocalTime.of(3, 2))) { }
        state = viewModel.state

        state.test {
            assertTrue(expectMostRecentItem().currentEventEndTime == "03:02")
        }
    }
}
