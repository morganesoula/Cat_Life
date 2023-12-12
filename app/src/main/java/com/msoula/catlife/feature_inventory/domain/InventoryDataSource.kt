package com.msoula.catlife.feature_inventory.domain

import commsoulacatlifedatabase.InventoryItemEntity
import kotlinx.coroutines.flow.Flow

interface InventoryDataSource {
    fun getAllInventoryItems(): Flow<List<InventoryItemEntity>>
    fun getInventoryItemsWithLowQuantity(): Flow<List<InventoryItemEntity>>
    suspend fun insertInventoryItem(label: String, quantity: Int, id: Long?)
    suspend fun deleteInventoryItemById(id: Int)
    suspend fun getInventoryItemById(itemId: Int): InventoryItemEntity?
    suspend fun updateInventoryItemQuantity(itemId: Int, quantity: Int)
}
