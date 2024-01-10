package com.msoula.catlife.feature_inventory.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msoula.catlife.core.domain.use_case.ValidationResult
import com.msoula.catlife.core.presentation.OnLifecycleEvent
import com.msoula.catlife.core.util.Resource
import com.msoula.catlife.extension.printToLog
import com.msoula.catlife.feature_inventory.data.state.AddEditInventoryFormState
import com.msoula.catlife.feature_inventory.data.state.InventoryItemFormEvent
import com.msoula.catlife.feature_inventory.domain.use_case.InventoryImplValidationUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.InventoryValidationUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.InventoryCrudUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditInventoryItemViewModel @Inject constructor(
    private val inventoryCrudUseCase: InventoryCrudUseCase,
    private val inventoryValidationUseCase: InventoryValidationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditInventoryFormState())
    val state = _state.asStateFlow()

    fun onLifecycleEvent(event: OnLifecycleEvent) {
        when (event) {
            is OnLifecycleEvent.OnBackPressed -> {
                _state.update {
                    AddEditInventoryFormState()
                }
            }
        }
    }

    fun onUiEvent(event: InventoryItemFormEvent, onInventoryItemAdded: () -> Unit) {
        when (event) {
            is InventoryItemFormEvent.InitLabel -> {
                _state.update {
                    it.copy(currentLabel = event.label)
                }

                validateInput(
                    InventoryImplValidationUseCase.ValidateLabelUseCase
                )
            }

            is InventoryItemFormEvent.InitQuantity -> {
                _state.update {
                    it.copy(currentQuantity = event.quantity)
                }

                validateInput(
                    InventoryImplValidationUseCase.ValidateQuantityUseCase
                )
            }

            is InventoryItemFormEvent.SubmitElement -> saveInventoryItem(onInventoryItemAdded)
        }
    }

    private fun validateInput(
        validationUseCase: InventoryImplValidationUseCase
    ) {
        val validationResult = when (validationUseCase) {
            is InventoryImplValidationUseCase.ValidateLabelUseCase -> inventoryValidationUseCase.validateLabelUseCase.execute(
                state.value.currentLabel,
            )

            is InventoryImplValidationUseCase.ValidateQuantityUseCase -> inventoryValidationUseCase.validateQuantityUseCase.execute(
                state.value.currentQuantity
            )
        }

        val hasError = !validationResult.successful

        _state.update {
            when (validationUseCase) {
                is InventoryImplValidationUseCase.ValidateLabelUseCase -> it.copy(currentLabelError = validationResult.errorMessage)
                is InventoryImplValidationUseCase.ValidateQuantityUseCase -> it.copy(
                    currentQuantityError = validationResult.errorMessage
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
        val hasError = validateUseCase().any { (!it.successful) }

        if (hasError) return

        _state.update {
            it.copy(enableSubmit = true)
        }
    }

    private fun validateUseCase(): List<ValidationResult> {
        val labelResult =
            inventoryValidationUseCase.validateLabelUseCase.execute(state.value.currentLabel)
        val quantityResult =
            inventoryValidationUseCase.validateQuantityUseCase.execute(state.value.currentQuantity)

        return listOf(labelResult, quantityResult)
    }

    private fun saveInventoryItem(onInventoryItemAdded: () -> Unit) {
        viewModelScope.launch {
            val result = inventoryCrudUseCase.insertInventoryItem.invoke(
                state.value.currentLabel,
                state.value.currentQuantity.toInt(),
                null
            )

            when (result) {
                is Resource.Success -> {
                    onInventoryItemAdded()
                }

                is Resource.Error -> {
                    result.throwable?.message.printToLog()
                }
            }
        }
    }
}
