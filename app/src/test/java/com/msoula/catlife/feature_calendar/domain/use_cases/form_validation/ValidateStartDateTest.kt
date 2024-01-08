package com.msoula.catlife.feature_calendar.domain.use_cases.form_validation

import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidateStartDate
import com.msoula.catlife.globalCurrentDay
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidateStartDateTest {

    private lateinit var validateStartDate: ValidateStartDate

    @Before
    fun setUp() {
        validateStartDate = ValidateStartDate()
    }

    @Test
    fun `equals to 0, return false`() {
        val result = validateStartDate.execute(0L, 0L)
        assertFalse(result.successful)
    }

    @Test
    fun `equals to random date, return true`() {
        val result = validateStartDate.execute(globalCurrentDay, 0L)
        assertTrue(result.successful)
    }
}
