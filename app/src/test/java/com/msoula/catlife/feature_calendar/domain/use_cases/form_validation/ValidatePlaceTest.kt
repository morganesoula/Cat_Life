package com.msoula.catlife.feature_calendar.domain.use_cases.form_validation

import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidatePlace
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidatePlaceTest {

    private lateinit var validatePlace: ValidatePlace

    @Before
    fun setUp() {
        validatePlace = ValidatePlace()
    }

    @Test
    fun `empty value, return false`() {
        val result = validatePlace.execute("")
        assertFalse(result.successful)
    }

    @Test
    fun `only numbers, return false`() {
        val result = validatePlace.execute("123")
        assertFalse(result.successful)
    }

    @Test
    fun `only letters, return true`() {
        val result = validatePlace.execute("test")
        assertTrue(result.successful)
    }

    @Test
    fun `letter and number, return true`() {
        val result = validatePlace.execute("test123")
        assertTrue(result.successful)
    }

    @Test
    fun `whole sentence, return true`() {
        val result = validatePlace.execute("this is a whole sentece")
        assertTrue(result.successful)
    }

    @Test
    fun `accent, return true`() {
        val result = validatePlace.execute("être")
        assertTrue(result.successful)
    }

    @Test
    fun `sentence with number, letter and accent, return true`() {
        val result = validatePlace.execute("Test 123 être")
        assertTrue(result.successful)
    }
}
