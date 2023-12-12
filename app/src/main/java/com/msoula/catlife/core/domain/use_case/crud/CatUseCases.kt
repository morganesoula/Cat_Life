package com.msoula.catlife.core.domain.use_case.crud
data class CatUseCases(
    val insertCatUseCase: InsertCatUseCase,
    val deleteCatUseCase: DeleteCatUseCase,
    val getAllCatsUseCase: GetAllCatsUseCase,
    val getCatUseCase: GetCatUseCase,
    val fetchLastInsertedCatId: FetchLastInsertedCatIdUseCase
)
