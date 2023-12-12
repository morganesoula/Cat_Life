package com.msoula.catlife.core.domain.use_case.crud

import com.msoula.catlife.core.data.CatDataSource
import com.msoula.catlife.core.util.Constant.CAT_BY_ID_DELETE
import com.msoula.catlife.core.util.DeleteCatByIdError
import com.msoula.catlife.core.util.Resource

class DeleteCatUseCase(private val catDataSource: CatDataSource) {
    suspend operator fun invoke(catId: Int): Resource<String> {
        return try {
            catDataSource.deleteCat(catId)
            Resource.Success(CAT_BY_ID_DELETE)
        } catch (e: DeleteCatByIdError) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }

}
