package com.msoula.catlife.feature_calendar.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult
import com.msoula.catlife.core.presentation.OnLifecycleEvent
import com.msoula.catlife.core.presentation.navigation.AddEditEventFormScreenNavArgs
import com.msoula.catlife.core.util.Resource
import com.msoula.catlife.di.DispatcherModule
import com.msoula.catlife.extension.printToLog
import com.msoula.catlife.extension.resolveError
import com.msoula.catlife.feature_calendar.custom_places.data.CustomPlace
import com.msoula.catlife.feature_calendar.custom_places.data.state.State
import com.msoula.catlife.feature_calendar.custom_places.domain.use_case.FetchPlaceUseCase
import com.msoula.catlife.feature_calendar.data.state.AddEditEventFormEvent
import com.msoula.catlife.feature_calendar.data.state.CalendarEventFormState
import com.msoula.catlife.feature_calendar.data.state.mapToEvent
import com.msoula.catlife.feature_calendar.domain.use_case.CalendarEventUseCases
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.EventImplValidationUseCases
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.EventValidationUseCases
import com.msoula.catlife.globalCurrentDay
import com.msoula.catlife.navArgs
import commsoulacatlifedatabase.CustomEventEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class AddEditEventViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @DispatcherModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val calendarEventUseCases: CalendarEventUseCases,
    private val validationEventUseCases: EventValidationUseCases,
    private val fetchPlaceUseCase: FetchPlaceUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CalendarEventFormState())
    val state = _state.asStateFlow()

    private val navArgs: AddEditEventFormScreenNavArgs = savedStateHandle.navArgs()
    private var currentEventId: Int? =
        if (navArgs.eventUpdateId != -1) navArgs.eventUpdateId else null

    private var currentDaySelected: Long? =
        if (navArgs.currentDateSelected != -1L) navArgs.currentDateSelected else globalCurrentDay

    private lateinit var existingEvent: CustomEventEntity

    init {
        checkIfUpdate(currentEventId)
    }

    fun onLifecycleEvent(event: OnLifecycleEvent) {
        when (event) {
            is OnLifecycleEvent.OnBackPressed -> {
                _state.update { CalendarEventFormState() }
            }
        }
    }

    fun onUiEvent(event: AddEditEventFormEvent, onEventAddedOrUpdated: (id: Int?) -> Unit) {
        when (event) {
            is AddEditEventFormEvent.OnEventTitleChanged -> {
                _state.update {
                    it.copy(currentEventTitle = event.title)
                }

                validateInput(EventImplValidationUseCases.ValidateTitle)
            }

            is AddEditEventFormEvent.OnEventDescriptionChanged -> {
                _state.update {
                    it.copy(currentEventDescription = event.description)
                }

                validateInput(EventImplValidationUseCases.ValidateDescription)
            }

            is AddEditEventFormEvent.OnEventPlaceChanged -> {
                _state.update {
                    it.copy(currentEventPlace = event.place)
                }

                if (event.place.length > 2) {
                    getPredictionsForGivenAddress(address = event.place)
                }

                validateInput(EventImplValidationUseCases.ValidatePlace)
            }

            is AddEditEventFormEvent.ClearMapData -> {
                _state.update {
                    it.copy(currentEventPlaceLat = 0.0, currentEventPlaceLng = 0.0)
                }
            }

            is AddEditEventFormEvent.OnEventPlaceSelected -> {
                viewModelScope.launch {
                    getPlaceDetails(event.placeId, event.token)
                }

                _state.update {
                    it.copy(
                        currentEventPlace = event.address,
                    )
                }

                if (state.value.currentEventPlace.isNotEmpty()) {
                    _state.update {
                        it.copy(listOfPredictions = emptyList())
                    }
                }

                validateInput(EventImplValidationUseCases.ValidatePlace)
            }

            is AddEditEventFormEvent.OnEventStartDateChanged -> {
                _state.update {
                    it.copy(
                        currentEventStartDate = event.startDate.atStartOfDay(ZoneId.systemDefault())
                            .toEpochSecond().times(1000)
                    )
                }

                validateInput(EventImplValidationUseCases.ValidateStartDate)
            }

            is AddEditEventFormEvent.OnEventEndDateChanged -> {
                _state.update {
                    it.copy(
                        currentEventEndDate = event.endDate.atStartOfDay(ZoneId.systemDefault())
                            .toEpochSecond().times(1000)
                    )
                }

                validateInput(EventImplValidationUseCases.ValidateEndDate)
                validateInput(EventImplValidationUseCases.ValidateStartEndDateTime)
            }

            is AddEditEventFormEvent.OnEventStartTimeChanged -> {
                _state.update {
                    it.copy(
                        currentEventStartTime = event.startTime.toString()
                    )

                }
            }

            is AddEditEventFormEvent.OnEventEndTimeChanged -> {
                _state.update {
                    it.copy(
                        currentEventEndTime = event.endTime.toString()
                    )
                }

                validateInput(EventImplValidationUseCases.ValidateStartEndDateTime)
            }

            is AddEditEventFormEvent.OnEventAllDayChecked -> {
                _state.update {
                    it.copy(currentEventAllDayChecked = event.allDay)
                }

                if (event.allDay) {
                    _state.update { it.copy(eventEndDateError = null, currentEventEndDate = 0L) }
                    validateInput(EventImplValidationUseCases.ValidateStartDate)
                } else {
                    if (state.value.currentEventEndDate != 0L) {
                        validateInput(EventImplValidationUseCases.ValidateEndDate)
                    }
                }
            }

            is AddEditEventFormEvent.OnCurrentDateSelected -> {
                _state.update {
                    it.copy(
                        currentEventStartDate = event.selectedDate
                    )
                }

                validateInput(EventImplValidationUseCases.ValidateStartDate)
            }

            is AddEditEventFormEvent.ClearPredictions -> {
                _state.update { it.copy(listOfPredictions = emptyList()) }
            }

            is AddEditEventFormEvent.Submit -> saveEvent(onEventAddedOrUpdated)
        }
    }


    private fun validateInput(
        validationUseCases: EventImplValidationUseCases
    ) {
        val validationResult = when (validationUseCases) {
            EventImplValidationUseCases.ValidateTitle -> validationEventUseCases.validateTitle.execute(
                state.value.currentEventTitle
            )

            EventImplValidationUseCases.ValidatePlace -> validationEventUseCases.validatePlace.execute(
                state.value.currentEventPlace
            )

            EventImplValidationUseCases.ValidateDescription -> validationEventUseCases.validateDescription.execute(
                state.value.currentEventDescription
            )

            EventImplValidationUseCases.ValidateStartDate -> {
                validationEventUseCases.validateStartDate.execute(
                    date = state.value.currentEventStartDate,
                    endDate = state.value.currentEventEndDate
                )
            }

            EventImplValidationUseCases.ValidateEndDate ->
                validationEventUseCases.validateEndDate.execute(
                    state.value.currentEventEndDate,
                    state.value.currentEventStartDate
                )

            EventImplValidationUseCases.ValidateStartEndDateTime -> if (state.value.currentEventAllDayChecked) {
                ValidationResult(true)
            } else {
                validationEventUseCases.validateStartEndDateTime.execute(
                    state.value.currentEventStartDate,
                    state.value.currentEventEndDate,
                    state.value.currentEventStartTime,
                    state.value.currentEventEndTime
                )
            }
        }

        val hasError = !validationResult.successful

        _state.update {
            when (validationUseCases) {
                EventImplValidationUseCases.ValidateTitle -> it.copy(eventTitleError = validationResult.errorMessage)
                EventImplValidationUseCases.ValidatePlace -> it.copy(eventPlaceError = validationResult.errorMessage)
                EventImplValidationUseCases.ValidateDescription -> it.copy(eventDescriptionError = validationResult.errorMessage)
                EventImplValidationUseCases.ValidateStartDate -> it.copy(eventStartDateError = validationResult.errorMessage)
                EventImplValidationUseCases.ValidateEndDate -> it.copy(eventEndDateError = validationResult.errorMessage)
                EventImplValidationUseCases.ValidateStartEndDateTime -> it.copy(
                    eventStartAndEndTimeError = validationResult.errorMessage
                )
            }
        }

        if (hasError) {
            if (state.value.enableSubmit) _state.update { it.copy(enableSubmit = false) }
            return
        }

        enableSubmit()
    }

    private fun enableSubmit() {
        val hasError = validateUseCase().any { !it.successful }

        if (hasError) return

        _state.update {
            it.copy(enableSubmit = !(::existingEvent.isInitialized && existingEvent == state.value.mapToEvent()))
        }
    }

    private fun validateUseCase(): List<ValidationResult> {
        val titleResult =
            validationEventUseCases.validateTitle.execute(state.value.currentEventTitle)
        val placeResult =
            validationEventUseCases.validatePlace.execute(state.value.currentEventPlace)
        val descriptionResult = if (state.value.currentEventDescription != "") {
            validationEventUseCases.validateDescription.execute(state.value.currentEventDescription)
        } else {
            ValidationResult(true)
        }

        val allDayResult =
            validationEventUseCases.validateAllDayDate.execute(state.value.currentEventStartDate)
        val endDateResult = if (state.value.currentEventAllDayChecked) {
            ValidationResult(true)
        } else {
            validationEventUseCases.validateEndDate.execute(
                state.value.currentEventEndDate,
                state.value.currentEventStartDate
            )
        }
        val startAndEndTimeResult = if (state.value.currentEventAllDayChecked) {
            ValidationResult(true)
        } else {
            validationEventUseCases.validateStartEndDateTime.execute(
                state.value.currentEventStartDate,
                state.value.currentEventEndDate,
                state.value.currentEventStartTime,
                state.value.currentEventEndTime
            )
        }

        return listOf(
            titleResult,
            placeResult,
            descriptionResult,
            allDayResult,
            startAndEndTimeResult,
            endDateResult
        )
    }

    private fun saveEvent(onEventAddedOrUpdated: (id: Int?) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            val result = calendarEventUseCases.insertCalendarEventUseCase.invoke(
                id = currentEventId?.toLong(),
                title = state.value.currentEventTitle,
                description = state.value.currentEventDescription,
                place = state.value.currentEventPlace,
                placeLat = state.value.currentEventPlaceLat.toString(),
                placeLng = state.value.currentEventPlaceLng.toString(),
                startDate = state.value.currentEventStartDate,
                endDate = state.value.currentEventEndDate,
                startTime = if (state.value.currentEventAllDayChecked) {
                    "00:00"
                } else {
                    state.value.currentEventStartTime
                },
                endTime = if (state.value.currentEventAllDayChecked) {
                    "00:00"
                } else {
                    state.value.currentEventEndTime
                },
                allDay = state.value.currentEventAllDayChecked
            )

            when (result) {
                is Resource.Success -> {
                    fetchLastInsertedEventId(onEventAddedOrUpdated)
                }

                is Resource.Error -> {
                    result.throwable?.message.printToLog()
                }
            }
        }
    }

    private fun fetchLastInsertedEventId(onEventAddedOrUpdated: (id: Int?) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            _state.update {
                it.copy(
                    lastInsertedEventId = calendarEventUseCases.fetchLastInsertedEvent()?.toInt()
                        ?: -1
                )
            }

            withContext(Dispatchers.Main) {
                onEventAddedOrUpdated(state.value.lastInsertedEventId)
            }
        }
    }

    private fun checkIfUpdate(eventId: Int?) {
        eventId?.let {
            getEventById(eventId)
        } ?: updateStartDate()
    }

    private fun getEventById(eventId: Int) {
        viewModelScope.launch(ioDispatcher) {
            when (val result = calendarEventUseCases.getCalendarEventById.invoke(eventId)) {
                is Resource.Success -> {
                    result.data?.let {
                        currentEventId = it.id.toInt()
                        initEvent(it)
                    }
                }

                is Resource.Error -> {
                    result.throwable?.message.printToLog()
                }
            }
        }
    }

    private fun initEvent(event: CustomEventEntity) {
        _state.update {
            it.copy(
                eventId = event.id.toInt(),
                currentEventTitle = event.title,
                currentEventDescription = event.description,
                currentEventPlace = event.place,
                currentEventPlaceLng = event.placeLng.toDouble(),
                currentEventPlaceLat = event.placeLat.toDouble(),
                currentEventAllDayChecked = event.allDay,
                currentEventStartDate = event.startDate,
                currentEventEndDate = event.endDate,
                currentEventEndTime = event.endTime,
                currentEventStartTime = event.startTime,
                addEditEventComposableTitle = R.string.update_event_title,
                submitEventText = R.string.update_general_btn
            )
        }

        existingEvent = event

        validateInput(EventImplValidationUseCases.ValidateStartDate)
        if (!event.allDay) validateInput(EventImplValidationUseCases.ValidateEndDate)
    }

    private fun updateStartDate() {
        _state.update {
            it.copy(currentEventStartDate = currentDaySelected ?: globalCurrentDay)
        }

        validateInput(EventImplValidationUseCases.ValidateStartDate)
    }

    @Suppress("UNCHECKED_CAST")
    private fun getPredictionsForGivenAddress(address: String) {
        viewModelScope.launch(ioDispatcher) {
            getPredictionsResponse(address).collect { stateList ->
                when (stateList) {
                    is State.DataState<*> -> {
                        val data = stateList.data as? List<CustomPlace>

                        if (data != null) {
                            _state.update {
                                it.copy(listOfPredictions = data)
                            }
                        } else {
                            Log.e("CATLIFE", "Failed to cast data to List<CustomPlace>")
                        }
                    }

                    is State.ErrorState -> Log.e(
                        "CATLIFE",
                        "Error in getPredictionsResponse - ${stateList.exception.message}"
                    )

                    is State.LoadingState -> {
                        Unit
                    }
                }
            }
        }
    }

    private fun getPredictionsResponse(address: String) = flow {
        emit(State.LoadingState)
        try {
            delay(300)
            emit(State.DataState(fetchPlaceUseCase(address)))
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(exception.resolveError())
        }
    }

    private suspend fun getPlaceDetails(placeId: String, token: AutocompleteSessionToken) {
        withContext(ioDispatcher) {
            fetchPlaceUseCase.getPlaceLatLng(placeId, token) { latLng ->
                _state.update {
                    it.copy(
                        currentEventPlaceLat = latLng.latitude,
                        currentEventPlaceLng = latLng.longitude
                    )
                }
            }
        }
    }
}
