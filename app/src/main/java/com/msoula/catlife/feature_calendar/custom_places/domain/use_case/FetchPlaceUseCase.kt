package com.msoula.catlife.feature_calendar.custom_places.domain.use_case

import com.google.gson.Gson
import com.msoula.catlife.feature_calendar.custom_places.data.CustomPlace
import com.msoula.catlife.feature_calendar.custom_places.data.service.PlaceAPI
import javax.inject.Inject

class FetchPlaceUseCase @Inject constructor(private val placeAPI: PlaceAPI) {

    suspend operator fun invoke(
        address: String,
        apiKey: String
    ): List<CustomPlace> {
        val data: MutableList<CustomPlace> = mutableListOf()

        val response = placeAPI.getPlacesForGivenAddress(address, apiKey)
            .get("results")?.asJsonArray

        response?.let { array ->
            if (!array.isEmpty) {
                for (element in array) {
                    data.add(
                        Gson().fromJson(
                            element.asJsonObject,
                            CustomPlace::class.java
                        )
                    )
                }
            }
        }

        return data
    }
}