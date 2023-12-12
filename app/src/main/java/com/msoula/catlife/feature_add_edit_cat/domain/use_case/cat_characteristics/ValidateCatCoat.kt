package com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics

import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult
import com.msoula.catlife.core.util.Constant

class ValidateCatCoat {

    fun execute(catCoat: String): ValidationResult {

        if (catCoat.isBlank()) {
            return ValidationResult(
                false, R.string.blank_error
            )
        }

        if (catCoat.contains(Constant.NUMERIC_REGEX.toRegex())) {
            return ValidationResult(
                false, R.string.no_number_allowed
            )
        }

        return ValidationResult(true)
    }
}
