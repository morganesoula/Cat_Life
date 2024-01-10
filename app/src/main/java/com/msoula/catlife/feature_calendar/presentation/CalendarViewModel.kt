package com.msoula.catlife.feature_calendar.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msoula.catlife.core.presentation.navigation.CalendarScreenNavArgs
import com.msoula.catlife.core.util.Resource
import com.msoula.catlife.di.DispatcherModule
import com.msoula.catlife.extension.convertToKotlinLocalDate
import com.msoula.catlife.extension.printToLog
import com.msoula.catlife.feature_calendar.data.state.CalendarFeedUiState
import com.msoula.catlife.feature_calendar.data.state.CalendarUiState
import com.msoula.catlife.feature_calendar.domain.use_case.CalendarEventUseCases
import com.msoula.catlife.feature_inventory.data.state.UiActionEvent
import com.msoula.catlife.navArgs
import commsoulacatlifedatabase.CustomEventEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.toKotlinLocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val calendarEventUseCases: CalendarEventUseCases,
    @DispatcherModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _calendarUiState = MutableStateFlow(CalendarUiState())
    val calendarUiState = _calendarUiState.asStateFlow()

    private val navArgs: CalendarScreenNavArgs = savedStateHandle.navArgs()
    private val selectedDate: Long? =
        if (navArgs.selectedStartDate != -1L) navArgs.selectedStartDate else -1L

    val eventsState: StateFlow<CalendarFeedUiState> = calendarEventUseCases.getAllEventsCalendars()
        .map { list ->
            if (list.isEmpty()) CalendarFeedUiState.Empty else CalendarFeedUiState.Success(
                list
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CalendarFeedUiState.Loading
        )

    init {
        if (selectedDate != -1L) {
            onUiEvent(
                UiActionEvent.OnCurrentDaySelected(
                    selectedDate?.convertToKotlinLocalDate() ?: java.time.LocalDate.now()
                        .toKotlinLocalDate()
                )
            )
        }
    }

    fun onUiEvent(event: UiActionEvent) {
        when (event) {
            is UiActionEvent.OnSwipeDelete -> removeEvent(event.item as CustomEventEntity)

            is UiActionEvent.OnDismissRequest -> {
                _calendarUiState.update { it.copy(openDeleteAlert = false) }
            }

            is UiActionEvent.OpenDeleteAlertDialog -> {
                _calendarUiState.update {
                    it.copy(openDeleteAlert = true, itemId = event.itemId)
                }
            }

            is UiActionEvent.OnDeleteUi -> {
                if (event.deleteData) {
                    removeDataWithId(event.elementId)
                }

                _calendarUiState.update {
                    it.copy(openDeleteAlert = false)
                }
            }

            is UiActionEvent.OnCurrentDaySelected -> {
                _calendarUiState.update {
                    it.copy(currentDaySelected = event.selectedDate)
                }
            }

            else -> Unit
        }
    }

    private fun removeEvent(event: CustomEventEntity) {
        viewModelScope.launch(ioDispatcher) {
            when (val result =
                calendarEventUseCases.deleteCalendarEventByIdUseCase.invoke(event.id.toInt())) {
                is Resource.Success -> Unit
                is Resource.Error -> result.throwable?.message.printToLog()
            }
        }
    }

    private fun removeDataWithId(itemId: Int) {
        viewModelScope.launch(ioDispatcher) {
            when (val result =
                calendarEventUseCases.deleteCalendarEventByIdUseCase.invoke(itemId)) {
                is Resource.Success -> "CatLife - Element deleted".printToLog()
                is Resource.Error -> result.throwable?.message.printToLog()
            }
        }
    }
}
