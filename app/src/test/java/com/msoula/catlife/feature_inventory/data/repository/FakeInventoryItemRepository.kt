package com.msoula.catlife.feature_inventory.data.repository

import com.msoula.catlife.feature_inventory.domain.InventoryDataSource
import commsoulacatlifedatabase.InventoryItemEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeInventoryItemRepository : InventoryDataSource {

    private val inventoryItems = mutableListOf<InventoryItemEntity>()

    override fun getAllInventoryItems(): Flow<List<InventoryItemEntity>> =
        flow { emit(inventoryItems) }

    override suspend fun insertInventoryItem(label: String, quantity: Int, id: Long?) {
        inventoryItems.add(
            InventoryItemEntity(
                id ?: 0, label, quantity
            )
        )
    }

    override suspend fun updateInventoryItemQuantity(itemId: Int, quantity: Int) {
        val index = inventoryItems.indexOfFirst { it.id == itemId.toLong() }
        val itemToUpdate = inventoryItems.find { it.id == itemId.toLong() }

        itemToUpdate?.let {
            inventoryItems[index] = InventoryItemEntity(it.id, it.label, quantity)
        }
    }

    override suspend fun getInventoryItemById(itemId: Int): InventoryItemEntity =
        inventoryItems.first { it.id == itemId.toLong() }

    override fun getInventoryItemsWithLowQuantity(): Flow<List<InventoryItemEntity>> =
        flow { emit(inventoryItems.filter { it.quantity <= 1 }) }

    override suspend fun deleteInventoryItemById(id: Int) {
        inventoryItems.removeIf { it.id == id.toLong() }
    }
}
