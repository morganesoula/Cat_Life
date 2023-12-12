package com.msoula.catlife.core.domain.use_case.crud

import com.msoula.catlife.core.data.CatDataSource

class GetCatUseCase(private val catDataSource: CatDataSource) {
    suspend operator fun invoke(id: Int) = catDataSource.getCatById(id)
}
