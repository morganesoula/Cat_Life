package com.msoula.catlife.feature_inventory.domain.use_cases

import com.msoula.catlife.feature_inventory.domain.use_case.ValidateQuantity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ValidateQuantityTest {

    private lateinit var validateQuantity: ValidateQuantity

    @Before
    fun setUp() {
        validateQuantity = ValidateQuantity()
    }

    @Test
    fun `contains letters, return error`() {
        val result = validateQuantity.execute("chat")
        assertEquals(result.successful, false)
    }

    @Test
    fun `contains letters and numbers, return error`() {
        val result = validateQuantity.execute("chat123")
        assertEquals(result.successful, false)
    }

    @Test
    fun `number inferior at 0, return error`() {
        val result = validateQuantity.execute("-1")
        assertEquals(result.successful, false)
    }

    @Test
    fun `number superior to 999, return error`() {
        val result = validateQuantity.execute("1000")
        assertEquals(result.successful, false)
    }

    @Test
    fun `number between 0 and 999', return success`() {
        val result = validateQuantity.execute("456")
        assertEquals(result.successful, true)
    }
}
