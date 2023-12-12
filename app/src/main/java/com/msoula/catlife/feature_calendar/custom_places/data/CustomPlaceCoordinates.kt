package com.msoula.catlife.feature_calendar.custom_places.data

import com.google.gson.annotations.SerializedName

data class CustomLocation(
    @SerializedName("location")
    val location: CustomPlaceCoordinates
)

data class CustomPlaceCoordinates(
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lng")
    val longitude: Double
)
