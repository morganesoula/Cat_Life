package com.msoula.catlife.di

import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatBirthdate
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatCoat
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatDewormingDate
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatDiseases
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatFleaDate
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatName
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatPicturePath
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatRaceUseCase
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatVaccineDate
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatWeight
import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidationUseCases
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.EventValidationUseCases
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidateAllDayDate
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidateDescription
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidateEndDate
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidatePlace
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidateStartDate
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidateStartEndDate
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidateStartEndDateTime
import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidateTitle
import com.msoula.catlife.feature_inventory.domain.use_case.InventoryValidationUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.ValidateLabel
import com.msoula.catlife.feature_inventory.domain.use_case.ValidateQuantity
import com.msoula.catlife.feature_note.domain.use_case.NoteValidationUseCases
import com.msoula.catlife.feature_note.domain.use_case.ValidateDescriptionUseCase
import com.msoula.catlife.feature_note.domain.use_case.ValidateSelectedCatUseCase
import com.msoula.catlife.feature_note.domain.use_case.ValidateTitleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ValidationUseCaseModule {
    @Provides
    fun provideValidationUseCases() = ValidationUseCases(
        ValidateCatPicturePath(),
        ValidateCatName(),
        ValidateCatWeight(),
        ValidateCatCoat(),
        ValidateCatRaceUseCase(),
        ValidateCatBirthdate(),
        ValidateCatVaccineDate(),
        ValidateCatFleaDate(),
        ValidateCatDewormingDate(),
        ValidateCatDiseases()
    )

    @Provides
    fun provideInventoryValidationUseCases() = InventoryValidationUseCase(
        ValidateLabel(),
        ValidateQuantity()
    )

    @Provides
    fun providesEventValidationUseCases() = EventValidationUseCases(
        validateTitle = ValidateTitle(),
        validateDescription = ValidateDescription(),
        validateStartDate = ValidateStartDate(),
        validateEndDate = ValidateEndDate(),
        validatePlace = ValidatePlace(),
        validateStartEndDate = ValidateStartEndDate(),
        validateStartEndDateTime = ValidateStartEndDateTime(),
        validateAllDayDate = ValidateAllDayDate()
    )

    @Provides
    fun providesNoteValidationUseCases() = NoteValidationUseCases(
        validateSelectedCatUseCase = ValidateSelectedCatUseCase(),
        validateTitleUseCase = ValidateTitleUseCase(),
        validateDescriptionUseCase = ValidateDescriptionUseCase()
    )
}
