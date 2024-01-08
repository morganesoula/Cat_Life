package com.msoula.catlife.feature_add_edit_cat.domain.use_cases.cat_characteristics

import com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics.ValidateCatDewormingDate
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class ValidateDewormingTest {

    private lateinit var validateDeworming: ValidateCatDewormingDate

    @Before
    fun setUp() {
        validateDeworming = ValidateCatDewormingDate()
    }

    @Test
    fun `deworming value is empty, returns no error`() {
        val result = validateDeworming.execute(0, 1383474363980)
        assertEquals(result.successful, true)
    }

    @Test
    fun `deworming value is not 0 and ulterior to current date, returns error`() {
        val result = validateDeworming.execute(1718043278000, 1383474363980)
        assertEquals(result.successful, false)
    }

    @Test
    fun `deworming value is inferior by 34 years from now on, returns error`() {
        val result = validateDeworming.execute(581969678000, 1383474363980)
        assertEquals(result.successful, false)
    }

    @Test
    fun `deworming value is set 4 years from now on, returns true`() {
        val result = validateDeworming.execute(1528654478000, 1383474363980)
        assertEquals(result.successful, true)
    }
}
