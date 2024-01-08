package com.msoula.catlife.feature_inventory.presentation

import app.cash.turbine.test
import com.msoula.catlife.core.presentation.OnLifecycleEvent
import com.msoula.catlife.feature_inventory.data.repository.FakeInventoryItemRepository
import com.msoula.catlife.feature_inventory.data.state.AddEditInventoryFormState
import com.msoula.catlife.feature_inventory.data.state.InventoryItemFormEvent
import com.msoula.catlife.feature_inventory.domain.use_case.InventoryValidationUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.ValidateLabel
import com.msoula.catlife.feature_inventory.domain.use_case.ValidateQuantity
import com.msoula.catlife.feature_inventory.domain.use_case.crud.DeleteInventoryItemByIdUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.GetAllInventoryItemUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.GetInventoryItemByIdUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.InsertInventoryItemUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.InventoryCrudUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.UpdateInventoryItemQuantityUseCase
import com.msoula.catlife.feature_inventory.presentation.util.CoroutineTestRule
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddEditInventoryItemViewModelTest {

    @JvmField
    @Rule
    var mainCoroutineDispatcher = CoroutineTestRule()

    private lateinit var viewModel: AddEditInventoryItemViewModel
    private lateinit var fakeInventoryItemRepository: FakeInventoryItemRepository
    private lateinit var stateTest: StateFlow<AddEditInventoryFormState>
    private val validationUseCase = InventoryValidationUseCase(ValidateLabel(), ValidateQuantity())

    @Before
    fun setUp() {
        fakeInventoryItemRepository = FakeInventoryItemRepository()

        viewModel = AddEditInventoryItemViewModel(
            InventoryCrudUseCase(
                insertInventoryItem = InsertInventoryItemUseCase(fakeInventoryItemRepository),
                getAllInventoryItem = GetAllInventoryItemUseCase(fakeInventoryItemRepository),
                getInventoryItemById = GetInventoryItemByIdUseCase(fakeInventoryItemRepository),
                deleteInventoryItemById = DeleteInventoryItemByIdUseCase(fakeInventoryItemRepository),
                updateInventoryItemQuantity = UpdateInventoryItemQuantityUseCase(fakeInventoryItemRepository)
            ),
            validationUseCase
        )

        stateTest = viewModel.state
    }

    @Test
    fun `add new label, fetch it`() = runTest {
        stateTest.test {
            assertTrue(expectMostRecentItem().currentLabel.isEmpty())
        }

        viewModel.onUiEvent(InventoryItemFormEvent.InitLabel("testLabel")) { }
        stateTest = viewModel.state

        stateTest.test {
            assertTrue(expectMostRecentItem().currentLabel == "testLabel")
        }
    }

    @Test
    fun `add new quantity, fetch it`() = runTest {
        stateTest.test {
            assertTrue(expectMostRecentItem().currentQuantity.isEmpty())
        }

        viewModel.onUiEvent(InventoryItemFormEvent.InitQuantity("12")) { }
        stateTest = viewModel.state

        stateTest.test {
            assertTrue(expectMostRecentItem().currentQuantity != "11")
        }

        stateTest.test {
            assertTrue(expectMostRecentItem().currentQuantity == "12")
        }
    }

    @Test
    fun onLifecycleEventTest() = runTest {
        viewModel.onUiEvent(InventoryItemFormEvent.InitLabel("label for back pressed")) { }
        stateTest.test {
            assertTrue(expectMostRecentItem().currentLabel == "label for back pressed")
        }

        viewModel.onLifecycleEvent(OnLifecycleEvent.OnBackPressed)
        stateTest.test {
            assertTrue(expectMostRecentItem().currentLabel == "")
        }
    }

    @Test
    fun submitDataWithError() = runTest {
        viewModel.onUiEvent(InventoryItemFormEvent.InitLabel("label submit")) { }
        viewModel.onUiEvent(InventoryItemFormEvent.InitQuantity(".")) { }

        stateTest.test {
            val item = expectMostRecentItem()

            assertTrue(item.currentLabel == "label submit")
        }

        assertTrue(stateTest.value.currentLabelError == null)
        assertTrue(stateTest.value.currentQuantityError != null)
    }

    @Test
    fun onUiSubmitDataSuccess() = runTest {
        viewModel.onUiEvent(InventoryItemFormEvent.InitLabel("label submit")) { }
        viewModel.onUiEvent(InventoryItemFormEvent.InitQuantity("5")) { }

        viewModel.onUiEvent(InventoryItemFormEvent.SubmitElement) { println("Successfully submitted") }
    }
}
