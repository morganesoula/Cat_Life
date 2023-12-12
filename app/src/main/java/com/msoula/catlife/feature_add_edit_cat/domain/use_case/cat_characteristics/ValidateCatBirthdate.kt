package com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics

import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult
import com.msoula.catlife.globalCurrentDay

class ValidateCatBirthdate {

    private val maxAgeInMs: Long = 1009843200000

    fun execute(catBirthdate: Long): ValidationResult {
        if (catBirthdate == 0L) {
            return ValidationResult(false, R.string.blank_error)
        }

        if (catBirthdate > globalCurrentDay) {
            return ValidationResult(false, R.string.cant_be_in_the_future)
        }

        if (catBirthdate < (globalCurrentDay - maxAgeInMs)) {
            return ValidationResult(false, R.string.wrong_input_format)
        }

        return ValidationResult(true)
    }
}
