package com.msoula.catlife.feature_inventory.domain.use_case.crud

import com.msoula.catlife.core.util.AddEditInventoryItemError
import com.msoula.catlife.core.util.Constant
import com.msoula.catlife.core.util.Resource
import com.msoula.catlife.feature_inventory.domain.InventoryDataSource

class InsertInventoryItemUseCase(private val inventoryDataSource: InventoryDataSource) {

    suspend operator fun invoke(label: String, quantity: Int, id: Long?): Resource<String> {
        return try {
            inventoryDataSource.insertInventoryItem(label, quantity, id)
            Resource.Success(Constant.INVENTORY_ITEM_ADDED_UPDATED)
        } catch (error: AddEditInventoryItemError) {
            error.printStackTrace()
            Resource.Error(error)
        }
    }
}
