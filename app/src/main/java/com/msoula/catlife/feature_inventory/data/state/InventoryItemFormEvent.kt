package com.msoula.catlife.feature_inventory.data.state

sealed interface InventoryItemFormEvent {

    data class InitLabel(val label: String) :
        InventoryItemFormEvent
    data class InitQuantity(val quantity: String) :
        InventoryItemFormEvent
    data object SubmitElement : InventoryItemFormEvent
}
