package com.msoula.catlife.feature_calendar.domain.use_case.form_validation

import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult

class ValidateStartEndDate {

    fun execute(startDate: Long, endDate: Long): ValidationResult {
        if (startDate > endDate) {
            return ValidationResult(false, R.string.earlier_end_date_error)
        }

        return ValidationResult(true)
    }
}
