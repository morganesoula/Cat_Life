package com.msoula.catlife.di

import com.msoula.catlife.feature_calendar.custom_places.data.service.PlaceAPI
import com.msoula.catlife.feature_calendar.custom_places.domain.use_case.FetchPlaceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlaceUseCaseModule {
    @Provides
    @Singleton
    fun provideFetchPlaceUseCase(placeAPI: PlaceAPI): FetchPlaceUseCase =
        FetchPlaceUseCase(placeAPI)
}