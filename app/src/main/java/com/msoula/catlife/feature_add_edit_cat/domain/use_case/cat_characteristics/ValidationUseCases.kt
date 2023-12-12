package com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics

data class ValidationUseCases(
    val validateCatPicturePathUseCase: ValidateCatPicturePath,
    val validateCatNameUseCase: ValidateCatName,
    val validateCatWeightUseCase: ValidateCatWeight,
    val validateCatCoatUseCase: ValidateCatCoat,
    val validateCatRaceUseCase: ValidateCatRaceUseCase,
    val validateCatBirthdateUseCase: ValidateCatBirthdate,
    val validateCatVaccineDateUseCase: ValidateCatVaccineDate,
    val validateCatFleaDateUseCase: ValidateCatFleaDate,
    val validateCatDewormingDateUseCase: ValidateCatDewormingDate,
    val validateCatDiseases: ValidateCatDiseases
)

sealed interface ValidationImplUseCase {
    data object ValidateCatPicturePath : ValidationImplUseCase
    data object ValidateCatName : ValidationImplUseCase
    data object ValidateCatWeight : ValidationImplUseCase
    data object ValidateCatCoat : ValidationImplUseCase
    data object ValidateCatRace : ValidationImplUseCase
    data object ValidateCatBirthdate : ValidationImplUseCase
    data object ValidateCatVaccineDate : ValidationImplUseCase
    data object ValidateCatFleaDate : ValidationImplUseCase
    data object ValidateCatDewormingDate : ValidationImplUseCase
    data object ValidateCatDiseases : ValidationImplUseCase
}
