package com.msoula.catlife.di

import android.app.Application
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.msoula.CatLifeDatabase
import com.msoula.catlife.core.adapter.QuantityAdapter
import com.msoula.catlife.core.adapter.WeightAdapter
import com.msoula.catlife.core.data.CatDataSource
import com.msoula.catlife.core.domain.CatDataSourceSQLImpl
import com.msoula.catlife.core.util.Constant
import com.msoula.catlife.feature_calendar.data.repository.CalendarEventDataSource
import com.msoula.catlife.feature_calendar.domain.CalendarEventDataSourceSQLImpl
import com.msoula.catlife.feature_inventory.data.repository.InventoryDataSourceSQLImpl
import com.msoula.catlife.feature_inventory.domain.InventoryDataSource
import com.msoula.catlife.feature_note.data.repository.NoteDataSource
import com.msoula.catlife.feature_note.domain.NoteDataSourceSQLImpl
import commsoulacatlifedatabase.CatEntity
import commsoulacatlifedatabase.InventoryItemEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CatLifeModule {

    @Provides
    @Singleton
    fun provideSqlDriver(app: Application): SqlDriver {
        return AndroidSqliteDriver(
            schema = CatLifeDatabase.Schema,
            context = app.applicationContext,
            name = Constant.DATABASE_NAME
        )
    }

    @Provides
    @Singleton
    fun provideCatLifeSQLDatabase(driver: SqlDriver): CatLifeDatabase {
        return CatLifeDatabase(
            driver = driver,
            catEntityAdapter = CatEntity.Adapter(WeightAdapter()),
            inventoryItemEntityAdapter = InventoryItemEntity.Adapter(QuantityAdapter())
        )
    }

    @Provides
    @Singleton
    fun provideCatDataSource(driver: SqlDriver): CatDataSource =
        CatDataSourceSQLImpl(provideCatLifeSQLDatabase(driver))

    @Provides
    @Singleton
    fun provideInventoryDataSource(driver: SqlDriver): InventoryDataSource =
        InventoryDataSourceSQLImpl(provideCatLifeSQLDatabase(driver))

    @Provides
    @Singleton
    fun provideNoteDataSource(driver: SqlDriver): NoteDataSource =
        NoteDataSourceSQLImpl(provideCatLifeSQLDatabase(driver))

    @Provides
    @Singleton
    fun provideCalendarDataSource(driver: SqlDriver): CalendarEventDataSource =
        CalendarEventDataSourceSQLImpl(provideCatLifeSQLDatabase(driver))
}
