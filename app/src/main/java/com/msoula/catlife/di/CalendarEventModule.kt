package com.msoula.catlife.di

import com.msoula.catlife.feature_calendar.data.repository.CalendarEventDataSource
import com.msoula.catlife.feature_calendar.domain.use_case.CalendarEventUseCases
import com.msoula.catlife.feature_calendar.domain.use_case.FetchLastInsertedEventIdUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.DeleteCalendarEventByIdUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetAllEventUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetCalendarEventByIdUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetCalendarEventsOnDateDUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.GetNextEventsUseCase
import com.msoula.catlife.feature_calendar.domain.use_case.crud.InsertCalendarEventUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CalendarEventModule {
    @Provides
    @Singleton
    fun provideCalendarEventUseCases(repository: CalendarEventDataSource): CalendarEventUseCases {
        return CalendarEventUseCases(
            InsertCalendarEventUseCase(repository),
            GetCalendarEventsOnDateDUseCase(repository),
            GetCalendarEventByIdUseCase(repository),
            DeleteCalendarEventByIdUseCase(repository),
            GetNextEventsUseCase(repository),
            GetAllEventUseCase(repository),
            FetchLastInsertedEventIdUseCase(repository)
        )
    }
}
