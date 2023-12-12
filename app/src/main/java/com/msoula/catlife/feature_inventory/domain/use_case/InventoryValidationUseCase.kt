package com.msoula.catlife.feature_inventory.domain.use_case

data class InventoryValidationUseCase(
    val validateLabelUseCase: ValidateLabel,
    val validateQuantityUseCase: ValidateQuantity
)

sealed interface InventoryImplValidationUseCase {
    data object ValidateLabelUseCase: InventoryImplValidationUseCase
    data object ValidateQuantityUseCase: InventoryImplValidationUseCase
}
