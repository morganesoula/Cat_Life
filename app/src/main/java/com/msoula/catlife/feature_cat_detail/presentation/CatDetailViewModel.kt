package com.msoula.catlife.feature_cat_detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msoula.catlife.core.domain.use_case.crud.CatUseCases
import com.msoula.catlife.core.presentation.navigation.CatDetailScreenNavArgs
import com.msoula.catlife.core.util.Resource
import com.msoula.catlife.di.DispatcherModule
import com.msoula.catlife.feature_cat_detail.data.state.CatDetailState
import com.msoula.catlife.feature_inventory.data.state.UiActionEvent
import com.msoula.catlife.feature_note.domain.use_case.crud.CrudNoteUseCase
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
class CatDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val catUseCases: CatUseCases,
    private val noteUseCase: CrudNoteUseCase,
    @DispatcherModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(CatDetailState())
    val state = _state.asStateFlow()

    private val navArgs: CatDetailScreenNavArgs = savedStateHandle.navArgs()

    init {
        initCat()
    }

    private fun initCat() {
        viewModelScope.launch(ioDispatcher) {
            catUseCases.getCatUseCase(navArgs.catDetailId)?.let { catInDb ->
                _state.update {
                    it.copy(
                        catId = navArgs.catDetailId,
                        catName = catInDb.name,
                        catProfilePath = catInDb.profilePicturePath,
                        catBirthdate = catInDb.birthdate,
                        gender = catInDb.gender,
                        neutered = catInDb.neutered,
                        lastVaccineDate = catInDb.vaccineDate,
                        lastFleaDate = catInDb.fleaDate,
                        lastDewormingDate = catInDb.dewormingDate,
                        race = catInDb.race,
                        fur = catInDb.coat,
                        weight = catInDb.weight,
                        diseases = catInDb.diseases
                    )
                }
            }
        }
    }

    fun onElementActionEvent(event: UiActionEvent, goBackToListScreen: () -> Unit) {
        when (event) {
            is UiActionEvent.OpenDeleteAlertDialog -> {
                _state.update { it.copy(openDeleteAlert = true, catId = event.itemId) }
            }

            is UiActionEvent.OnDismissRequest -> {
                _state.update { it.copy(openDeleteAlert = false) }
            }

            is UiActionEvent.OnDeleteUi -> {
                if (event.deleteData) {
                    deleteNote(event.elementId)
                    deleteCat(event.elementId, goBackToListScreen)
                }
            }

            else -> Unit
        }
    }

    private fun deleteNote(catId: Int) {
        viewModelScope.launch(ioDispatcher) {
            when (val result = noteUseCase.deleteNoteByCatIdUseCase.invoke(catId)) {
                is Resource.Success -> {}
                is Resource.Error -> result.throwable?.message.printToLog()
            }
        }
    }

    private fun deleteCat(catId: Int, goBackToListScreen: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            when (val result = catUseCases.deleteCatUseCase.invoke(catId)) {
                is Resource.Success -> withContext(Dispatchers.Main) { goBackToListScreen() }
                is Resource.Error -> result.throwable?.message.printToLog()
            }
        }
    }
}
