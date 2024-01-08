package com.msoula.catlife.feature_calendar.domain.use_cases.form_validation

import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidateEndDate
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidateEndDateTest {

    private lateinit var validateEndDate: ValidateEndDate

    @Before
    fun setUp() {
        validateEndDate = ValidateEndDate()
    }

    @Test
    fun `equals to 0, return false`() {
        val result = validateEndDate.execute(0L, 0L)
        assertFalse(result.successful)
    }

    @Test
    fun `equals to random date, return true`() {
        val result = validateEndDate.execute(1236242346135L, 1136242346135L)
        assertTrue(result.successful)
    }
}
