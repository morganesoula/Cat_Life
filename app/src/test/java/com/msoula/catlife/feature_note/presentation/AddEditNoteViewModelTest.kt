package com.msoula.catlife.feature_note.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.msoula.catlife.core.domain.use_case.crud.CatUseCases
import com.msoula.catlife.core.domain.use_case.crud.DeleteCatUseCase
import com.msoula.catlife.core.domain.use_case.crud.FetchLastInsertedCatIdUseCase
import com.msoula.catlife.core.domain.use_case.crud.GetAllCatsUseCase
import com.msoula.catlife.core.domain.use_case.crud.GetCatUseCase
import com.msoula.catlife.core.domain.use_case.crud.InsertCatUseCase
import com.msoula.catlife.feature_add_edit_cat.data.repository.FakeCatDataSource
import com.msoula.catlife.feature_inventory.presentation.util.CoroutineTestRule
import com.msoula.catlife.feature_note.data.repository.FakeNoteDataSource
import com.msoula.catlife.feature_note.data.state.AddEditNoteFormEvent
import com.msoula.catlife.feature_note.data.state.AddEditNoteFormState
import com.msoula.catlife.feature_note.domain.use_case.NoteValidationUseCases
import com.msoula.catlife.feature_note.domain.use_case.ValidateDescriptionUseCase
import com.msoula.catlife.feature_note.domain.use_case.ValidateSelectedCatUseCase
import com.msoula.catlife.feature_note.domain.use_case.ValidateTitleUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.CrudNoteUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.DeleteNoteByCatIdUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.DeleteNoteByIdUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.FetchLastInsertedNoteIdUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.GetAllCatNotesUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.GetAllNotesUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.GetNoteByIdUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.InsertNoteUseCase
import commsoulacatlifedatabase.CatEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class AddEditNoteViewModelTest {

    @JvmField
    @Rule
    var mainCoroutineDispatcher = CoroutineTestRule()

    private lateinit var addEditViewModel: AddEditNoteViewModel
    private lateinit var noteValidationUseCase: NoteValidationUseCases
    private lateinit var crudNoteUseCase: CrudNoteUseCase
    private lateinit var catUseCase: CatUseCases
    private val fakeNoteDataSource = FakeNoteDataSource()
    private val fakeCatDataSource = FakeCatDataSource()
    private lateinit var stateTest: StateFlow<AddEditNoteFormState>

    @Before
    fun setUp() {
        val savedStateHandle = SavedStateHandle(mapOf("noteUpdateId" to 0))

        noteValidationUseCase = NoteValidationUseCases(
            ValidateSelectedCatUseCase(),
            ValidateTitleUseCase(),
            ValidateDescriptionUseCase()
        )

        crudNoteUseCase = CrudNoteUseCase(
            InsertNoteUseCase(fakeNoteDataSource),
            GetNoteByIdUseCase(fakeNoteDataSource),
            GetAllNotesUseCase(fakeNoteDataSource),
            GetAllCatNotesUseCase(fakeNoteDataSource),
            DeleteNoteByIdUseCase(fakeNoteDataSource),
            DeleteNoteByCatIdUseCase(fakeNoteDataSource),
            FetchLastInsertedNoteIdUseCase(fakeNoteDataSource)
        )

        catUseCase = CatUseCases(
            InsertCatUseCase(fakeCatDataSource),
            DeleteCatUseCase(fakeCatDataSource),
            GetAllCatsUseCase(fakeCatDataSource),
            GetCatUseCase(fakeCatDataSource),
            FetchLastInsertedCatIdUseCase(fakeCatDataSource)
        )

        addEditViewModel = AddEditNoteViewModel(
            noteValidationUseCases = noteValidationUseCase,
            noteUseCase = crudNoteUseCase,
            ioDispatcher = Dispatchers.IO,
            savedStateHandle = savedStateHandle,
            catUseCases = catUseCase
        )

        stateTest = addEditViewModel.state
    }


    @Test
    fun onUiEventTitleTest() = runTest {
        addEditViewModel.onUiEvent(AddEditNoteFormEvent.OnTitleChanged("test title")) { }

        stateTest.test {
            assertTrue(expectMostRecentItem().currentTitle == "test title")
        }
    }

    @Test
    fun onUiEventDescriptionTest() = runTest {
        addEditViewModel.onUiEvent(AddEditNoteFormEvent.OnDescriptionChanged("test description")) { }

        stateTest.test {
            assertTrue(expectMostRecentItem().currentDescription == "test description")
        }
    }

    @Test
    fun onUiEventCatChangedTest() = runTest {
        addEditViewModel.onUiEvent(
            AddEditNoteFormEvent.OnCatChanged(
                CatEntity(
                    id = 3,
                    gender = true,
                    neutered = true,
                    name = "Felix",
                    profilePicturePath = "empty path",
                    birthdate = 1234L,
                    weight = 4f,
                    race = "Siamese",
                    coat = "White",
                    diseases = null,
                    vaccineDate = null,
                    fleaDate = null,
                    dewormingDate = null
                )
            )
        ) { }

        stateTest.test {
            assertTrue(expectMostRecentItem().currentCatName == "Felix")
        }
    }

    @Test
    fun onUiEventSubmitDataFailureTest() = runTest {
        addEditViewModel.onUiEvent(AddEditNoteFormEvent.OnDescriptionChanged("test submit description")) { }
        addEditViewModel.onUiEvent(AddEditNoteFormEvent.OnTitleChanged("")) { }

        stateTest.test {
            assertTrue(expectMostRecentItem().currentDescription == "test submit description")
        }

        stateTest.test {
            assertTrue(expectMostRecentItem().currentDescriptionErrorMessage == null)
        }

        stateTest.test {
            assertTrue(expectMostRecentItem().currentTitleErrorMessage != null)
        }
    }

    @Test
    fun onUiEventSubmitDataSuccessTest() = runTest {
        addEditViewModel.onUiEvent(AddEditNoteFormEvent.OnTitleChanged("test submit title")) { }
        addEditViewModel.onUiEvent(AddEditNoteFormEvent.OnDescriptionChanged("test submit description")) { }
        addEditViewModel.onUiEvent(
            AddEditNoteFormEvent.OnCatChanged(
                CatEntity(
                    id = 3,
                    gender = true,
                    neutered = true,
                    name = "Felix",
                    profilePicturePath = "empty path",
                    birthdate = 1234L,
                    weight = 4f,
                    race = "Siamese",
                    coat = "White",
                    diseases = null,
                    vaccineDate = null,
                    fleaDate = null,
                    dewormingDate = null
                )
            )
        ) { }

        stateTest.test {
            assertEquals("test submit description", awaitItem().currentDescription)
        }

        addEditViewModel.onUiEvent(AddEditNoteFormEvent.Submit) { println("Successfully submitted") }
        this.advanceUntilIdle()

        withContext(Dispatchers.IO) {
            Thread.sleep(500)
        }

        stateTest.test {
            assertTrue(expectMostRecentItem().lastInsertedNoteId == 0)
        }
    }
}