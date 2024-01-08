package com.msoula.catlife.feature_add_edit_cat.data.repository

import com.msoula.catlife.core.data.CatDataSource
import commsoulacatlifedatabase.CatEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCatDataSource : CatDataSource {

    private val cats = mutableListOf<CatEntity>()
    override fun getCats(): Flow<List<CatEntity>> = flow { emit(cats) }
    override suspend fun getCatById(id: Int): CatEntity? = cats.find { it.id == id.toLong() }
    override suspend fun insertCat(
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
    ) {
        cats.add(
            CatEntity(
                id ?: 0,
                name,
                profilePicturePath,
                gender,
                neutered,
                birthdate,
                weight,
                race,
                coat,
                diseases,
                vaccineDate,
                dewormingDate,
                fleaDate
            )
        )
    }

    override suspend fun deleteCat(catId: Int) {
        cats.removeIf { it.id == catId.toLong() }
    }

    override suspend fun getLastInsertedCatId(): Long? {
        return cats.lastOrNull()?.id
    }
}
