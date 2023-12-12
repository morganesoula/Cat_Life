package com.msoula.catlife.feature_inventory.domain.use_case.crud

import com.msoula.catlife.core.util.GetItemByIdError
import com.msoula.catlife.core.util.Resource
import com.msoula.catlife.feature_inventory.domain.InventoryDataSource
import commsoulacatlifedatabase.InventoryItemEntity

class GetInventoryItemByIdUseCase(private val inventoryDataSource: InventoryDataSource) {

    suspend operator fun invoke(inventoryItemId: Int): Resource<InventoryItemEntity?> {
        return try {
            val item = inventoryDataSource.getInventoryItemById(inventoryItemId)
            Resource.Success(item)
        } catch (e: GetItemByIdError) {
            Resource.Error(e)
        }
    }
}
