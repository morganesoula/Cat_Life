package com.msoula.catlife.feature_main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msoula.catlife.core.domain.use_case.crud.CatUseCases
import com.msoula.catlife.feature_calendar.data.state.CalendarFeedUiState
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetNextEventsUseCase
import com.msoula.catlife.feature_inventory.data.state.InventoryItemsFeedUiState
import com.msoula.catlife.feature_main.data.state.CatsFeedUiState
import com.msoula.catlife.feature_main.domain.use_case.GetItemsWithLowQuantityUseCase
import com.msoula.catlife.globalCurrentDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    catUseCases: CatUseCases,
    inventoryUseCase: GetItemsWithLowQuantityUseCase,
    calendarUseCase: GetNextEventsUseCase
) : ViewModel() {

    val catState: StateFlow<CatsFeedUiState> = catUseCases.getAllCatsUseCase()
        .map { list -> if (list.isEmpty()) CatsFeedUiState.Empty else CatsFeedUiState.Success(list) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CatsFeedUiState.Loading
        )

    val inventoryItemState: StateFlow<InventoryItemsFeedUiState> = inventoryUseCase()
        .map { list ->
            if (list.isEmpty()) InventoryItemsFeedUiState.Empty else InventoryItemsFeedUiState.Success(
                list
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = InventoryItemsFeedUiState.Loading
        )

    val eventState: StateFlow<CalendarFeedUiState> = calendarUseCase.invoke(globalCurrentDay)
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
}
