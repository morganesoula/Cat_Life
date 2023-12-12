package com.msoula.catlife.feature_inventory.data.state

import commsoulacatlifedatabase.InventoryItemEntity


sealed interface InventoryItemsFeedUiState {
    data object Loading : InventoryItemsFeedUiState
    data object Empty : InventoryItemsFeedUiState
    data class Success(val inventoryItemsFeed: List<InventoryItemEntity>) : InventoryItemsFeedUiState
}