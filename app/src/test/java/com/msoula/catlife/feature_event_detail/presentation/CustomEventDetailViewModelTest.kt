package com.msoula.catlife.feature_event_detail.presentation

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
import com.msoula.catlife.feature_event_detail.data.state.CustomEventDetailState
import com.msoula.catlife.feature_inventory.presentation.util.CoroutineTestRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class CustomEventDetailViewModelTest {

    @JvmField
    @Rule
    var mainCoroutineDispatcher = CoroutineTestRule()

    private lateinit var viewModel: CustomEventDetailViewModel
    private lateinit var customEventUseCase: CalendarEventUseCases
    private val fakeCustomEventDataSource = FakeCalendarDataSource()

    private lateinit var stateTest: StateFlow<CustomEventDetailState>

    @Before
    fun setUp() {
        val savedStateHandle = SavedStateHandle(mapOf("eventDetailId" to 2))

        customEventUseCase = CalendarEventUseCases(
            InsertCalendarEventUseCase(fakeCustomEventDataSource),
            GetCalendarEventsOnDateDUseCase(fakeCustomEventDataSource),
            GetCalendarEventByIdUseCase(fakeCustomEventDataSource),
            DeleteCalendarEventByIdUseCase(fakeCustomEventDataSource),
            GetNextEventsUseCase(fakeCustomEventDataSource),
            GetAllEventUseCase(fakeCustomEventDataSource),
            FetchLastInsertedEventIdUseCase(fakeCustomEventDataSource)
        )

        viewModel = CustomEventDetailViewModel(
            savedStateHandle = savedStateHandle,
            customEventUseCases = customEventUseCase,
            Dispatchers.IO
        )

        stateTest = viewModel.state
    }

    @Test
    fun onUiDeleteEvent() = runTest {
        // TODO Redo one
    }
}