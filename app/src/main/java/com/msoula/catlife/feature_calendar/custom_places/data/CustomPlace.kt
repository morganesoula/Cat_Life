package com.msoula.catlife.feature_calendar.custom_places.data

import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.gson.annotations.SerializedName

data class CustomPlace(
    val id: String,
    @SerializedName("formatted_address")
    val address: String,
    @SerializedName("geometry")
    val geometry: CustomLocation,
    val token: AutocompleteSessionToken
)
