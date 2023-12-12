package com.msoula.catlife.feature_add_edit_cat.presentation


sealed interface AddEditCatDateEvent {
    data object OnBirthdateChanged : AddEditCatDateEvent
    data object OnVaccineChanged : AddEditCatDateEvent
    data object OnDewormingChanged : AddEditCatDateEvent
    data object OnFleaChanged : AddEditCatDateEvent
}
