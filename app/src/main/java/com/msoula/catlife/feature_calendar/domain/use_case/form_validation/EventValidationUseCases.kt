package com.msoula.catlife.feature_calendar.domain.use_case.form_validation

data class EventValidationUseCases(
    val validateStartDate: ValidateStartDate,
    val validateEndDate: ValidateEndDate,
    val validateDescription: ValidateDescription,
    val validateTitle: ValidateTitle,
    val validatePlace: ValidatePlace,
    val validateStartEndDate: ValidateStartEndDate,
    val validateStartEndDateTime: ValidateStartEndDateTime,
    val validateAllDayDate: ValidateAllDayDate
)

sealed interface EventImplValidationUseCases {
    data object ValidateStartDate : EventImplValidationUseCases
    data object ValidateEndDate : EventImplValidationUseCases
    data object ValidateDescription : EventImplValidationUseCases
    data object ValidateTitle : EventImplValidationUseCases
    data object ValidatePlace : EventImplValidationUseCases
    data object ValidateStartEndDateTime : EventImplValidationUseCases
}
