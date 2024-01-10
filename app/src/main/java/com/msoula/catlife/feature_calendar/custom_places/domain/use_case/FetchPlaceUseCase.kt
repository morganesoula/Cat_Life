package com.msoula.catlife.feature_calendar.custom_places.domain.use_case

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.msoula.catlife.feature_calendar.custom_places.data.CustomLocation
import com.msoula.catlife.feature_calendar.custom_places.data.CustomPlace
import com.msoula.catlife.feature_calendar.custom_places.data.CustomPlaceCoordinates
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FetchPlaceUseCase @Inject constructor(
    private val placesClient: PlacesClient
) {

    suspend operator fun invoke(
        addressSearched: String
    ): List<CustomPlace> = suspendCoroutine { continuation ->
        val token = AutocompleteSessionToken.newInstance()

        val request = FindAutocompletePredictionsRequest.builder()
            .setTypesFilter(listOf(PlaceTypes.ADDRESS))
            .setSessionToken(token)
            .setQuery(addressSearched)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                val customPlaces = response.autocompletePredictions.map { prediction ->
                    CustomPlace(
                        prediction.placeId,
                        prediction.getFullText(null).toString(),
                        CustomLocation(CustomPlaceCoordinates(0.0, 0.0)),
                        token
                    )
                }
                continuation.resume(customPlaces)
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }

    suspend fun getPlaceLatLng(
        placeId: String,
        token: AutocompleteSessionToken,
        listener: (LatLng) -> Unit
    ): LatLng =
        suspendCoroutine { continuation: Continuation<LatLng> ->
            val request = FetchPlaceRequest
                .builder(
                    placeId,
                    listOf(Place.Field.LAT_LNG)
                )
                .setSessionToken(token)
                .build()

            placesClient.fetchPlace(request)
                .addOnSuccessListener { response: FetchPlaceResponse ->
                    continuation.let {
                        Log.d("CATLIFE", "LatLng is: ${response.place.latLng}")
                        response.place.latLng?.let { listener(it) }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("CATLIFE", "Error in fetching place LatLng - $exception")
                    continuation.resumeWithException(exception)
                }
        }
}