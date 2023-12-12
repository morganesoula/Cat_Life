package com.msoula.catlife.feature_note_detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msoula.catlife.core.presentation.navigation.NoteDetailScreenNavArgs
import com.msoula.catlife.core.util.Resource
import com.msoula.catlife.di.DispatcherModule
import com.msoula.catlife.feature_inventory.data.state.UiActionEvent
import com.msoula.catlife.feature_note.domain.use_case.crud.CrudNoteUseCase
import com.msoula.catlife.feature_note_detail.data.NoteDetailUiState
import com.msoula.catlife.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import printToLog
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val noteUseCase: CrudNoteUseCase,
    @DispatcherModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(NoteDetailUiState())
    val state = _state.asStateFlow()

    private val navArgs: NoteDetailScreenNavArgs = savedStateHandle.navArgs()

    init {
        viewModelScope.launch(ioDispatcher) {
            try {
                initNote()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun initNote() {
        noteUseCase.getNoteByIdUseCase(navArgs.noteDetailId)?.let { noteEntity ->
            _state.update {
                it.copy(
                    noteId = noteEntity.id.toInt(),
                    catId = noteEntity.catId.toInt(),
                    noteTitle = noteEntity.title,
                    noteDescription = noteEntity.content,
                    catProfilePath = noteEntity.catProfilePath,
                    catName = noteEntity.catName
                )
            }
        }
    }

    fun onElementActionEvent(event: UiActionEvent, goBackToListScreen: () -> Unit) {
        when (event) {
            is UiActionEvent.OpenDeleteAlertDialog -> {
                _state.update { it.copy(openDeleteAlert = true, noteId = event.itemId) }
            }

            is UiActionEvent.OnDismissRequest -> {
                _state.update { it.copy(openDeleteAlert = false) }
            }

            is UiActionEvent.OnDeleteUi -> {
                if (event.deleteData) {
                    deleteNoteById(event.elementId, goBackToListScreen)
                }
            }

            else -> Unit
        }
    }

    private fun deleteNoteById(noteId: Int, goBackToListScreen: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            when (val result = noteUseCase.deleteNoteByIdUseCase.invoke(noteId)) {
                is Resource.Success -> withContext(Dispatchers.Main) { goBackToListScreen() }
                is Resource.Error -> result.throwable?.message.printToLog()
            }
        }
    }
}