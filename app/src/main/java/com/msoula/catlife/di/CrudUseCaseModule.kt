package com.msoula.catlife.di

import com.msoula.catlife.core.data.CatDataSource
import com.msoula.catlife.core.domain.use_case.crud.CatUseCases
import com.msoula.catlife.core.domain.use_case.crud.DeleteCatUseCase
import com.msoula.catlife.core.domain.use_case.crud.FetchLastInsertedCatIdUseCase
import com.msoula.catlife.core.domain.use_case.crud.GetAllCatsUseCase
import com.msoula.catlife.core.domain.use_case.crud.GetCatUseCase
import com.msoula.catlife.core.domain.use_case.crud.InsertCatUseCase
import com.msoula.catlife.feature_calendar.data.repository.CalendarEventDataSource
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetNextEventsUseCase
import com.msoula.catlife.feature_inventory.domain.InventoryDataSource
import com.msoula.catlife.feature_main.domain.use_case.GetItemsWithLowQuantityUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CrudUseCaseModule {
    @Provides
    @Singleton
    fun provideCatCRUDUseCases(repository: CatDataSource): CatUseCases {
        return CatUseCases(
            InsertCatUseCase(repository),
            DeleteCatUseCase(repository),
            GetAllCatsUseCase(repository),
            GetCatUseCase(repository),
            FetchLastInsertedCatIdUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideItemLowUseCase(repository: InventoryDataSource): GetItemsWithLowQuantityUseCase =
        GetItemsWithLowQuantityUseCase(repository)

    @Provides
    @Singleton
    fun provideNextEventsToCome(repository: CalendarEventDataSource): GetNextEventsUseCase =
        GetNextEventsUseCase(repository)
}
