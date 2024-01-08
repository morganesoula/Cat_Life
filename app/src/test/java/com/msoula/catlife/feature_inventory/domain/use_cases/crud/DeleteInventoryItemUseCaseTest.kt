package com.msoula.catlife.feature_inventory.domain.use_cases.crud

import com.msoula.catlife.feature_inventory.data.repository.FakeInventoryItemRepository
import com.msoula.catlife.feature_inventory.domain.use_case.crud.DeleteInventoryItemByIdUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.GetAllInventoryItemUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.GetInventoryItemByIdUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.InsertInventoryItemUseCase
import commsoulacatlifedatabase.InventoryItemEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeleteInventoryItemUseCaseTest {

    private lateinit var getInventoryItemUseCase: GetInventoryItemByIdUseCase
    private lateinit var insertInventoryItemUseCase: InsertInventoryItemUseCase
    private lateinit var fakeInventoryItemRepository: FakeInventoryItemRepository
    private lateinit var getAllInventoryItemUseCase: GetAllInventoryItemUseCase
    private lateinit var deleteInventoryItemUseCase: DeleteInventoryItemByIdUseCase

    @Before
    fun setUp() {
        fakeInventoryItemRepository = FakeInventoryItemRepository()
        getInventoryItemUseCase = GetInventoryItemByIdUseCase(fakeInventoryItemRepository)
        insertInventoryItemUseCase = InsertInventoryItemUseCase(fakeInventoryItemRepository)
        getAllInventoryItemUseCase = GetAllInventoryItemUseCase(fakeInventoryItemRepository)
        deleteInventoryItemUseCase = DeleteInventoryItemByIdUseCase(fakeInventoryItemRepository)
    }

    @Test
    fun `Insert, fetch one, delete, should not exist anymore`() = runTest {
        val itemToDelete = InventoryItemEntity(
            id = 1,
            label = "testItem",
            quantity = 34
        )

        insertInventoryItemUseCase.invoke(itemToDelete.label, itemToDelete.quantity, itemToDelete.id)
        val fetchedItem = getInventoryItemUseCase.invoke(itemToDelete.id.toInt())
        var itemsInStore = getAllInventoryItemUseCase.invoke()

        assertTrue(fetchedItem.data?.label.equals("testItem"))
        assertTrue(itemsInStore.first().size == 1)

        deleteInventoryItemUseCase.invoke(itemToDelete.id.toInt())
        itemsInStore = getAllInventoryItemUseCase.invoke()

        assertTrue(itemsInStore.first().isEmpty())
    }
}
