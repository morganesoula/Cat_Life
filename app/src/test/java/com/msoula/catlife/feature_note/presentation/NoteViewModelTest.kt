package com.msoula.catlife.feature_note.presentation

import com.msoula.catlife.feature_inventory.data.state.UiActionEvent
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
import commsoulacatlifedatabase.Note
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
internal class NoteViewModelTest {

    @JvmField
    @Rule
    val mainCoroutineDispatcher = CoroutineTestRule()

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var crudNoteUseCase: CrudNoteUseCase
    private val fakeNoteDataSource = FakeNoteDataSource()
    private lateinit var noteEntity: Note

    @Before
    fun setUp() {
        crudNoteUseCase = CrudNoteUseCase(
            InsertNoteUseCase(fakeNoteDataSource),
            GetNoteByIdUseCase(fakeNoteDataSource),
            GetAllNotesUseCase(fakeNoteDataSource),
            GetAllCatNotesUseCase(fakeNoteDataSource),
            DeleteNoteByIdUseCase(fakeNoteDataSource),
            DeleteNoteByCatIdUseCase(fakeNoteDataSource),
            FetchLastInsertedNoteIdUseCase(fakeNoteDataSource)
        )

        notesViewModel = NotesViewModel(crudNoteUseCase, Dispatchers.IO)

        noteEntity = Note(
            id = 1,
            catId = 1,
            catProfilePath = "random path",
            catName = "Felix",
            date = 123455L,
            title = "Note for Felix",
            content = "Random content for Felix"
        )
    }

    @Test
    fun `add element then delete it`() = runTest {
        crudNoteUseCase.insertNoteUseCase.invoke(
            id = 1,
            catId = 1,
            catProfilePath = "random path",
            catName = "Felix",
            date = 123455L,
            title = "Note for Felix",
            content = "Random content for Felix"
        )

        assertTrue(crudNoteUseCase.getAllNotesUseCase.invoke().first().size == 1)

        notesViewModel.onElementActionEvent(UiActionEvent.OnSwipeDelete(noteEntity)) { }
        this.advanceUntilIdle()

        withContext(Dispatchers.IO) {
            Thread.sleep(1000)
        }

        assertTrue(crudNoteUseCase.getAllNotesUseCase.invoke().first().isEmpty())
    }
}