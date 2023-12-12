package com.msoula.catlife.feature_inventory.domain.use_case.crud

import com.msoula.catlife.core.util.Constant
import com.msoula.catlife.core.util.DeleteItemError
import com.msoula.catlife.core.util.Resource
import com.msoula.catlife.feature_inventory.domain.InventoryDataSource

class DeleteInventoryItemByIdUseCase(private val inventoryDataSource: InventoryDataSource) {

    suspend operator fun invoke(id: Int): Resource<String> {
        return try {
            inventoryDataSource.deleteInventoryItemById(id)
            Resource.Success(Constant.ITEM_DELETED)
        } catch (e: DeleteItemError) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }
}