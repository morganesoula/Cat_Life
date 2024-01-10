package com.msoula.catlife.core.preview

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.msoula.catlife.R
import com.msoula.catlife.core.data.CatDataSource
import com.msoula.catlife.core.domain.use_case.crud.CatUseCases
import com.msoula.catlife.core.domain.use_case.crud.DeleteCatUseCase
import com.msoula.catlife.core.domain.use_case.crud.FetchLastInsertedCatIdUseCase
import com.msoula.catlife.core.domain.use_case.crud.GetAllCatsUseCase
import com.msoula.catlife.core.domain.use_case.crud.GetCatUseCase
import com.msoula.catlife.core.domain.use_case.crud.InsertCatUseCase
import com.msoula.catlife.extension.getAllRaces
import com.msoula.catlife.feature_add_edit_cat.presentation.AddEditCatFormEvent
import com.msoula.catlife.feature_add_edit_cat.presentation.AddEditCatViewModel
import com.msoula.catlife.feature_add_edit_cat.presentation.screen.AddEditCatFormScreen
import com.msoula.catlife.feature_calendar.data.repository.CalendarEventDataSource
import com.msoula.catlife.feature_calendar.data.state.AddEditEventFormEvent
import com.msoula.catlife.feature_calendar.data.state.CalendarFeedUiState
import com.msoula.catlife.feature_calendar.domain.use_case.CalendarEventUseCases
import com.msoula.catlife.feature_calendar.domain.use_case.FetchLastInsertedEventIdUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.DeleteCalendarEventByIdUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetAllEventUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetCalendarEventByIdUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetCalendarEventsOnDateDUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetNextEventsUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.InsertCalendarEventUseCase
import com.msoula.catlife.feature_calendar.presentation.AddEditEventViewModel
import com.msoula.catlife.feature_calendar.presentation.CalendarViewModel
import com.msoula.catlife.feature_calendar.presentation.screen.AddEditEventFormScreen
import com.msoula.catlife.feature_calendar.presentation.screen.CalendarScreen
import com.msoula.catlife.feature_calendar.presentation.screen.previewEventsFeedStateWithData
import com.msoula.catlife.feature_cat_detail.presentation.CatDetailViewModel
import com.msoula.catlife.feature_cat_detail.presentation.screen.CatDetailScreen
import com.msoula.catlife.feature_event_detail.presentation.CustomEventDetailViewModel
import com.msoula.catlife.feature_event_detail.presentation.screen.CustomEventDetailScreen
import com.msoula.catlife.feature_inventory.data.state.InventoryItemsFeedUiState
import com.msoula.catlife.feature_inventory.presentation.AddEditInventoryItemViewModel
import com.msoula.catlife.feature_inventory.presentation.screen.AddEditInventoryItemFormScreen
import com.msoula.catlife.feature_main.data.state.CatsFeedUiState
import com.msoula.catlife.feature_note.data.repository.NoteDataSource
import com.msoula.catlife.feature_note.data.state.CatForNoteUiState
import com.msoula.catlife.feature_note.domain.use_case.crud.CrudNoteUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.DeleteNoteByCatIdUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.DeleteNoteByIdUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.FetchLastInsertedNoteIdUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.GetAllCatNotesUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.GetAllNotesUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.GetNoteByIdUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.InsertNoteUseCase
import com.msoula.catlife.feature_note.presentation.AddEditNoteViewModel
import com.msoula.catlife.feature_note.presentation.screen.AddEditNoteFormScreen
import com.msoula.catlife.feature_note_detail.presentation.NoteDetailViewModel
import com.msoula.catlife.feature_note_detail.presentation.screen.NoteDetailScreen
import com.msoula.catlife.globalCurrentDay
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import commsoulacatlifedatabase.CatEntity
import commsoulacatlifedatabase.CustomEventEntity
import commsoulacatlifedatabase.InventoryItemEntity
import commsoulacatlifedatabase.Note
import kotlinx.coroutines.Dispatchers

@Composable
fun PreviewAddEditInventoryItemFormScreen() {
    val viewModel = hiltViewModel<AddEditInventoryItemViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle()

    AddEditInventoryItemFormScreen(
        state = state.value,
        navigator = EmptyDestinationsNavigator,
        onLifecycleEvent = viewModel::onLifecycleEvent,
        onUiEvent = viewModel::onUiEvent
    )
}

@Composable
fun PreviewAddEditNoteFormScreen() {
    val viewModel = hiltViewModel<AddEditNoteViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle()
    val catsState = CatForNoteUiState.Success(cats())

    AddEditNoteFormScreen(
        state = state.value,
        catState = catsState,
        onUiEvent = viewModel::onUiEvent,
        onLifecycleEvent = viewModel::onLifecycleEvent,
        onNoteAddedOrUpdated = { },
        navigator = EmptyDestinationsNavigator
    )
}

@Composable
fun PreviewAddEditCatFormScreen() {
    val viewModel = hiltViewModel<AddEditCatViewModel>()
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()

    AddEditCatFormScreen(
        state = state.value,
        onUiEvent = viewModel::onUiEvent,
        onDateEvent = viewModel::onDateEvent,
        onDeleteDateForEvent = {},
        onClearClick = {},
        onLifecycleEvent = viewModel::onLifecycleEvent,
        initRaces = { context.getAllRaces() },
        onCatAddedOrUpdated = {},
        navigator = EmptyDestinationsNavigator
    )

    viewModel.onUiEvent(
        AddEditCatFormEvent.OnEditCatProfilePicturePathChanged(Uri.parse("android.resource://" + context.packageName + "/" + R.drawable.catlife_add_cat)),
    ) {}
}

@Composable
fun PreviewAddEditEventFormScreen() {
    val viewModel = hiltViewModel<AddEditEventViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle()

    AddEditEventFormScreen(
        state = state.value,
        onUiEvent = viewModel::onUiEvent,
        onLifecycleEvent = viewModel::onLifecycleEvent,
        onEventAddedOrUpdated = {},
        navigator = EmptyDestinationsNavigator
    )

    viewModel.onUiEvent(AddEditEventFormEvent.OnEventPlaceChanged("1 rue des Charmes 35240 Retiers")) {}
}

@Composable
fun PreviewCalendarScreen() {
    val context = LocalContext.current
    val viewModel = hiltViewModel<CalendarViewModel>()
    val state = viewModel.calendarUiState.collectAsStateWithLifecycle().value

    CalendarScreen(
        calendarUiState = state,
        eventsState = previewEventsFeedStateWithData(),
        onUiEvent = viewModel::onUiEvent,
        navController = NavController(context),
        navigator = EmptyDestinationsNavigator
    )
}

@Composable
fun PreviewNoteDetailScreen(noteDataSource: NoteDataSource) {
    val savedStateHandle = SavedStateHandle(mapOf("noteDetailId" to 1))
    val viewModel = NoteDetailViewModel(
        savedStateHandle = savedStateHandle,
        CrudNoteUseCase(
            InsertNoteUseCase(noteDataSource),
            GetNoteByIdUseCase(noteDataSource),
            GetAllNotesUseCase(noteDataSource),
            GetAllCatNotesUseCase(noteDataSource),
            DeleteNoteByIdUseCase(noteDataSource),
            DeleteNoteByCatIdUseCase(noteDataSource),
            FetchLastInsertedNoteIdUseCase(noteDataSource)
        ),
        ioDispatcher = Dispatchers.IO
    )

    val state = viewModel.state.collectAsStateWithLifecycle().value

    NoteDetailScreen(
        state = state,
        goBackToListScreen = {},
        onUiAction = viewModel::onElementActionEvent,
        navigator = EmptyDestinationsNavigator
    )
}

@Composable
fun PreviewCatDetailScreen(catDataSource: CatDataSource, noteDataSource: NoteDataSource) {
    val savedStateHandle = SavedStateHandle(mapOf("catDetailId" to 1))
    val viewModel = CatDetailViewModel(
        savedStateHandle = savedStateHandle,
        catUseCases = CatUseCases(
            InsertCatUseCase(catDataSource),
            DeleteCatUseCase(catDataSource),
            GetAllCatsUseCase(catDataSource),
            GetCatUseCase(catDataSource),
            FetchLastInsertedCatIdUseCase(catDataSource)
        ),
        noteUseCase = CrudNoteUseCase(
            InsertNoteUseCase(noteDataSource),
            GetNoteByIdUseCase(noteDataSource),
            GetAllNotesUseCase(noteDataSource),
            GetAllCatNotesUseCase(noteDataSource),
            DeleteNoteByIdUseCase(noteDataSource),
            DeleteNoteByCatIdUseCase(noteDataSource),
            FetchLastInsertedNoteIdUseCase(noteDataSource)
        ),
        Dispatchers.IO
    )

    val state = viewModel.state.collectAsStateWithLifecycle().value

    CatDetailScreen(
        state = state,
        onUiAction = viewModel::onElementActionEvent,
        navigator = EmptyDestinationsNavigator,
        goBackToListScreen = {}
    )
}

@Composable
fun PreviewEventDetailScreen(calendarEventDataSource: CalendarEventDataSource) {
    val savedStateHandle = SavedStateHandle(mapOf("eventDetailId" to 1))
    val viewModel = CustomEventDetailViewModel(
        savedStateHandle = savedStateHandle,
        customEventUseCases = CalendarEventUseCases(
            InsertCalendarEventUseCase(calendarEventDataSource),
            GetCalendarEventsOnDateDUseCase(calendarEventDataSource),
            GetCalendarEventByIdUseCase(calendarEventDataSource),
            DeleteCalendarEventByIdUseCase(calendarEventDataSource),
            GetNextEventsUseCase(calendarEventDataSource),
            GetAllEventUseCase(calendarEventDataSource),
            FetchLastInsertedEventIdUseCase(calendarEventDataSource)
        ),
        ioDispatcher = Dispatchers.IO
    )

    val state = viewModel.state.collectAsStateWithLifecycle().value

    CustomEventDetailScreen(
        state = state,
        onUiAction = viewModel::onUiEvent,
        navigateToEditEventForm = {},
        goBackToListScreen = {},
        navigator = EmptyDestinationsNavigator
    )
}

fun cats() = listOf(
    CatEntity(
        1,
        "catTest1",
        "",
        gender = true,
        neutered = true,
        1680300000000L,
        4.0f,
        "Siamese",
        "Snowshoe",
        "",
        null,
        null,
        null
    ),
    CatEntity(
        2,
        "catTest2",
        "",
        gender = true,
        neutered = false,
        1677279600000L,
        2.5f,
        "Persan",
        "Black",
        "",
        null,
        null,
        null
    )
)

fun previewCatsFeedStateEmpty(): CatsFeedUiState =
    CatsFeedUiState.Empty

fun previewCatsFeedStateWithData() =
    CatsFeedUiState.Success(
        cats()
    )

fun note(): Note = Note(
    1,
    1,
    "",
    "catTest1",
    globalCurrentDay,
    "test note 1",
    "random test 1 description"
)

fun event(): CustomEventEntity = CustomEventEntity(
    1,
    "eventTest1",
    "random description event 1",
    "3 allée des glénans, 35150 Janze",
    "47.95235959861251",
    "-1.4946459452566867",
    startDate = globalCurrentDay,
    endDate = globalCurrentDay,
    startTime = "00:00",
    endTime = "00:00",
    allDay = true
)

fun previewInventoryFeedStateEmpty() =
    InventoryItemsFeedUiState.Empty

fun previewInventoryFeedStateWithData() =
    InventoryItemsFeedUiState.Success(
        listOf(
            InventoryItemEntity(1, "Croquettes", 0),
            InventoryItemEntity(2, "Jouet", 1)
        )
    )

fun previewEventFeedStateEmpty() = CalendarFeedUiState.Empty