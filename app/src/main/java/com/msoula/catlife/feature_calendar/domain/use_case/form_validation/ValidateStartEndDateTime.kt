package com.msoula.catlife.feature_calendar.domain.use_case.form_validation

import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ValidateStartEndDateTime {

    fun execute(
        startDate: Long,
        endDate: Long,
        startTime: String,
        endTime: String
    ): ValidationResult {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val localStartTime = LocalTime.parse(startTime, timeFormatter)
        val localEndTime = LocalTime.parse(endTime, timeFormatter)

        if (startDate > endDate) {
            return ValidationResult(false, R.string.earlier_end_date_error)
        }

        if (startDate == endDate && localStartTime == localEndTime) {
            return ValidationResult(false, R.string.same_time_error)
        }

        if (startDate >= endDate) {
            if (localStartTime > localEndTime) {
                return ValidationResult(
                    false,
                    R.string.earlier_end_time_error
                )
            }
        }

        return ValidationResult(true)
    }
}
