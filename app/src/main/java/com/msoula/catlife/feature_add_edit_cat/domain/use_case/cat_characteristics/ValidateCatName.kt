package com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics

import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult
import com.msoula.catlife.core.util.Constant

class ValidateCatName {
    fun execute(catName: String): ValidationResult {
        if (catName.isBlank()) {
            return ValidationResult(
                false, R.string.blank_error
            )
        }

        if (catName.contains(Constant.NUMERIC_REGEX.toRegex())) {
            return ValidationResult(
                false, R.string.no_number_allowed
            )
        }

        return ValidationResult(true)
    }
}
