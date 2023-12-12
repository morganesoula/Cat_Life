package com.msoula.catlife.feature_note.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult
import com.msoula.catlife.core.domain.use_case.crud.CatUseCases
import com.msoula.catlife.core.presentation.OnLifecycleEvent
import com.msoula.catlife.core.presentation.navigation.AddEditNoteFormScreenNavArgs
import com.msoula.catlife.core.util.Resource
import com.msoula.catlife.di.DispatcherModule
import com.msoula.catlife.feature_note.data.state.AddEditNoteFormEvent
import com.msoula.catlife.feature_note.data.state.AddEditNoteFormState
import com.msoula.catlife.feature_note.data.state.CatForNoteUiState
import com.msoula.catlife.feature_note.data.state.mapToNote
import com.msoula.catlife.feature_note.domain.use_case.NoteImplValidationUseCase
import com.msoula.catlife.feature_note.domain.use_case.NoteValidationUseCases
import com.msoula.catlife.feature_note.domain.use_case.crud.CrudNoteUseCase
import com.msoula.catlife.navArgs
import commsoulacatlifedatabase.CatEntity
import commsoulacatlifedatabase.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import printToLog
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val noteValidationUseCases: NoteValidationUseCases,
    private val noteUseCase: CrudNoteUseCase,
    @DispatcherModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    catUseCases: CatUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditNoteFormState())
    val state = _state.asStateFlow()

    private val navArgs: AddEditNoteFormScreenNavArgs = savedStateHandle.navArgs()
    private var currentNoteId: Int? = if (navArgs.noteUpdateId != -1) navArgs.noteUpdateId else null

    val cats = catUseCases.getAllCatsUseCase()
        .map<List<CatEntity>, CatForNoteUiState>(CatForNoteUiState::Success)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CatForNoteUiState.Loading
        )

    private lateinit var existingNote: Note

    init {
        checkIfUpdate(currentNoteId)
    }

    fun onLifecycleEvent(event: OnLifecycleEvent) {
        when (event) {
            is OnLifecycleEvent.OnBackPressed -> {
                _state.update {
                    AddEditNoteFormState()
                }
            }
        }
    }

    fun onUiEvent(event: AddEditNoteFormEvent, onNoteAddedOrUpdated: (id: Int?) -> Unit) {
        when (event) {
            is AddEditNoteFormEvent.OnCatChanged -> {
                _state.update {
                    it.copy(
                        currentCatProfilePath = event.cat.profilePicturePath,
                        currentCatName = event.cat.name,
                        currentCatId = event.cat.id.toInt()
                    )
                }

                validateInput(
                    NoteImplValidationUseCase.ValidatePickedCatUseCase,
                    ""
                )
            }

            is AddEditNoteFormEvent.OnTitleChanged -> {
                _state.update {
                    it.copy(currentTitle = event.title, currentTitleErrorMessage = null)
                }

                validateInput(
                    NoteImplValidationUseCase.ValidateTitleUseCase,
                    state.value.currentTitle
                )
            }

            is AddEditNoteFormEvent.OnDescriptionChanged -> {
                _state.update {
                    it.copy(
                        currentDescription = event.description,
                        currentDescriptionErrorMessage = null
                    )
                }

                validateInput(
                    NoteImplValidationUseCase.ValidateDescriptionUseCase,
                    state.value.currentDescription
                )
            }

            is AddEditNoteFormEvent.OnNoteUpdatedEvent -> {
                checkIfUpdate(event.noteId)
            }

            is AddEditNoteFormEvent.Submit -> {
                if (state.value.currentCatId == 0) {
                    _state.update {
                        it.copy(
                            currentCatName = when (val data = cats.value) {
                                is CatForNoteUiState.Success -> data.catForNote.first { selectedCat: CatEntity -> selectedCat.id.toInt() == state.value.currentCatId }.name
                                else -> ""
                            },
                            currentCatProfilePath = when (val data = cats.value) {
                                is CatForNoteUiState.Success -> data.catForNote.first { selectedCat: CatEntity -> selectedCat.id.toInt() == state.value.currentCatId }.profilePicturePath
                                else -> ""
                            }
                        )
                    }
                }

                saveNote(onNoteAddedOrUpdated)
            }
        }
    }

    private fun validateInput(
        validationUseCase: NoteImplValidationUseCase,
        input: String
    ) {
        val validationResult = when (validationUseCase) {
            NoteImplValidationUseCase.ValidatePickedCatUseCase -> noteValidationUseCases.validateSelectedCatUseCase.execute(
                state.value.currentCatId
            )

            NoteImplValidationUseCase.ValidateTitleUseCase -> noteValidationUseCases.validateTitleUseCase.execute(
                input
            )

            NoteImplValidationUseCase.ValidateDescriptionUseCase -> noteValidationUseCases.validateDescriptionUseCase.execute(
                input
            )
        }

        val hasError = !validationResult.successful

        _state.update {
            when (validationUseCase) {
                is NoteImplValidationUseCase.ValidatePickedCatUseCase -> it.copy(
                    currentCatIdErrorMessage = validationResult.errorMessage
                )

                is NoteImplValidationUseCase.ValidateTitleUseCase -> it.copy(
                    currentTitleErrorMessage = validationResult.errorMessage
                )

                is NoteImplValidationUseCase.ValidateDescriptionUseCase ->
                    it.copy(
                        currentDescriptionErrorMessage = validationResult.errorMessage
                    )
            }
        }

        if (hasError) {
            if (state.value.enableSubmit) _state.update { it.copy(enableSubmit = false) }
            return
        }

        enableSubmit()
    }

    private fun enableSubmit() {
        val hasError = validateUseCases().any { !it.successful }

        if (hasError) return

        _state.update {
            it.copy(
                enableSubmit = !(::existingNote.isInitialized && existingNote == state.value.mapToNote())
            )
        }
    }

    private fun validateUseCases(): List<ValidationResult> {
        val catPickedResult =
            noteValidationUseCases.validateSelectedCatUseCase.execute(state.value.currentCatId)
        val titleResult =
            noteValidationUseCases.validateTitleUseCase.execute(state.value.currentTitle)
        val descriptionResult =
            noteValidationUseCases.validateDescriptionUseCase.execute(state.value.currentDescription)

        return listOf(catPickedResult, titleResult, descriptionResult)
    }

    private fun saveNote(onNoteAddedOrUpdated: (id: Int?) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            val result = noteUseCase.insertNoteUseCase(
                id = currentNoteId?.toLong(),
                catProfilePath = state.value.currentCatProfilePath,
                catId = state.value.currentCatId,
                title = state.value.currentTitle,
                content = state.value.currentDescription,
                catName = state.value.currentCatName,
                date = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            )

            when (result) {
                is Resource.Success -> {
                    fetchLastInsertedNoteId(onNoteAddedOrUpdated)
                }

                is Resource.Error -> {
                    result.throwable?.message.printToLog()
                }
            }
        }
    }

    private fun fetchLastInsertedNoteId(onNoteAddedOrUpdated: (id: Int?) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            _state.update {
                it.copy(
                    lastInsertedNoteId = noteUseCase.fetchLastInsertedIdUseCase.invoke()?.toInt()
                        ?: -1
                )
            }
            withContext(Dispatchers.Main) {
                onNoteAddedOrUpdated(state.value.lastInsertedNoteId)
            }
        }
    }

    private fun checkIfUpdate(noteId: Int?) {
        viewModelScope.launch(ioDispatcher) {
            val entity = noteId?.let {
                noteUseCase.getNoteByIdUseCase.invoke(noteId = it)
            }

            entity?.let { initNote(it) }
        }

    }

    private fun initNote(note: Note) {
        _state.update {
            it.copy(
                currentNoteId = currentNoteId,
                currentCatProfilePath = note.catProfilePath ?: "",
                currentCatId = note.catId.toInt(),
                currentCatName = note.catName,
                currentTitle = note.title,
                currentDate = note.date,
                currentDescription = note.content,
                addEditNoteComposableTitle = R.string.update_note_title,
                submitNoteText = R.string.update_general_btn
            )
        }

        existingNote = note
    }
}