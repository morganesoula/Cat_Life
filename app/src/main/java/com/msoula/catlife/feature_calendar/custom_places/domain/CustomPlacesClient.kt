package com.msoula.catlife.feature_calendar.custom_places.domain

import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse

interface CustomPlacesClient {

    fun findAutocompletePredictions(request: FindAutocompletePredictionsRequest): Task<FindAutocompletePredictionsResponse>
}