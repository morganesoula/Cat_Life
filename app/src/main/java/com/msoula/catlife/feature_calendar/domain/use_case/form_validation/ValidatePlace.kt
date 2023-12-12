package com.msoula.catlife.feature_calendar.domain.use_case.form_validation

import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult
import com.msoula.catlife.core.util.Constant

class ValidatePlace {

    fun execute(place: String): ValidationResult {
        if (place.isEmpty()) {
            return ValidationResult(false, R.string.should_not_be_empty)
        }

        if (place.matches(Constant.ONLY_NUMBER_REGEX.toRegex())) {
            return ValidationResult(false, R.string.not_only_number)
        }

        return ValidationResult(true)
    }
}
