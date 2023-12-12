package com.msoula.catlife.feature_inventory.presentation

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msoula.catlife.core.util.Resource
import com.msoula.catlife.di.DispatcherModule
import com.msoula.catlife.feature_inventory.data.state.InventoryItemsFeedUiState
import com.msoula.catlife.feature_inventory.data.state.InventoryState
import com.msoula.catlife.feature_inventory.data.state.UiActionEvent
import com.msoula.catlife.feature_inventory.domain.use_case.crud.InventoryCrudUseCase
import commsoulacatlifedatabase.InventoryItemEntity
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
import printToLog
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val inventoryCrudUseCase: InventoryCrudUseCase,
    @DispatcherModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    var quantity = mutableIntStateOf(0)

    val inventoryItemState: StateFlow<InventoryItemsFeedUiState> =
        inventoryCrudUseCase.getAllInventoryItem()
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

    private val _inventoryUiState = MutableStateFlow(InventoryState())
    val inventoryUiState = _inventoryUiState.asStateFlow()

    fun onElementActionEvent(event: UiActionEvent) {
        when (event) {
            is UiActionEvent.OnDismissRequest -> {
                _inventoryUiState.update { it.copy(openDeleteAlert = false) }
            }

            is UiActionEvent.OpenDeleteAlertDialog -> {
                _inventoryUiState.update {
                    it.copy(openDeleteAlert = true, itemId = event.itemId)
                }
            }

            is UiActionEvent.OnDeleteUi -> {
                if (event.deleteData) {
                    removeDataWithId(event.elementId)
                }

                _inventoryUiState.update {
                    it.copy(openDeleteAlert = false)
                }
            }

            is UiActionEvent.OnSwipeDelete -> removeData(event.item as InventoryItemEntity)
            else -> Unit
        }
    }

    fun updateItemWithQuantity(itemId: Int, isIncrementing: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            when (val result = getInventoryItemById(itemId)) {
                is Resource.Success -> {
                    result.data?.let {
                        quantity.intValue = it.quantity
                        quantity.intValue += if (isIncrementing) 1 else -1
                        updateInventoryItemQuantityInDb(it)
                    }
                }

                is Resource.Error -> {
                    result.throwable?.message.printToLog()
                }
            }
        }
    }

    private fun removeData(item: InventoryItemEntity) {
        viewModelScope.launch {
            when (val result = inventoryCrudUseCase.deleteInventoryItemById.invoke(item.id.toInt())) {
                is Resource.Success -> Unit
                is Resource.Error -> result.throwable?.message.printToLog()
            }
        }
    }

    private fun removeDataWithId(id: Int) {
        viewModelScope.launch {
            when (val result = inventoryCrudUseCase.deleteInventoryItemById.invoke(id)) {
                is Resource.Success -> "CatLife - Element deleted".printToLog()
                is Resource.Error -> result.throwable?.message.printToLog()
            }
        }
    }

    private suspend fun getInventoryItemById(itemId: Int): Resource<InventoryItemEntity?> =
        inventoryCrudUseCase.getInventoryItemById.invoke(itemId)

    private suspend fun updateInventoryItemQuantityInDb(inventoryItem: InventoryItemEntity) =
        inventoryCrudUseCase.updateInventoryItemQuantity.invoke(inventoryItem.id.toInt(), quantity.intValue)
}
