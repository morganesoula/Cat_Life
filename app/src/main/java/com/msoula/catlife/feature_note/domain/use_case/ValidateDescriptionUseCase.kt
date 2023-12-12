package com.msoula.catlife.feature_note.domain.use_case

import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult

class ValidateDescriptionUseCase {

    fun execute(description: String): ValidationResult {
        if (description.isEmpty()) {
            return ValidationResult(false, R.string.should_not_be_empty)
        }

        return ValidationResult(true)
    }
}