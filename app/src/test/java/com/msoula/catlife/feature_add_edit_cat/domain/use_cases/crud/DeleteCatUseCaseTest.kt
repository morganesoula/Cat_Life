package com.msoula.catlife.feature_add_edit_cat.domain.use_cases.crud

import com.google.common.truth.Truth.assertThat
import com.msoula.catlife.core.domain.use_case.crud.DeleteCatUseCase
import com.msoula.catlife.core.domain.use_case.crud.GetCatUseCase
import com.msoula.catlife.core.domain.use_case.crud.InsertCatUseCase
import com.msoula.catlife.feature_add_edit_cat.data.repository.FakeCatDataSource
import commsoulacatlifedatabase.CatEntity
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteCatUseCaseTest {

    private lateinit var deleteCatUseCase: DeleteCatUseCase
    private lateinit var getCatUseCase: GetCatUseCase
    private lateinit var insertCatUseCase: InsertCatUseCase
    private lateinit var fakeCatRepository: FakeCatDataSource

    @Before
    fun setUp() {
        fakeCatRepository = FakeCatDataSource()
        insertCatUseCase = InsertCatUseCase(fakeCatRepository)
        getCatUseCase = GetCatUseCase(fakeCatRepository)
        deleteCatUseCase = DeleteCatUseCase(fakeCatRepository)
    }

    @Test
    fun `Insert, fetch, delete, fetch, is deleted`() = runBlocking {
        val catToDelete = CatEntity(
            profilePicturePath = "test profile path",
            id = 1,
            name = "Oreo",
            neutered = true,
            gender = true,
            birthdate = 1529602002000,
            weight = 3.4f,
            race = "Européen",
            coat = "Noir et blanc",
            dewormingDate = null,
            fleaDate = null,
            vaccineDate = null,
            diseases = null
        )

        insertCatUseCase.invoke(
            profilePicturePath = "test profile path",
            id = 1,
            name = "Oreo",
            neutered = true,
            gender = true,
            birthdate = 1529602002000,
            weight = 3.4f,
            race = "Européen",
            coat = "Noir et blanc",
            dewormingDate = null,
            fleaDate = null,
            vaccineDate = null,
            diseases = null
        )
        val catFetched = getCatUseCase.invoke(1)

        assertThat(catFetched).isNotNull()
        assertThat(catFetched?.name).isEqualTo("Oreo")

        deleteCatUseCase.invoke(catToDelete.id.toInt())
        val catFetchedAgain = getCatUseCase.invoke(1)

        assertThat(catFetchedAgain).isNull()
    }
}
