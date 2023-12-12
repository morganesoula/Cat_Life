package com.msoula.catlife.feature_add_edit_cat.presentation

import android.net.Uri

sealed interface AddEditCatFormEvent {
    data class OnEditCatProfilePicturePathChanged(val catProfilePicturePath: Uri) : AddEditCatFormEvent
    data class EditCatGenderChanged(val gender: Boolean) : AddEditCatFormEvent
    data class EditCatNeuteredChanged(val neutered: Boolean) : AddEditCatFormEvent
    data class EditCatNameChanged(val catName: String) : AddEditCatFormEvent
    data class EditCatWeightChanged(val weight: String) : AddEditCatFormEvent
    data class EditCatCoatChanged(val coat: String) : AddEditCatFormEvent
    data class EditCatDiseasesChanged(val diseases: String) : AddEditCatFormEvent
    data class EditCatRaceChanged(val race: String, val allRaces: List<String>) : AddEditCatFormEvent
    data object OnRaceFocusChanged : AddEditCatFormEvent
    data object OnClearClick : AddEditCatFormEvent
    data object Submit : AddEditCatFormEvent
}
