package com.msoula.catlife.feature_add_edit_cat.data.state

import android.net.Uri
import com.msoula.catlife.R
import commsoulacatlifedatabase.CatEntity

data class AddEditCatFormState(
    val catId: Int? = null,
    val catPictureUri: Uri? = null,
    val catPictureUriError: Int? = null,
    val catGender: Boolean = true,
    val catName: String = "",
    val catNameError: Int? = null,
    val catBirthdate: Long = 0L,
    val catBirthdateError: Int? = null,
    val catNeutered: Boolean = true,
    val catRace: String = "",
    val catRaceError: Int? = null,
    val allRaces: List<String> = emptyList(),
    val catVaccineDate: Long = 0L,
    val catVaccineDateError: Int? = null,
    val catFleaDate: Long = 0L,
    val catFleaDateError: Int? = null,
    val catDewormingDate: Long = 0L,
    val catDewormingDateError: Int? = null,
    val weight: String = "",
    val weightError: Int? = null,
    val catCoat: String = "",
    val catCoatError: Int? = null,
    val catDiseases: String = "",
    val catDiseasesError: Int? = null,
    val enableSubmit: Boolean = false,
    val currentCatId: Int? = null,
    val lastInsertedCatId: Int? = null,
    val addEditComposableTitle: Int = R.string.add_cat_title,
    val submitCatText: Int = R.string.submit_cat,
    val addEditBirthdateText: Int = R.string.add_birthdate
)

fun AddEditCatFormState.mapToCat(): CatEntity =
    CatEntity(
        birthdate = this.catBirthdate,
        coat = this.catCoat,
        dewormingDate = this.catDewormingDate,
        vaccineDate = this.catVaccineDate,
        fleaDate = this.catFleaDate,
        gender = this.catGender,
        id = this.currentCatId?.toLong() ?: -1L,
        diseases = this.catDiseases,
        name = this.catName,
        neutered = this.catNeutered,
        profilePicturePath = this.catPictureUri?.toString() ?: "",
        race = this.catRace,
        weight = this.weight.toFloat()
    )