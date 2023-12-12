package com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics

import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult
import com.msoula.catlife.core.util.Constant

class ValidateCatDiseases {

    fun execute(diseases: String): ValidationResult {
        if (diseases.contains(Constant.NUMERIC_REGEX)) {
            return ValidationResult(false, R.string.not_only_number)
        }

        return ValidationResult(true)
    }
}