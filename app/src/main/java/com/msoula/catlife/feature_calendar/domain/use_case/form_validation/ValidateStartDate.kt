package com.msoula.catlife.feature_calendar.domain.use_case.form_validation

import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult
import com.msoula.catlife.globalCurrentDay

class ValidateStartDate {
    fun execute(date: Long, endDate: Long): ValidationResult {

        if (date == 0L) {
            return ValidationResult(false, R.string.should_not_be_empty)
        }

        if (date < globalCurrentDay) {
            return ValidationResult(false, R.string.earlier_current_day_error)
        }

        if (endDate != 0L) {
            if (date > endDate) {
                return ValidationResult(false, R.string.earlier_end_date_error)
            }
        }

        return ValidationResult(true)
    }
}
