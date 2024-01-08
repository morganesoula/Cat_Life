package com.msoula.catlife.feature_inventory.domain.use_cases.crud

import com.msoula.catlife.feature_inventory.data.repository.FakeInventoryItemRepository
import com.msoula.catlife.feature_inventory.domain.use_case.crud.GetAllInventoryItemUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.InsertInventoryItemUseCase
import commsoulacatlifedatabase.InventoryItemEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class InsertInventoryItemUseCaseTest {

    private lateinit var fakeInventoryItemRepository: FakeInventoryItemRepository
    private lateinit var insertInventoryItemUseCase: InsertInventoryItemUseCase
    private lateinit var getAllInventoryItemUseCase: GetAllInventoryItemUseCase

    @Before
    fun setUp() {
        fakeInventoryItemRepository = FakeInventoryItemRepository()
        insertInventoryItemUseCase = InsertInventoryItemUseCase(fakeInventoryItemRepository)
        getAllInventoryItemUseCase = GetAllInventoryItemUseCase(fakeInventoryItemRepository)
    }

    @Test
    fun `Insert and fetch`() = runTest {
        val listOfItems = listOf(
            InventoryItemEntity(
                1, "test1", 1
            ),
            InventoryItemEntity(
                2, "test2", 2
            )
        )

        for (item in listOfItems) {
            insertInventoryItemUseCase.invoke(item.label, item.quantity, item.id)
        }

        val fetchedItems = getAllInventoryItemUseCase.invoke()

        assertTrue(fetchedItems.first().size == 2)
    }
}
