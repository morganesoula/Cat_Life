package com.msoula.catlife.core.domain.use_case.crud

import com.msoula.catlife.core.data.CatDataSource

class FetchLastInsertedCatIdUseCase(private val catDataSource: CatDataSource) {

    suspend operator fun invoke(): Long? = catDataSource.getLastInsertedCatId()
}