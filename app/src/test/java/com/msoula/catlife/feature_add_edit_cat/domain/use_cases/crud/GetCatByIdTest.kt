package com.msoula.catlife.feature_add_edit_cat.domain.use_cases.crud

import com.google.common.truth.Truth.assertThat
import com.msoula.catlife.core.domain.use_case.crud.GetCatUseCase
import com.msoula.catlife.feature_add_edit_cat.data.repository.FakeCatDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetCatByIdTest {

    private lateinit var getCatUseCases: GetCatUseCase
    private lateinit var fakeCatRepository: FakeCatDataSource

    @Before
    fun setUp() {
        fakeCatRepository = FakeCatDataSource()
        getCatUseCases = GetCatUseCase(fakeCatRepository)

        runBlocking {
            fakeCatRepository.insertCat(
                profilePicturePath = "test profile path",
                id = 1,
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
                fleaDate = null
            )

            fakeCatRepository.insertCat(
                profilePicturePath = "test profile path",
                id = 2,
                name = "Douffy",
                gender = false,
                neutered = true,
                birthdate = 1403371602000,
                weight = 2.8f,
                race = "Siamoise",
                coat = "Seal point",
                diseases = null,
                vaccineDate = null,
                dewormingDate = null,
                fleaDate = null
            )
        }
    }

    @Test
    fun `Fetch cats by ID, known cats`() = runBlocking {
        val cat = getCatUseCases.invoke(1)

        assertThat(cat).isNotNull()
        assertThat(cat?.name).isEqualTo("Oreo")

        val catTwo = getCatUseCases.invoke(2)

        assertThat(catTwo?.gender).isFalse()
    }
}
