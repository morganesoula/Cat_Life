package com.msoula.catlife.feature_inventory.domain.use_cases

import com.msoula.catlife.feature_inventory.domain.use_case.ValidateLabel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ValidateLabelTest {

    private lateinit var validateLabelTest: ValidateLabel

    @Before
    fun setUp() {
        validateLabelTest = ValidateLabel()
    }

    @Test
    fun `is Empty, return error`() {
        val result = validateLabelTest.execute("")

        assertEquals(result.successful, false)
    }

    @Test
    fun `use only numbers, return error`() {
        val result = validateLabelTest.execute("123")
        assertEquals(result.successful, false)
    }

    @Test
    fun `use only letters, return success`() {
        val result = validateLabelTest.execute("chat")
        assertEquals(result.successful, true)
    }

    @Test
    fun `use accent, return success`() {
        val result = validateLabelTest.execute("litière et pâtée")
        assertEquals(result.successful, true)
    }

    @Test
    fun `use numbers and letters, return success`() {
        val result = validateLabelTest.execute("chat123")
        assertEquals(result.successful, true)
    }
}
