package com.msoula.catlife.feature_inventory.presentation

import com.msoula.catlife.feature_inventory.data.repository.FakeInventoryItemRepository
import com.msoula.catlife.feature_inventory.data.state.UiActionEvent
import com.msoula.catlife.feature_inventory.domain.use_case.crud.DeleteInventoryItemByIdUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.GetAllInventoryItemUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.GetInventoryItemByIdUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.InsertInventoryItemUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.InventoryCrudUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.UpdateInventoryItemQuantityUseCase
import com.msoula.catlife.feature_inventory.presentation.util.CoroutineTestRule
import commsoulacatlifedatabase.InventoryItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class InventoryViewModelTest {

    @JvmField
    @Rule
    var mainCoroutineDispatcher = CoroutineTestRule()

    private lateinit var inventoryViewModel: InventoryViewModel
    private lateinit var fakeInventoryItemRepository: FakeInventoryItemRepository

    @Before
    fun setUp() {
        fakeInventoryItemRepository = FakeInventoryItemRepository()

        val initItems = listOf(
            InventoryItemEntity(1, "fakeItem1", 10),
            InventoryItemEntity(2, "fakeItem2", 100),
            InventoryItemEntity(3, "fakeItem3", 20),
            InventoryItemEntity(4, "fakeItem4", 200)
        )

        runTest {
            for (item in initItems) {
                fakeInventoryItemRepository.insertInventoryItem(item.label, item.quantity, item.id)
            }
        }

        inventoryViewModel =
            InventoryViewModel(
                InventoryCrudUseCase(
                    insertInventoryItem = InsertInventoryItemUseCase(fakeInventoryItemRepository),
                    getAllInventoryItem = GetAllInventoryItemUseCase(fakeInventoryItemRepository),
                    getInventoryItemById = GetInventoryItemByIdUseCase(fakeInventoryItemRepository),
                    deleteInventoryItemById = DeleteInventoryItemByIdUseCase(fakeInventoryItemRepository),
                    updateInventoryItemQuantity = UpdateInventoryItemQuantityUseCase(fakeInventoryItemRepository)
                ),
                Dispatchers.IO
            )
    }

    @Test
    fun `remove element on swipe, delete data`() = runTest {
        assertTrue(fakeInventoryItemRepository.getAllInventoryItems().first().size == 4)

        inventoryViewModel.onElementActionEvent(
            UiActionEvent.OnSwipeDelete(
                InventoryItemEntity(
                    1,
                    "fakeItem1",
                    10
                )
            )
        )

        this.advanceUntilIdle()
        assertTrue(fakeInventoryItemRepository.getAllInventoryItems().first().size == 3)
    }

    @Test
    fun `increment quantity of item 1, quantity is 11`() = runTest {
        assertTrue(fakeInventoryItemRepository.getInventoryItemById(1).quantity == 10)

        inventoryViewModel.updateItemWithQuantity(1, true)
        this.advanceUntilIdle()

        withContext(Dispatchers.IO) {
            Thread.sleep(1000)
        }

        assertTrue(fakeInventoryItemRepository.getInventoryItemById(1).quantity == 11)
    }

    @Test
    fun `decrement quantity of item 4, quantity is 199`() = runTest {
        assertTrue(fakeInventoryItemRepository.getInventoryItemById(4).quantity == 200)

        inventoryViewModel.updateItemWithQuantity(4, false)
        this.advanceUntilIdle()

        withContext(Dispatchers.IO) {
            Thread.sleep(1000)
        }

        assertTrue(fakeInventoryItemRepository.getInventoryItemById(4).quantity == 199)
    }
}
