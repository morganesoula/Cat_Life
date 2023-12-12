package com.msoula.catlife.feature_cat_detail.data.state

data class CatDetailState(
    val catId: Int = 0,
    val catName: String = "",
    val catProfilePath: String? = null,
    val catBirthdate: Long = 0L,
    val gender: Boolean = true,
    val neutered: Boolean = true,
    val weight: Float = 0f,
    val lastVaccineDate: Long? = null,
    val lastFleaDate: Long? = null,
    val lastDewormingDate: Long? = null,
    val race: String = "",
    val fur: String = "",
    val diseases: String? = null,
    val openDeleteAlert: Boolean = false
)
