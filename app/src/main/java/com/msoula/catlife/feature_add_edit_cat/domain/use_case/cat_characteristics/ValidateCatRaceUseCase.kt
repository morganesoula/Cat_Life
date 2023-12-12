package com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics

import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult
import com.msoula.catlife.core.util.Constant

class ValidateCatRaceUseCase {

    fun execute(catRace: String): ValidationResult {
        if (catRace.isEmpty()) {
            return ValidationResult(false, R.string.blank_error)
        }

        if (catRace.contains(Constant.NUMERIC_REGEX.toRegex())) {
            return ValidationResult(
                false, R.string.no_number_allowed
            )
        }

        return ValidationResult(true)
    }
}