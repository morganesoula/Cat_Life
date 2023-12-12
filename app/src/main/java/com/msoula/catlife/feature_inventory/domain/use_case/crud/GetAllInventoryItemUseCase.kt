package com.msoula.catlife.feature_inventory.domain.use_case.crud

import com.msoula.catlife.feature_inventory.domain.InventoryDataSource

class GetAllInventoryItemUseCase(private val inventoryDataSource: InventoryDataSource) {
    operator fun invoke() = inventoryDataSource.getAllInventoryItems()
}
