package com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics

import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult

class ValidateCatWeight {

    fun execute(weight: String): ValidationResult {
        if (weight.isEmpty()) {
            return ValidationResult(
                false,
                R.string.blank_error
            )
        }

        if (weight.toFloatOrNull() == null) {
            return ValidationResult(false, R.string.wrong_input_format)
        }

        if (weight.toFloat() <= 0 || weight.toFloat() > 22) {
            return ValidationResult(
                false,
                R.string.weight_error
            )
        }

        return ValidationResult(true)
    }
}
