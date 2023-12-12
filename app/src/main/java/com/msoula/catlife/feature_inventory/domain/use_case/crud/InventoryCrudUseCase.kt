package com.msoula.catlife.feature_inventory.domain.use_case.crud

data class InventoryCrudUseCase(
    val insertInventoryItem: InsertInventoryItemUseCase,
    val deleteInventoryItemById: DeleteInventoryItemByIdUseCase,
    val getAllInventoryItem: GetAllInventoryItemUseCase,
    val getInventoryItemById: GetInventoryItemByIdUseCase,
    val updateInventoryItemQuantity: UpdateInventoryItemQuantityUseCase
)
