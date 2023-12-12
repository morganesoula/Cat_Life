package com.msoula.catlife.feature_note.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msoula.catlife.core.util.Resource
import com.msoula.catlife.di.DispatcherModule
import com.msoula.catlife.feature_inventory.data.state.UiActionEvent
import com.msoula.catlife.feature_note.data.state.NoteFeedUiState
import com.msoula.catlife.feature_note.data.state.NoteUiState
import com.msoula.catlife.feature_note.domain.use_case.crud.CrudNoteUseCase
import commsoulacatlifedatabase.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import printToLog
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class NotesViewModel @Inject constructor(
    private val crudNoteUseCase: CrudNoteUseCase,
    @DispatcherModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val selectedCatStream: MutableStateFlow<Int?> =
        MutableStateFlow(null)

    val noteState: StateFlow<NoteFeedUiState> =
        selectedCatStream.flatMapLatest { selectedCat ->
            crudNoteUseCase.getAllCatNotesUseCase(selectedCat)
        }
            .map { list: List<Note> ->
                if (list.isEmpty()) NoteFeedUiState.Empty else NoteFeedUiState.Success(list)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = NoteFeedUiState.Loading
            )

    private val _noteUiState = MutableStateFlow(NoteUiState())
    val noteUiState = _noteUiState.asStateFlow()

    fun onElementActionEvent(event: UiActionEvent, goBackToListScreen: () -> Unit) {
        when (event) {
            is UiActionEvent.OnDismissRequest -> {
                _noteUiState.update { it.copy(openDeleteAlert = false) }
            }

            is UiActionEvent.OpenDeleteAlertDialog -> {
                _noteUiState.update { it.copy(openDeleteAlert = true, itemId = event.itemId) }
            }

            is UiActionEvent.OnDeleteUi -> {
                if (event.deleteData) {
                    removeDataWithId(event.elementId, goBackToListScreen)
                }

                _noteUiState.update { it.copy(openDeleteAlert = false) }
            }

            is UiActionEvent.OnSwipeDelete -> removeData(event.item as Note)

            is UiActionEvent.OnCatFiltered -> {
                selectedCatStream.update {
                    event.catId
                }
            }

            else -> Unit
        }
    }

    private fun removeData(note: Note) {
        viewModelScope.launch(ioDispatcher) {
            when (val result = crudNoteUseCase.deleteNoteByIdUseCase.invoke(note.id.toInt())) {
                is Resource.Success -> Unit
                is Resource.Error -> result.throwable?.message.printToLog()
            }
        }
    }

    private fun removeDataWithId(itemId: Int, goBackToListScreen: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            when (val result = crudNoteUseCase.deleteNoteByIdUseCase.invoke(itemId)) {
                is Resource.Success -> withContext(Dispatchers.Main) { goBackToListScreen() }
                is Resource.Error -> result.throwable?.message.printToLog()
            }
        }
    }
}
