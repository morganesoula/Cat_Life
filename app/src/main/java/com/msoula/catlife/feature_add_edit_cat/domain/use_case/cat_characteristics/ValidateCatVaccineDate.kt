package com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics

import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult
import com.msoula.catlife.globalCurrentDay

class ValidateCatVaccineDate {

    fun execute(vaccineDate: Long, birthdate: Long): ValidationResult {
        val isVaccineDateValid = vaccineDate != 0L
        val isBirthDateValid = birthdate != 0L

        return when {
            isVaccineDateValid -> {
                if (isBirthDateValid && birthdate > vaccineDate) {
                    ValidationResult(false, R.string.cat_not_birthed)
                } else if (vaccineDate > globalCurrentDay) {
                    ValidationResult(false, R.string.cant_be_in_the_future)
                } else {
                    ValidationResult(true)
                }
            }
            else -> ValidationResult(true)
        }
    }
}
