package com.msoula.catlife.core.data

import commsoulacatlifedatabase.CatEntity
import kotlinx.coroutines.flow.Flow

interface CatDataSource {
    fun getCats(): Flow<List<CatEntity>>
    suspend fun getCatById(id: Int): CatEntity?
    suspend fun insertCat(
        name: String,
        profilePicturePath: String,
        gender: Boolean,
        neutered: Boolean,
        birthdate: Long,
        weight: Float,
        race: String,
        coat: String,
        diseases: String?,
        vaccineDate: Long?,
        dewormingDate: Long?,
        fleaDate: Long?,
        id: Long?
    )

    suspend fun deleteCat(catId: Int)

    suspend fun getLastInsertedCatId(): Long?
}