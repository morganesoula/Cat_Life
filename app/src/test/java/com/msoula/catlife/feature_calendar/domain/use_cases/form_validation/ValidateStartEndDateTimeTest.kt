package com.msoula.catlife.feature_calendar.domain.use_cases.form_validation

import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidateStartEndDateTime
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalTime

class ValidateStartEndDateTimeTest {

    private lateinit var validateStartEndDateTime: ValidateStartEndDateTime

    private val startDate = 1236242346135L // 05/03/2009
    private val endDate = 1246242346135L // 29/06/2009
    private val currentTime = LocalTime.now().hour.toString() + ":" + LocalTime.now().minute.toString().padStart(2, '0')

    @Before
    fun setUp() {
        validateStartEndDateTime = ValidateStartEndDateTime()
    }

    @Test
    fun `start date is superior to end date, return false`() {
        val result = validateStartEndDateTime.execute(endDate, startDate, currentTime, currentTime)

        assertFalse(result.successful)
    }

    @Test
    fun `start date is inferior to end date, return true`() {
        val result = validateStartEndDateTime.execute(startDate, endDate = endDate,
            currentTime, currentTime
        )

        assertTrue(result.successful)
    }

    @Test
    fun `start date is inferior to end date, start time is superior to end time, return true`() {
        val result = validateStartEndDateTime.execute(startDate, endDate, LocalTime.of(2, 13).toString(), LocalTime.of(1, 5).toString())

        assertTrue(result.successful)
    }

    @Test
    fun `start date is inferior to end date, start time is inferior to end time, return true`() {
        val result = validateStartEndDateTime.execute(startDate, endDate, LocalTime.of(1, 5).toString(), LocalTime.of(3, 10).toString())

        assertTrue(result.successful)
    }

    @Test
    fun `start date is inferior to end date, start time is equal to end time, return true`() {
        val result = validateStartEndDateTime.execute(startDate, endDate, currentTime,
            currentTime
        )

        assertTrue(result.successful)
    }

    @Test
    fun `start date is equal to end date, start time is equal to end time, return false`() {
        val result = validateStartEndDateTime.execute(startDate, startDate, startTime = LocalTime.of(1, 5).toString(), endTime = LocalTime.of(1, 5).toString())

        assertFalse(result.successful)
    }
}
