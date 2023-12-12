package com.msoula.catlife.feature_calendar.domain.use_case.form_validation

import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult
import com.msoula.catlife.globalCurrentDay

class ValidateAllDayDate {

    fun execute(allDayDateSelected: Long): ValidationResult {
        if (allDayDateSelected < globalCurrentDay) {
            return ValidationResult(false, R.string.earlier_current_day_error)
        }

        return ValidationResult(true)
    }
}