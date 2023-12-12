package com.msoula.catlife.di

import com.msoula.catlife.feature_inventory.domain.InventoryDataSource
import com.msoula.catlife.feature_inventory.domain.use_case.crud.DeleteInventoryItemByIdUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.GetAllInventoryItemUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.GetInventoryItemByIdUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.InsertInventoryItemUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.InventoryCrudUseCase
import com.msoula.catlife.feature_inventory.domain.use_case.crud.UpdateInventoryItemQuantityUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InventoryCrudCaseModule {
    @Provides
    @Singleton
    fun provideInventoryCrudCaseModule(repository: InventoryDataSource): InventoryCrudUseCase {
        return InventoryCrudUseCase(
            InsertInventoryItemUseCase(repository),
            DeleteInventoryItemByIdUseCase(repository),
            GetAllInventoryItemUseCase(repository),
            GetInventoryItemByIdUseCase(repository),
            UpdateInventoryItemQuantityUseCase(repository)
        )
    }
}
