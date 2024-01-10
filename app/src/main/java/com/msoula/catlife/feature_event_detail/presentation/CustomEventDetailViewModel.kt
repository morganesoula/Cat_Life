package com.msoula.catlife.feature_event_detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msoula.catlife.core.presentation.navigation.EventDetailScreenNavArgs
import com.msoula.catlife.core.util.Resource
import com.msoula.catlife.di.DispatcherModule
import com.msoula.catlife.extension.printToLog
import com.msoula.catlife.feature_calendar.domain.use_case.CalendarEventUseCases
import com.msoula.catlife.feature_event_detail.data.state.CustomEventDetailState
import com.msoula.catlife.feature_inventory.data.state.UiActionEvent
import com.msoula.catlife.navArgs
import commsoulacatlifedatabase.CustomEventEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CustomEventDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val customEventUseCases: CalendarEventUseCases,
    @DispatcherModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(CustomEventDetailState())
    val state = _state.asStateFlow()

    private val navArgs: EventDetailScreenNavArgs = savedStateHandle.navArgs()

    init {
        fetchCustomEvent(navArgs.eventDetailId)
    }

    fun onUiEvent(event: UiActionEvent, goBackToListScreen: () -> Unit) {
        when (event) {
            is UiActionEvent.OpenDeleteAlertDialog -> {
                _state.update { it.copy(openDeleteAlert = true, eventId = event.itemId) }
            }

            is UiActionEvent.OnDismissRequest -> {
                _state.update { it.copy(openDeleteAlert = false) }
            }

            is UiActionEvent.OnDeleteUi -> {
                if (event.deleteData) {
                    deleteEventById(event.elementId, goBackToListScreen)
                }
            }

            else -> Unit
        }
    }

    private fun fetchCustomEvent(eventId: Int) {
        viewModelScope.launch(ioDispatcher) {
            when (val result = customEventUseCases.getCalendarEventById.invoke(eventId)) {
                is Resource.Success -> {
                    updateEventFromDB(result.data)
                }

                is Resource.Error -> {
                    result.throwable?.message.printToLog()
                }
            }
        }
    }

    private fun updateEventFromDB(eventInDb: CustomEventEntity?) {
        eventInDb?.let { event ->
            _state.update {
                it.copy(
                    eventId = navArgs.eventDetailId,
                    eventTitle = event.title,
                    eventDescription = event.description ?: "",
                    eventPlace = event.place,
                    eventPlaceLat = event.placeLat.toDouble(),
                    eventPlaceLng = event.placeLng.toDouble(),
                    eventAllDay = event.allDay,
                    eventDateStart = event.startDate,
                    eventDateEnd = event.endDate,
                    eventTimeStart = event.startTime,
                    eventTimeEnd = event.endTime
                )
            }
        }
    }

    private fun deleteEventById(id: Int, goBackToListScreen: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            when (val result = customEventUseCases.deleteCalendarEventByIdUseCase.invoke(id)) {
                is Resource.Success -> {
                    withContext(Dispatchers.Main) { goBackToListScreen() }
                }

                is Resource.Error -> {
                    result.throwable?.message.printToLog()
                }
            }
        }
    }
}
