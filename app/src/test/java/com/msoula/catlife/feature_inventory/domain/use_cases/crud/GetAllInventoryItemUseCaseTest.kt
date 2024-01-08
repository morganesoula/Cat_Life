package com.msoula.catlife.feature_inventory.domain.use_cases.crud

import com.msoula.catlife.feature_inventory.data.repository.FakeInventoryItemRepository
import com.msoula.catlife.feature_inventory.domain.use_case.crud.GetAllInventoryItemUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.InsertInventoryItemUseCase
import commsoulacatlifedatabase.InventoryItemEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetAllInventoryItemUseCaseTest {

    private lateinit var insertInventoryItemUseCase: InsertInventoryItemUseCase
    private lateinit var fakeInventoryItemRepository: FakeInventoryItemRepository
    private lateinit var getAllInventoryItemUseCase: GetAllInventoryItemUseCase

    @Before
    fun setUp() {
        fakeInventoryItemRepository = FakeInventoryItemRepository()
        insertInventoryItemUseCase = InsertInventoryItemUseCase(fakeInventoryItemRepository)
        getAllInventoryItemUseCase = GetAllInventoryItemUseCase(fakeInventoryItemRepository)
    }

    @Test
    fun `Insert many items and fetch all`() {
        val listItem = listOf(
            InventoryItemEntity(
                1, "test1", 1
            ),
            InventoryItemEntity(
                2, "test2", 2
            ),
            InventoryItemEntity(
                3, "test3", 3
            ),
            InventoryItemEntity(
                4, "test4", 4
            )
        )

        runTest {
            for (item in listItem) {
                insertInventoryItemUseCase.invoke(item.label, item.quantity, item.id)
            }

            val itemsInStore = getAllInventoryItemUseCase.invoke()

            assertTrue(itemsInStore.toList().isNotEmpty())
            assertTrue(itemsInStore.first().size == 4)
        }
    }
}
