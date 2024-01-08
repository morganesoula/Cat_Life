package com.msoula.catlife.feature_calendar.domain.use_cases.form_validation

import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidateStartEndDate
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidateStartEndDateTest {

    private lateinit var validateStartEndDate: ValidateStartEndDate
    private val startDate = 1236242346135L // 05/03/2009
    private val endDate = 1246242346135L // 29/06/2009

    @Before
    fun setUp() {
        validateStartEndDate = ValidateStartEndDate()
    }

    @Test
    fun `end before start, return false`() {
        val result = validateStartEndDate.execute(endDate, startDate)
        assertFalse(result.successful)
    }

    @Test
    fun `start equals end, return true`() {
        val result = validateStartEndDate.execute(startDate, startDate)
        assertTrue(result.successful)
    }

    @Test
    fun `start before end, return true`() {
        val result = validateStartEndDate.execute(startDate, endDate)
        assertTrue(result.successful)
    }
}
