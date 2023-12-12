package com.msoula.catlife.feature_inventory.data.state

data class InventoryState(
    val openDeleteAlert: Boolean = false,
    val itemId: Int = -1
)
