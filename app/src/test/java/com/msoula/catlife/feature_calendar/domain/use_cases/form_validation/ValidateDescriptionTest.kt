package com.msoula.catlife.feature_calendar.domain.use_cases.form_validation

import com.msoula.catlife.feature_calendar.domain.use_case.form_validation.ValidateDescription
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidateDescriptionTest {

    private lateinit var validateDescription: ValidateDescription

    @Before
    fun setUp() {
        validateDescription = ValidateDescription()
    }

    @Test
    fun `if is empty, return true`() {
        val result = validateDescription.execute("")

        assertTrue(result.successful)
    }

    @Test
    fun `contains only number, return false`() {
        val result = validateDescription.execute("123")
        assertFalse(result.successful)
    }

    @Test
    fun `contains only text, return true`() {
        val result = validateDescription.execute("test")
        assertTrue(result.successful)
    }

    @Test
    fun `contains number and text, return true`() {
        val result = validateDescription.execute("test123")
        assertTrue(result.successful)
    }

    @Test
    fun `contains whole sentence, return true`() {
        val result = validateDescription.execute("This is a test")
        assertTrue(result.successful)
    }

    @Test
    fun `contains accent, return true`() {
        val result = validateDescription.execute("This is a test with french word: être")
        assertTrue(result.successful)
    }
}
