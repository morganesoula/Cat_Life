package com.msoula.catlife.feature_calendar.domain.use_case.form_validation

import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult

class ValidateEndDate {

    fun execute(endDate: Long, startDate: Long): ValidationResult {
        if (endDate == 0L) {
            return ValidationResult(false, R.string.should_not_be_empty)
        }

        if (startDate > endDate) {
            return ValidationResult(false, R.string.earlier_end_date_error)
        }

        return ValidationResult(true)
    }
}
