package com.msoula.catlife.feature_inventory.domain.use_cases.crud

import com.msoula.catlife.feature_inventory.data.repository.FakeInventoryItemRepository
import com.msoula.catlife.feature_inventory.domain.use_case.crud.GetInventoryItemByIdUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.InsertInventoryItemUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetInventoryItemByIdUseCaseTest {

    private lateinit var fakeInventoryItemRepository: FakeInventoryItemRepository
    private lateinit var insertInventoryItemUseCase: InsertInventoryItemUseCase
    private lateinit var getInventoryItemByIdUseCase: GetInventoryItemByIdUseCase

    @Before
    fun setUp() {
        fakeInventoryItemRepository = FakeInventoryItemRepository()
        insertInventoryItemUseCase = InsertInventoryItemUseCase(fakeInventoryItemRepository)
        getInventoryItemByIdUseCase = GetInventoryItemByIdUseCase(fakeInventoryItemRepository)
    }

    @Test
    fun `Insert and fetch by ID`() = runTest {
        insertInventoryItemUseCase.invoke(id = 1, label = "testItem", quantity = 56)
        val fetchedItem = getInventoryItemByIdUseCase.invoke(1)

        assertTrue(fetchedItem.data?.label == "testItem")
        assertFalse(fetchedItem.data?.quantity == 12)
    }
}
