package com.msoula.catlife.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.msoula.catlife.NavGraphs
import com.msoula.catlife.destinations.AddEditCatFormScreenDestination
import com.msoula.catlife.destinations.AddEditEventFormScreenDestination
import com.msoula.catlife.destinations.AddEditInventoryItemFormScreenDestination
import com.msoula.catlife.destinations.AddEditNoteFormScreenDestination
import com.msoula.catlife.destinations.CalendarScreenDestination
import com.msoula.catlife.destinations.CatDetailScreenDestination
import com.msoula.catlife.destinations.CustomEventDetailScreenDestination
import com.msoula.catlife.destinations.HomeScreenDestination
import com.msoula.catlife.destinations.InventoryScreenDestination
import com.msoula.catlife.destinations.NoteDetailScreenDestination
import com.msoula.catlife.destinations.NotesScreenDestination
import com.msoula.catlife.feature_add_edit_cat.presentation.AddEditCatFormEvent
import com.msoula.catlife.feature_add_edit_cat.presentation.AddEditCatViewModel
import com.msoula.catlife.feature_add_edit_cat.presentation.screen.AddEditCatFormScreen
import com.msoula.catlife.feature_calendar.presentation.AddEditEventViewModel
import com.msoula.catlife.feature_calendar.presentation.CalendarViewModel
import com.msoula.catlife.feature_calendar.presentation.screen.AddEditEventFormScreen
import com.msoula.catlife.feature_calendar.presentation.screen.CalendarScreen
import com.msoula.catlife.feature_cat_detail.presentation.CatDetailViewModel
import com.msoula.catlife.feature_cat_detail.presentation.screen.CatDetailScreen
import com.msoula.catlife.feature_event_detail.presentation.CustomEventDetailViewModel
import com.msoula.catlife.feature_event_detail.presentation.screen.CustomEventDetailScreen
import com.msoula.catlife.feature_inventory.presentation.AddEditInventoryItemViewModel
import com.msoula.catlife.feature_inventory.presentation.InventoryViewModel
import com.msoula.catlife.feature_inventory.presentation.screen.AddEditInventoryItemFormScreen
import com.msoula.catlife.feature_inventory.presentation.screen.InventoryScreen
import com.msoula.catlife.feature_main.presentation.HomeViewModel
import com.msoula.catlife.feature_main.presentation.screen.HomeScreen
import com.msoula.catlife.feature_note.presentation.AddEditNoteViewModel
import com.msoula.catlife.feature_note.presentation.NotesViewModel
import com.msoula.catlife.feature_note.presentation.screen.AddEditNoteFormScreen
import com.msoula.catlife.feature_note.presentation.screen.NotesScreen
import com.msoula.catlife.feature_note_detail.presentation.NoteDetailViewModel
import com.msoula.catlife.feature_note_detail.presentation.screen.NoteDetailScreen
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable

@Composable
fun DestinationsNavHostSetUp(navController: NavHostController) {
    val homeViewModel = hiltViewModel<HomeViewModel>()

    DestinationsNavHost(navGraph = NavGraphs.root, navController = navController) {
        composable(HomeScreenDestination) {
            HomeScreen(
                catState = homeViewModel.catState.collectAsStateWithLifecycle().value,
                inventoryState = homeViewModel.inventoryItemState.collectAsStateWithLifecycle().value,
                eventsState = homeViewModel.eventState.collectAsStateWithLifecycle().value,
                navigator = this.destinationsNavigator,
                navController = navController
            )
        }

        composable(AddEditCatFormScreenDestination) {
            val addEditCatViewModel = hiltViewModel<AddEditCatViewModel>()

            AddEditCatFormScreen(
                state = addEditCatViewModel.state.collectAsStateWithLifecycle().value,
                onUiEvent = addEditCatViewModel::onUiEvent,
                onDateEvent = addEditCatViewModel::onDateEvent,
                onDeleteDateForEvent = addEditCatViewModel::onClearDateEvent,
                onClearClick = { lambda ->
                    addEditCatViewModel.onUiEvent(
                        AddEditCatFormEvent.OnClearClick,
                        lambda
                    )
                },
                onLifecycleEvent = addEditCatViewModel::onLifecycleEvent,
                initRaces = addEditCatViewModel::initRacesList,
                onCatAddedOrUpdated = { id ->
                    if (id != -1) {
                        this.destinationsNavigator.navigate(
                            CatDetailScreenDestination(
                                catDetailId = id!!
                            )
                        ) {
                            popUpTo(HomeScreenDestination.route)
                        }
                    } else {
                        this.destinationsNavigator.navigate(HomeScreenDestination)
                    }
                },
                navigator = this.destinationsNavigator
            )
        }

        composable(CatDetailScreenDestination) {
            val catDetailViewModel = hiltViewModel<CatDetailViewModel>()

            CatDetailScreen(
                state = catDetailViewModel.state.collectAsStateWithLifecycle().value,
                onUiAction = { event, goBackToListScreen ->
                    catDetailViewModel.onElementActionEvent(event, goBackToListScreen)
                },
                navigator = this.destinationsNavigator,
                goBackToListScreen = {
                    this.destinationsNavigator.popBackStack(
                        HomeScreenDestination.route,
                        inclusive = false
                    )
                }
            )
        }

        composable(CalendarScreenDestination) {
            val calendarViewModel = hiltViewModel<CalendarViewModel>()

            CalendarScreen(
                calendarUiState = calendarViewModel.calendarUiState.collectAsStateWithLifecycle().value,
                eventsState = calendarViewModel.eventsState.collectAsStateWithLifecycle().value,
                onUiEvent = calendarViewModel::onUiEvent,
                navController = navController,
                navigator = this.destinationsNavigator
            )
        }

        composable(AddEditEventFormScreenDestination) {
            val addEditEventViewModel = hiltViewModel<AddEditEventViewModel>()

            AddEditEventFormScreen(
                state = addEditEventViewModel.state.collectAsStateWithLifecycle().value,
                onUiEvent = addEditEventViewModel::onUiEvent,
                onLifecycleEvent = addEditEventViewModel::onLifecycleEvent,
                onEventAddedOrUpdated = { id ->
                    if (id != -1) {
                        this.destinationsNavigator.navigate(
                            CustomEventDetailScreenDestination(eventDetailId = id!!)
                        ) {
                            popUpTo(CalendarScreenDestination(-1L).route)
                        }
                    } else {
                        this.destinationsNavigator.navigate(HomeScreenDestination)
                    }
                },
                navigator = this.destinationsNavigator
            )
        }

        composable(CustomEventDetailScreenDestination) {
            val eventDetailViewModel = hiltViewModel<CustomEventDetailViewModel>()

            CustomEventDetailScreen(
                state = eventDetailViewModel.state.collectAsStateWithLifecycle().value,
                onUiAction = eventDetailViewModel::onUiEvent,
                navigateToEditEventForm = { id ->
                    if (id != -1) {
                        this.destinationsNavigator.navigate(
                            AddEditEventFormScreenDestination(eventUpdateId = id)
                        )
                    } else {
                        this.destinationsNavigator.navigate(HomeScreenDestination)
                    }
                },
                navigator = this.destinationsNavigator,
                goBackToListScreen = {
                    this.destinationsNavigator.navigate(CalendarScreenDestination.route) {
                        popUpTo(HomeScreenDestination.route)
                    }
                }
            )
        }

        composable(NotesScreenDestination) {
            val noteViewModel = hiltViewModel<NotesViewModel>()

            NotesScreen(
                catState = homeViewModel.catState.collectAsStateWithLifecycle().value,
                noteState = noteViewModel.noteState.collectAsStateWithLifecycle().value,
                noteUiState = noteViewModel.noteUiState.collectAsStateWithLifecycle().value,
                navController = navController,
                onUiAction = { event, goBackToListScreen ->
                    noteViewModel.onElementActionEvent(
                        event,
                        goBackToListScreen
                    )
                },
                navigator = this.destinationsNavigator,
                goBackToListScreen = {
                    this.destinationsNavigator.navigate(NotesScreenDestination.route) {
                        popUpTo(HomeScreenDestination.route)
                    }
                }
            )
        }

        composable(AddEditNoteFormScreenDestination) {
            val addEditNoteViewModel = hiltViewModel<AddEditNoteViewModel>()

            AddEditNoteFormScreen(
                state = addEditNoteViewModel.state.collectAsStateWithLifecycle().value,
                onUiEvent = addEditNoteViewModel::onUiEvent,
                onLifecycleEvent = addEditNoteViewModel::onLifecycleEvent,
                catState = addEditNoteViewModel.cats.collectAsStateWithLifecycle().value,
                onNoteAddedOrUpdated = { id ->
                    if (id != -1) {
                        this.destinationsNavigator.navigate(
                            NoteDetailScreenDestination(
                                noteDetailId = id!!
                            )
                        ) {
                            popUpTo(NotesScreenDestination.route)
                        }
                    } else {
                        this.destinationsNavigator.navigate(HomeScreenDestination)
                    }
                },
                navigator = this.destinationsNavigator
            )
        }

        composable(NoteDetailScreenDestination) {
            val noteDetailViewModel = hiltViewModel<NoteDetailViewModel>()

            NoteDetailScreen(
                state = noteDetailViewModel.state.collectAsStateWithLifecycle().value,
                onUiAction = { event, goBackToListScreen ->
                    noteDetailViewModel.onElementActionEvent(
                        event,
                        goBackToListScreen
                    )
                },
                navigator = this.destinationsNavigator,
                goBackToListScreen = {
                    this.destinationsNavigator.navigate(NotesScreenDestination.route) {
                        popUpTo(HomeScreenDestination.route)
                    }
                }
            )
        }

        composable(InventoryScreenDestination) {
            val inventoryViewModel = hiltViewModel<InventoryViewModel>()

            InventoryScreen(
                state = inventoryViewModel.inventoryItemState.collectAsStateWithLifecycle().value,
                inventoryUiState = inventoryViewModel.inventoryUiState.collectAsStateWithLifecycle().value,
                onUiActionEvent = inventoryViewModel::onElementActionEvent,
                onUpdateItemQuantity = { itemId, increment ->
                    inventoryViewModel.updateItemWithQuantity(
                        itemId,
                        increment
                    )
                },
                navController = navController,
                navigator = this.destinationsNavigator
            )
        }

        composable(AddEditInventoryItemFormScreenDestination) {
            val addEditItemViewModel = hiltViewModel<AddEditInventoryItemViewModel>()

            AddEditInventoryItemFormScreen(
                state = addEditItemViewModel.state.collectAsStateWithLifecycle().value,
                onUiEvent = addEditItemViewModel::onUiEvent,
                onLifecycleEvent = addEditItemViewModel::onLifecycleEvent,
                navigator = this.destinationsNavigator
            )
        }
    }
}