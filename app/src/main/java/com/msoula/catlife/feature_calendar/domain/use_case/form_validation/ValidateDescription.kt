package com.msoula.catlife.feature_calendar.domain.use_case.form_validation

import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult
import com.msoula.catlife.core.util.Constant

class ValidateDescription {

    fun execute(description: String?): ValidationResult {
        description?.let {
            if (it.isNotBlank() && it.matches(Constant.ONLY_NUMBER_REGEX.toRegex())) {
                return ValidationResult(false, R.string.not_only_number)
            }
        }

        return ValidationResult(true)
    }
}
