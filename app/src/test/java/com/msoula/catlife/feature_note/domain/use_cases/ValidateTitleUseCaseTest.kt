package com.msoula.catlife.feature_note.domain.use_cases

import com.msoula.catlife.feature_note.domain.use_case.ValidateTitleUseCase
import org.junit.Assert.assertTrue
import org.junit.Test

internal class ValidateTitleUseCaseTest {

    private val validateTitleUseCase = ValidateTitleUseCase()

    @Test
    fun `empty title, error`() {
        val result = validateTitleUseCase.execute("")
        assertTrue(!result.successful)
    }

    @Test
    fun `random title, success`() {
        val result = validateTitleUseCase.execute("Random title for test")
        assertTrue(result.successful)
    }
}