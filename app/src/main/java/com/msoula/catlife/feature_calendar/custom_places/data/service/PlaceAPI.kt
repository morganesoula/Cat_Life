package com.msoula.catlife.feature_calendar.custom_places.data.service

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceAPI {
    @GET("json")
    suspend fun getPlacesForGivenAddress(
        @Query("query") address: String,
        @Query("key") apiKey: String
    ): JsonObject
}