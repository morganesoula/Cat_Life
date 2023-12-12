package com.msoula.catlife.core.domain.use_case.crud

import com.msoula.catlife.core.data.CatDataSource
import com.msoula.catlife.core.util.AddEditCatError
import com.msoula.catlife.core.util.Constant
import com.msoula.catlife.core.util.Resource

class InsertCatUseCase(private val catDataSource: CatDataSource) {
    suspend operator fun invoke(
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
    ): Resource<String> {
        return try {
            catDataSource.insertCat(
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
                fleaDate,
                id
            )
            Resource.Success(Constant.CAT_ADDED_UPDATED)
        } catch (e: AddEditCatError) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }
}
