package com.msoula.catlife.feature_add_edit_cat.domain.use_cases.cat_characteristics

import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatVaccineDate
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class ValidateVaccineDateTest {
    private lateinit var validateVaccine: ValidateCatVaccineDate

    @Before
    fun setUp() {
        validateVaccine = ValidateCatVaccineDate()
    }

    @Test
    fun `vaccine date value is empty, returns no error`() {
        val result = validateVaccine.execute(0, 1383474363980)
        assertEquals(result.successful, true)
    }

    @Test
    fun `vaccine date value is not 0 and ulterior to current date, returns error`() {
        val result = validateVaccine.execute(1718043278000, 1383474363980)
        assertEquals(result.successful, false)
    }

    @Test
    fun `vaccine date value is inferior by 34 years from now on, returns error`() {
        val result = validateVaccine.execute(581969678000, 1383474363980)
        assertEquals(result.successful, false)
    }

    @Test
    fun `vaccine date value is set 4 years from now on, returns true`() {
        val result = validateVaccine.execute(1528654478000, 1383474363980)
        assertEquals(result.successful, true)
    }
}
