package com.msoula.catlife.feature_inventory.domain.use_case.crud

import com.msoula.catlife.core.util.AddEditInventoryItemError
import com.msoula.catlife.core.util.Constant
import com.msoula.catlife.core.util.Resource
import com.msoula.catlife.feature_inventory.domain.InventoryDataSource

class UpdateInventoryItemQuantityUseCase(private val inventoryDataSource: InventoryDataSource) {

    suspend operator fun invoke(itemId: Int, quantity: Int): Resource<String> {
        return try {
            inventoryDataSource.updateInventoryItemQuantity(itemId, quantity)
            Resource.Success(Constant.INVENTORY_ITEM_ADDED_UPDATED)
        } catch (error: AddEditInventoryItemError) {
            error.printStackTrace()
            Resource.Error(error)
        }
    }
}