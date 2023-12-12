package com.msoula.catlife.core.domain

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.msoula.CatLifeDatabase
import com.msoula.catlife.core.data.CatDataSource
import commsoulacatlifedatabase.CatEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CatDataSourceSQLImpl(
    db: CatLifeDatabase
) : CatDataSource {

    private val queries = db.catQueries

    override suspend fun getCatById(id: Int): CatEntity? {
        return withContext(Dispatchers.IO) {
            queries.getCatById(id.toLong()).executeAsOneOrNull()
        }
    }

    override fun getCats(): Flow<List<CatEntity>> {
        return queries.getAllCats().asFlow().mapToList(Dispatchers.IO)
    }

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
        withContext(Dispatchers.IO) {
            queries.insertCat(
                id,
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
        }
    }

    override suspend fun deleteCat(catId: Int) {
        withContext(Dispatchers.IO) {
            queries.deleteCat(catId.toLong())
        }
    }

    override suspend fun getLastInsertedCatId(): Long? =
        queries.lastInsertRowId().executeAsOneOrNull()
}