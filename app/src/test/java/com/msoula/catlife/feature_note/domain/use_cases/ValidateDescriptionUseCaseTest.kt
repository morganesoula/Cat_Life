package com.msoula.catlife.feature_note.domain.use_cases

import com.msoula.catlife.feature_note.domain.use_case.ValidateDescriptionUseCase
import org.junit.Assert.assertTrue
import org.junit.Test

internal class ValidateDescriptionUseCaseTest {

    private val validateDescriptionUseCase = ValidateDescriptionUseCase()

    @Test
    fun `empty description, error`() {
        val result = validateDescriptionUseCase.execute("")
        assertTrue(!result.successful)
    }

    @Test
    fun `random description, success`() {
        val result = validateDescriptionUseCase.execute("A description really interesting")
        assertTrue(result.successful)
    }
}