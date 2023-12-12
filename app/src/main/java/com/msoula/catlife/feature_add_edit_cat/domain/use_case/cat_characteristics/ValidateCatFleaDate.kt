package com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics

import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult
import com.msoula.catlife.globalCurrentDay

class ValidateCatFleaDate {

    fun execute(fleaDate: Long, birthdate: Long): ValidationResult {
        val isFleaDateValid = fleaDate != 0L
        val isBirthDateValid = birthdate != 0L

        return when {
            isFleaDateValid -> {
                if (isBirthDateValid && birthdate > fleaDate) {
                    ValidationResult(false, R.string.cat_not_birthed)
                } else if (fleaDate > globalCurrentDay) {
                    ValidationResult(false, R.string.cant_be_in_the_future)
                } else {
                    ValidationResult(true)
                }
            }
            else -> ValidationResult(true)
        }
    }
}
