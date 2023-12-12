package com.msoula.catlife.core.domain.use_case.crud

import com.msoula.catlife.core.data.CatDataSource
import commsoulacatlifedatabase.CatEntity
import kotlinx.coroutines.flow.Flow

class GetAllCatsUseCase(private val catDataSource: CatDataSource) {

    operator fun invoke(): Flow<List<CatEntity>> = catDataSource.getCats()
}
