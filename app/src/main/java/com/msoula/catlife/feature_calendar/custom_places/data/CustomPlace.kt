package com.msoula.catlife.feature_calendar.custom_places.data

import com.google.gson.annotations.SerializedName

data class CustomPlace(
    val id: Int,
    @SerializedName("formatted_address")
    val address: String,
    @SerializedName("geometry")
    val geometry: CustomLocation
)
