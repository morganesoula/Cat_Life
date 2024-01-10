package com.msoula.catlife.feature_add_edit_cat.presentation

import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult
import com.msoula.catlife.core.domain.use_case.crud.CatUseCases
import com.msoula.catlife.core.presentation.OnLifecycleEvent
import com.msoula.catlife.core.presentation.navigation.AddEditCatFormScreenNavArgs
import com.msoula.catlife.core.util.Resource
import com.msoula.catlife.di.DispatcherModule
import com.msoula.catlife.extension.printToLog
import com.msoula.catlife.feature_add_edit_cat.data.state.AddEditCatFormState
import com.msoula.catlife.feature_add_edit_cat.data.state.mapToCat
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidationImplUseCase
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidationUseCases
import com.msoula.catlife.navArgs
import commsoulacatlifedatabase.CatEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddEditCatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val catUseCases: CatUseCases,
    private val validationUseCases: ValidationUseCases,
    @DispatcherModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditCatFormState())
    val state = _state.asStateFlow()

    private val navArgs: AddEditCatFormScreenNavArgs = savedStateHandle.navArgs()
    private var currentCatId: Int? = if (navArgs.catUpdateId != -1) navArgs.catUpdateId else null

    private lateinit var existingCat: CatEntity

    init {
        checkIfUpdate(currentCatId)
    }

    fun onUiEvent(
        event: AddEditCatFormEvent,
        onCatAddedOrUpdated: (id: Int?) -> Unit
    ) {
        when (event) {
            is AddEditCatFormEvent.EditCatGenderChanged -> {
                _state.update {
                    it.copy(catGender = event.gender)
                }
            }

            is AddEditCatFormEvent.OnEditCatProfilePicturePathChanged -> {
                _state.update {
                    it.copy(catPictureUri = event.catProfilePicturePath)
                }

                validateInput(ValidationImplUseCase.ValidateCatPicturePath)
            }

            is AddEditCatFormEvent.EditCatNameChanged -> {
                _state.update {
                    it.copy(catName = event.catName)
                }

                validateInput(ValidationImplUseCase.ValidateCatName)
            }

            is AddEditCatFormEvent.EditCatNeuteredChanged -> {
                _state.update {
                    it.copy(catNeutered = event.neutered)
                }
            }

            is AddEditCatFormEvent.EditCatWeightChanged -> {
                _state.update {
                    it.copy(weight = event.weight)
                }

                validateInput(ValidationImplUseCase.ValidateCatWeight)
            }

            is AddEditCatFormEvent.EditCatCoatChanged -> {
                _state.update {
                    it.copy(catCoat = event.coat)
                }

                validateInput(ValidationImplUseCase.ValidateCatCoat)
            }

            is AddEditCatFormEvent.EditCatRaceChanged -> {
                _state.update {
                    it.copy(
                        catRace = event.race,
                        allRaces = event.allRaces.filter { race: String ->
                            race.lowercase().startsWith(event.race.lowercase())
                        })
                }

                validateInput(ValidationImplUseCase.ValidateCatRace)
            }

            is AddEditCatFormEvent.OnClearClick -> {
                _state.update {
                    it.copy(catRace = "", allRaces = emptyList())
                }
            }

            is AddEditCatFormEvent.EditCatDiseasesChanged -> {
                _state.update {
                    it.copy(catDiseases = event.diseases)
                }

                validateInput(ValidationImplUseCase.ValidateCatDiseases)
            }

            is AddEditCatFormEvent.OnRaceFocusChanged -> {
                _state.update {
                    it.copy(allRaces = emptyList())
                }
            }

            is AddEditCatFormEvent.Submit -> {
                println("XXX - Submitting cat entry")
                saveCat(onCatAddedOrUpdated)
            }
        }
    }

    fun onLifecycleEvent(event: OnLifecycleEvent) {
        when (event) {
            is OnLifecycleEvent.OnBackPressed -> {
                _state.update {
                    AddEditCatFormState()
                }
            }
        }
    }

    fun onDateEvent(type: AddEditCatDateEvent, newDate: Long) {
        when (type) {
            is AddEditCatDateEvent.OnBirthdateChanged -> {
                _state.update {
                    it.copy(catBirthdate = newDate, catBirthdateError = null)
                }

                validateInput(ValidationImplUseCase.ValidateCatBirthdate)
                validateAllOptionalDatesUseCase()
            }

            is AddEditCatDateEvent.OnDewormingChanged -> {
                _state.update {
                    it.copy(catDewormingDate = newDate, catDewormingDateError = null)
                }

                validateInput(ValidationImplUseCase.ValidateCatDewormingDate)
            }

            is AddEditCatDateEvent.OnFleaChanged -> {
                _state.update {
                    it.copy(catFleaDate = newDate, catFleaDateError = null)
                }

                validateInput(ValidationImplUseCase.ValidateCatFleaDate)
            }

            is AddEditCatDateEvent.OnVaccineChanged -> {
                _state.update {
                    it.copy(catVaccineDate = newDate, catVaccineDateError = null)
                }

                validateInput(ValidationImplUseCase.ValidateCatVaccineDate)
            }
        }
    }

    fun onClearDateEvent(type: AddEditCatDateEvent) {
        when (type) {
            AddEditCatDateEvent.OnDewormingChanged -> {
                _state.update {
                    it.copy(catDewormingDate = 0L, catDewormingDateError = null)
                }

                validateInput(ValidationImplUseCase.ValidateCatDewormingDate)
            }

            AddEditCatDateEvent.OnFleaChanged -> {
                _state.update {
                    it.copy(catFleaDate = 0L, catFleaDateError = null)
                }

                validateInput(ValidationImplUseCase.ValidateCatFleaDate)
            }

            AddEditCatDateEvent.OnVaccineChanged -> {
                _state.update {
                    it.copy(catVaccineDate = 0L, catVaccineDateError = null)
                }

                validateInput(ValidationImplUseCase.ValidateCatVaccineDate)
            }

            else -> Unit
        }
    }

    fun initRacesList(allRaces: List<String>) {
        _state.update { it.copy(allRaces = allRaces) }
    }

    private fun checkIfUpdate(idOfCat: Int?) {
        idOfCat?.let {
            getSingleCat(idOfCat)
        }
    }

    private fun getSingleCat(idOfCat: Int) {
        viewModelScope.launch(ioDispatcher) {
            catUseCases.getCatUseCase.invoke(idOfCat)?.let {
                currentCatId = it.id.toInt()
                initCat(it)
            }
        }
    }

    private fun initCat(cat: CatEntity) {
        _state.update {
            it.copy(
                currentCatId = currentCatId,
                catName = cat.name,
                catGender = cat.gender,
                catNeutered = cat.neutered,
                catRace = cat.race,
                catBirthdate = cat.birthdate,
                weight = cat.weight.toString(),
                catCoat = cat.coat,
                catVaccineDate = cat.vaccineDate ?: 0,
                catFleaDate = cat.fleaDate ?: 0,
                catDewormingDate = cat.dewormingDate ?: 0,
                catDiseases = cat.diseases ?: "",
                catPictureUri = cat.profilePicturePath.toUri(),
                addEditBirthdateText = R.string.update_birthdate,
                submitCatText = R.string.update_general_btn,
                addEditComposableTitle = R.string.update_cat_title
            )
        }

        existingCat = cat
    }

    private fun validateInput(validationUseCase: ValidationImplUseCase) {
        val validationResult = when (validationUseCase) {
            is ValidationImplUseCase.ValidateCatName -> validationUseCases.validateCatNameUseCase.execute(
                state.value.catName
            )

            is ValidationImplUseCase.ValidateCatBirthdate -> validationUseCases.validateCatBirthdateUseCase.execute(
                state.value.catBirthdate
            )

            is ValidationImplUseCase.ValidateCatCoat -> validationUseCases.validateCatCoatUseCase.execute(
                state.value.catCoat
            )

            is ValidationImplUseCase.ValidateCatRace -> validationUseCases.validateCatRaceUseCase.execute(
                state.value.catRace
            )

            ValidationImplUseCase.ValidateCatDewormingDate -> validationUseCases.validateCatDewormingDateUseCase.execute(
                birthdate = state.value.catBirthdate,
                dewormingDate = state.value.catDewormingDate
            )

            ValidationImplUseCase.ValidateCatFleaDate -> validationUseCases.validateCatFleaDateUseCase.execute(
                fleaDate = state.value.catFleaDate,
                birthdate = state.value.catBirthdate
            )

            ValidationImplUseCase.ValidateCatPicturePath -> validationUseCases.validateCatPicturePathUseCase.execute(
                state.value.catPictureUri
            )

            ValidationImplUseCase.ValidateCatVaccineDate -> validationUseCases.validateCatVaccineDateUseCase.execute(
                vaccineDate = state.value.catVaccineDate,
                birthdate = state.value.catBirthdate
            )

            ValidationImplUseCase.ValidateCatWeight -> validationUseCases.validateCatWeightUseCase.execute(
                weight = state.value.weight
            )

            ValidationImplUseCase.ValidateCatDiseases -> validationUseCases.validateCatDiseases.execute(
                diseases = state.value.catDiseases
            )
        }

        val hasError = !validationResult.successful

        _state.update {
            when (validationUseCase) {
                ValidationImplUseCase.ValidateCatBirthdate -> it.copy(catBirthdateError = validationResult.errorMessage)
                ValidationImplUseCase.ValidateCatCoat -> it.copy(catCoatError = validationResult.errorMessage)
                ValidationImplUseCase.ValidateCatDewormingDate -> it.copy(catDewormingDateError = validationResult.errorMessage)
                ValidationImplUseCase.ValidateCatFleaDate -> it.copy(catFleaDateError = validationResult.errorMessage)
                ValidationImplUseCase.ValidateCatName -> it.copy(catNameError = validationResult.errorMessage)
                ValidationImplUseCase.ValidateCatPicturePath -> it.copy(catPictureUriError = validationResult.errorMessage)
                ValidationImplUseCase.ValidateCatRace -> it.copy(catRaceError = validationResult.errorMessage)
                ValidationImplUseCase.ValidateCatVaccineDate -> it.copy(catVaccineDateError = validationResult.errorMessage)
                ValidationImplUseCase.ValidateCatWeight -> it.copy(weightError = validationResult.errorMessage)
                ValidationImplUseCase.ValidateCatDiseases -> it.copy(catDiseasesError = validationResult.errorMessage)
            }
        }

        if (hasError) {
            if (state.value.enableSubmit) _state.update { it.copy(enableSubmit = false) }
            return
        }

        enableSubmit()
    }

    private fun enableSubmit() {
        val hasError = validateUseCase().any { !it.successful }

        if (hasError) return
        _state.update {
            it.copy(
                enableSubmit = !(::existingCat.isInitialized && existingCat == state.value.mapToCat())
            )
        }
    }

    private fun validateAllOptionalDatesUseCase() {
        validateInput(ValidationImplUseCase.ValidateCatVaccineDate)
        validateInput(ValidationImplUseCase.ValidateCatFleaDate)
        validateInput(ValidationImplUseCase.ValidateCatDewormingDate)
    }

    private fun validateUseCase(): List<ValidationResult> {
        val nameResult = validationUseCases.validateCatNameUseCase.execute(state.value.catName)
        val birthdateResult =
            validationUseCases.validateCatBirthdateUseCase.execute(state.value.catBirthdate)
        val coatResult = validationUseCases.validateCatCoatUseCase.execute(state.value.catCoat)
        val raceResult = validationUseCases.validateCatRaceUseCase.execute(state.value.catRace)
        val vaccineDateResult = validationUseCases.validateCatVaccineDateUseCase.execute(
            state.value.catVaccineDate,
            state.value.catBirthdate
        )
        val weightResult = validationUseCases.validateCatWeightUseCase.execute(state.value.weight)

        val fleaDateResult = if (state.value.catFleaDate != 0L) {
            validationUseCases.validateCatFleaDateUseCase.execute(
                state.value.catFleaDate,
                state.value.catBirthdate
            )
        } else {
            ValidationResult(true, null)
        }

        val picturePathResult =
            if (state.value.catPictureUri == null || state.value.catPictureUri.toString().isEmpty()) {
                ValidationResult(true)
            } else {
                validationUseCases.validateCatPicturePathUseCase.execute(state.value.catPictureUri)
            }

        val dewormingDateResult = validationUseCases.validateCatDewormingDateUseCase.execute(
            state.value.catDewormingDate,
            state.value.catBirthdate
        )

        val diseasesResult = validationUseCases.validateCatDiseases.execute(state.value.catDiseases)

        return listOf(
            nameResult,
            birthdateResult,
            raceResult,
            vaccineDateResult,
            weightResult,
            fleaDateResult,
            picturePathResult,
            coatResult,
            dewormingDateResult,
            diseasesResult
        )
    }

    private fun saveCat(onCatAddedOrUpdated: (Int?) -> Unit) {
        println("XXX - Saving cat")
        viewModelScope.launch {
            val result = catUseCases.insertCatUseCase.invoke(
                id = currentCatId?.toLong(),
                name = state.value.catName,
                gender = state.value.catGender,
                neutered = state.value.catNeutered,
                profilePicturePath = state.value.catPictureUri.toString(),
                birthdate = state.value.catBirthdate,
                weight = state.value.weight.toFloat(),
                race = state.value.catRace,
                coat = state.value.catCoat,
                diseases = state.value.catDiseases,
                fleaDate = state.value.catFleaDate,
                dewormingDate = state.value.catDewormingDate,
                vaccineDate = state.value.catVaccineDate
            )

            println("XXX - Result is: $result")

            when (result) {
                is Resource.Success -> {
                    fetchLastInsertedId(onCatAddedOrUpdated)
                }

                is Resource.Error -> {
                    result.throwable?.message.printToLog()
                }
            }
        }
    }

    private fun fetchLastInsertedId(onCatAddedOrUpdated: (Int?) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            _state.update {
                it.copy(lastInsertedCatId = catUseCases.fetchLastInsertedCatId.invoke()?.toInt() ?: -1)
            }

            withContext(Dispatchers.Main) {
                onCatAddedOrUpdated(state.value.lastInsertedCatId)
            }
        }
    }
}
