package com.msoula.catlife.feature_note_detail.presentation

import androidx.lifecycle.SavedStateHandle
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
import com.msoula.catlife.feature_note_detail.data.NoteDetailUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteDetailViewModelTest {

    @JvmField
    @Rule
    var mainCoroutineDispatcher = CoroutineTestRule()

    private lateinit var viewModel: NoteDetailViewModel
    private lateinit var noteUseCases: CrudNoteUseCase
    private val fakeNoteDataSource = FakeNoteDataSource()

    private lateinit var stateTest: StateFlow<NoteDetailUiState>

    @Before
    fun setUp() {
        val savedStateHandle = SavedStateHandle(mapOf("noteDetailId" to 2))

        noteUseCases = CrudNoteUseCase(
            InsertNoteUseCase(fakeNoteDataSource),
            GetNoteByIdUseCase(fakeNoteDataSource),
            GetAllNotesUseCase(fakeNoteDataSource),
            GetAllCatNotesUseCase(fakeNoteDataSource),
            DeleteNoteByIdUseCase(fakeNoteDataSource),
            DeleteNoteByCatIdUseCase(fakeNoteDataSource),
            FetchLastInsertedNoteIdUseCase(fakeNoteDataSource)
        )

        viewModel = NoteDetailViewModel(
            savedStateHandle = savedStateHandle,
            noteUseCase = noteUseCases,
            Dispatchers.IO
        )

        stateTest = viewModel.state
    }

    @Test
    fun onUiDeleteEvent() = runTest {
        // TODO Redo one
    }
}