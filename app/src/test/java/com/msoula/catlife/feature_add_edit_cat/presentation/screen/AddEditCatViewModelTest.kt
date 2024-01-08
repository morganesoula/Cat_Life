package com.msoula.catlife.feature_add_edit_cat.presentation.screen

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.msoula.catlife.core.domain.use_case.crud.CatUseCases
import com.msoula.catlife.core.domain.use_case.crud.DeleteCatUseCase
import com.msoula.catlife.core.domain.use_case.crud.FetchLastInsertedCatIdUseCase
import com.msoula.catlife.core.domain.use_case.crud.GetAllCatsUseCase
import com.msoula.catlife.core.domain.use_case.crud.GetCatUseCase
import com.msoula.catlife.core.domain.use_case.crud.InsertCatUseCase
import com.msoula.catlife.core.presentation.OnLifecycleEvent
import com.msoula.catlife.feature_add_edit_cat.data.repository.FakeCatDataSource
import com.msoula.catlife.feature_add_edit_cat.data.state.AddEditCatFormState
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatBirthdate
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatCoat
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatDewormingDate
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatDiseases
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatFleaDate
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatName
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatPicturePath
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatRaceUseCase
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatVaccineDate
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatWeight
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidationUseCases
import com.msoula.catlife.feature_add_edit_cat.presentation.AddEditCatDateEvent
import com.msoula.catlife.feature_add_edit_cat.presentation.AddEditCatFormEvent
import com.msoula.catlife.feature_add_edit_cat.presentation.AddEditCatViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AddEditCatViewModelTest {
    private lateinit var viewModel: AddEditCatViewModel
    private lateinit var stateTest: StateFlow<AddEditCatFormState>

    private val fakeCatDataSource = FakeCatDataSource()

    @Before
    fun setUp() {
        val savedStateHandle = SavedStateHandle(mapOf("catUpdateId" to 0))

        viewModel =
            AddEditCatViewModel(
                savedStateHandle = savedStateHandle,
                catUseCases = CatUseCases(
                    InsertCatUseCase(fakeCatDataSource),
                    DeleteCatUseCase(fakeCatDataSource),
                    GetAllCatsUseCase(fakeCatDataSource),
                    GetCatUseCase(fakeCatDataSource),
                    FetchLastInsertedCatIdUseCase(fakeCatDataSource)
                ),
                validationUseCases = ValidationUseCases(
                    ValidateCatPicturePath(),
                    ValidateCatName(),
                    ValidateCatWeight(),
                    ValidateCatCoat(),
                    ValidateCatRaceUseCase(),
                    ValidateCatBirthdate(),
                    ValidateCatVaccineDate(),
                    ValidateCatFleaDate(),
                    ValidateCatDewormingDate(),
                    ValidateCatDiseases()
                ),
                Dispatchers.IO
            )

        stateTest = viewModel.state
    }

    @Test
    fun onEventTest() = runTest {
        stateTest.test {
            assertEquals("", expectMostRecentItem().catName)
        }

        stateTest.test {
            assertTrue(expectMostRecentItem().catNeutered)
        }

        viewModel.onUiEvent(AddEditCatFormEvent.EditCatNameChanged("fakeName")) { }
        viewModel.onUiEvent(AddEditCatFormEvent.EditCatNeuteredChanged(false)) { }

        stateTest = viewModel.state

        stateTest.test {
            assertTrue(expectMostRecentItem().catName == "fakeName")
        }

        stateTest.test {
            assertFalse(expectMostRecentItem().catNeutered)
        }
    }

    @Test
    fun onDateEventTest() = runTest {
        stateTest.test {
            assertTrue(expectMostRecentItem().catBirthdate == 0L)
        }

        stateTest.test {
            assertTrue(expectMostRecentItem().catFleaDate == 0L)
        }

        viewModel.onDateEvent(AddEditCatDateEvent.OnBirthdateChanged, 1528495200000L)
        viewModel.onDateEvent(AddEditCatDateEvent.OnFleaChanged, 1619042400000L)

        stateTest = viewModel.state

        stateTest.test {
            assertTrue(expectMostRecentItem().catFleaDate == 1619042400000L)
        }

        stateTest.test {
            assertTrue(expectMostRecentItem().catBirthdate == 1528495200000L)
        }
    }

    @Test
    fun onLifecycleEventTest() = runTest {
        viewModel.onUiEvent(AddEditCatFormEvent.EditCatNameChanged("Test for lifecycle")) { }

        stateTest.test {
            assertTrue(expectMostRecentItem().catName == "Test for lifecycle")
        }

        viewModel.onLifecycleEvent(OnLifecycleEvent.OnBackPressed)

        stateTest.test {
            assertTrue(expectMostRecentItem().catName == "")
        }
    }
}
