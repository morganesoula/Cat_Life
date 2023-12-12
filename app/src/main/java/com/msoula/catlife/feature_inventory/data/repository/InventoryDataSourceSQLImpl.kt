package com.msoula.catlife.feature_inventory.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.msoula.CatLifeDatabase
import com.msoula.catlife.feature_inventory.domain.InventoryDataSource
import commsoulacatlifedatabase.InventoryItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class InventoryDataSourceSQLImpl(
    db: CatLifeDatabase
) : InventoryDataSource {

    private val queries = db.inventoryQueries

    override fun getAllInventoryItems(): Flow<List<InventoryItemEntity>> {
        return queries.getAllInventoryItem().asFlow().mapToList(Dispatchers.IO)
    }

    override fun getInventoryItemsWithLowQuantity(): Flow<List<InventoryItemEntity>> {
        return queries.getInventoryItemsWithLowQuantity().asFlow().mapToList(Dispatchers.IO)
    }

    override suspend fun insertInventoryItem(label: String, quantity: Int, id: Long?) {
        withContext(Dispatchers.IO) {
            queries.insertInventoryItem(
                id = id,
                label = label,
                quantity = quantity
            )
        }
    }

    override suspend fun deleteInventoryItemById(id: Int) {
        withContext(Dispatchers.IO) {
            queries.deleteInventoryItemById(id.toLong())
        }
    }

    override suspend fun getInventoryItemById(itemId: Int): InventoryItemEntity? {
        return withContext(Dispatchers.IO) {
            queries.getInventoryItemById(itemId.toLong()).executeAsOneOrNull()
        }
    }

    override suspend fun updateInventoryItemQuantity(itemId: Int, quantity: Int) {
        withContext(Dispatchers.IO) {
            queries.updateInventoryItemQuantity(id = itemId.toLong(), quantity = quantity)
        }
    }
}