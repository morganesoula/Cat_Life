package com.msoula.catlife.feature_inventory.domain.use_case

import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult
import com.msoula.catlife.core.util.Constant

class ValidateLabel {

    fun execute(label: String): ValidationResult {
        if (label.isEmpty()) {
            return ValidationResult(
                false, R.string.blank_error
            )
        }

        if (label.matches(Constant.ONLY_NUMBER_REGEX.toRegex())) {
            return ValidationResult(
                false, R.string.not_only_number
            )
        }

        return ValidationResult(true)
    }
}
