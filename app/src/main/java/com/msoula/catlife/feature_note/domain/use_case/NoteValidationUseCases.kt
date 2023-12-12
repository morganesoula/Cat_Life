package com.msoula.catlife.feature_note.domain.use_case

data class NoteValidationUseCases(
    val validateSelectedCatUseCase: ValidateSelectedCatUseCase,
    val validateTitleUseCase: ValidateTitleUseCase,
    val validateDescriptionUseCase: ValidateDescriptionUseCase
)

sealed interface NoteImplValidationUseCase {
    data object ValidatePickedCatUseCase: NoteImplValidationUseCase
    data object ValidateTitleUseCase: NoteImplValidationUseCase
    data object ValidateDescriptionUseCase: NoteImplValidationUseCase
}
