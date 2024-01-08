package com.msoula.catlife.feature_cat_detail.presentation

import androidx.lifecycle.SavedStateHandle
import com.msoula.catlife.core.domain.use_case.crud.CatUseCases
import com.msoula.catlife.core.domain.use_case.crud.DeleteCatUseCase
import com.msoula.catlife.core.domain.use_case.crud.FetchLastInsertedCatIdUseCase
import com.msoula.catlife.core.domain.use_case.crud.GetAllCatsUseCase
import com.msoula.catlife.core.domain.use_case.crud.GetCatUseCase
import com.msoula.catlife.core.domain.use_case.crud.InsertCatUseCase
import com.msoula.catlife.feature_add_edit_cat.data.repository.FakeCatDataSource
import com.msoula.catlife.feature_cat_detail.data.state.CatDetailState
import com.msoula.catlife.feature_inventory.presentation.util.CoroutineTestRule
import com.msoula.catlife.feature_note.data.repository.FakeNoteDataSource
import com.msoula.catlife.feature_note.domain.use_case.crud.CrudNoteUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.DeleteNoteByCatIdUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.DeleteNoteByIdUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.FetchLastInsertedNoteIdUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.GetAllCatNotesUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.GetAllNotesUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.GetNoteByIdUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.InsertNoteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CatDetailViewModelTest {

    @JvmField
    @Rule
    var mainCoroutineDispatcher = CoroutineTestRule()

    private lateinit var viewModel: CatDetailViewModel
    private lateinit var catUseCases: CatUseCases
    private lateinit var noteUseCase: CrudNoteUseCase
    private val fakeCatDataSource = FakeCatDataSource()
    private val fakeNoteDateSource = FakeNoteDataSource()
    private val savedStateHandle = SavedStateHandle()

    private lateinit var stateTest: StateFlow<CatDetailState>

    @Before
    fun setUp() {
        savedStateHandle["catDetailId"] = 2

        catUseCases = CatUseCases(
            InsertCatUseCase(fakeCatDataSource),
            DeleteCatUseCase(fakeCatDataSource),
            GetAllCatsUseCase(fakeCatDataSource),
            GetCatUseCase(fakeCatDataSource),
            FetchLastInsertedCatIdUseCase(fakeCatDataSource)
        )

        noteUseCase = CrudNoteUseCase(
            insertNoteUseCase = InsertNoteUseCase(fakeNoteDateSource),
            deleteNoteByCatIdUseCase = DeleteNoteByCatIdUseCase(fakeNoteDateSource),
            deleteNoteByIdUseCase = DeleteNoteByIdUseCase(fakeNoteDateSource),
            getAllCatNotesUseCase = GetAllCatNotesUseCase(fakeNoteDateSource),
            getAllNotesUseCase = GetAllNotesUseCase(fakeNoteDateSource),
            getNoteByIdUseCase = GetNoteByIdUseCase(fakeNoteDateSource),
            fetchLastInsertedIdUseCase = FetchLastInsertedNoteIdUseCase(fakeNoteDateSource)
        )

        viewModel = CatDetailViewModel(
            savedStateHandle = savedStateHandle,
            catUseCases = catUseCases,
            noteUseCase = noteUseCase,
            Dispatchers.IO
        )

        stateTest = viewModel.state
    }

    @Test
    fun onUiDeleteEvent() = runTest {
        //TODO Redo one
    }
}