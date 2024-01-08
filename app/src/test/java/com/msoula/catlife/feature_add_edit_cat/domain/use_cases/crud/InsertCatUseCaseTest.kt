package com.msoula.catlife.feature_add_edit_cat.domain.use_cases.crud

import com.google.common.truth.Truth.assertThat
import com.msoula.catlife.core.domain.use_case.crud.GetAllCatsUseCase
import com.msoula.catlife.core.domain.use_case.crud.InsertCatUseCase
import com.msoula.catlife.feature_add_edit_cat.data.repository.FakeCatDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class InsertCatUseCaseTest {

    private lateinit var insertCatUseCase: InsertCatUseCase
    private lateinit var getAllCatsUseCase: GetAllCatsUseCase
    private lateinit var fakeCatRepository: FakeCatDataSource

    @Before
    fun setUp() {
        fakeCatRepository = FakeCatDataSource()
        getAllCatsUseCase = GetAllCatsUseCase(fakeCatRepository)
        insertCatUseCase = InsertCatUseCase(fakeCatRepository)
    }

    @Test
    fun `insert cat, retrieve it`() = runBlocking {
        insertCatUseCase.invoke(
            profilePicturePath = "test profile path",
            name = "Oreo",
            gender = true,
            neutered = true,
            birthdate = 1529602002000,
            weight = 3.4f,
            race = "Européen",
            coat = "Noir et blanc",
            diseases = null,
            vaccineDate = null,
            dewormingDate = null,
            fleaDate = null,
            id = 1
        )

        assertThat("Oreo").isEqualTo(getAllCatsUseCase.invoke().first()[0].name)
    }
}
