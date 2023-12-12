package com.msoula.catlife.feature_main.domain.use_case

import com.msoula.catlife.feature_inventory.domain.InventoryDataSource

class GetItemsWithLowQuantityUseCase(private val inventoryDataSource: InventoryDataSource) {
    operator fun invoke() = inventoryDataSource.getInventoryItemsWithLowQuantity()
}
