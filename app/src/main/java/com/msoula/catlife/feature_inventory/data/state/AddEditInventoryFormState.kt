package com.msoula.catlife.feature_inventory.data.state

import com.msoula.catlife.R

data class AddEditInventoryFormState(
    val currentLabel: String = "",
    val currentLabelError: Int? = null,
    val currentQuantity: String = "",
    val currentQuantityError: Int? = null,
    val addEditComposableTitle: Int = R.string.add_new_inventory_item_title,
    val enableSubmit: Boolean = false,
    val inventoryItemAdded: Boolean = false
)
