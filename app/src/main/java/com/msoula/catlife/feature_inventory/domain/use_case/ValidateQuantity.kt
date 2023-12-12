package com.msoula.catlife.feature_inventory.domain.use_case

import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult
import com.msoula.catlife.core.util.Constant

class ValidateQuantity {

    fun execute(quantity: String): ValidationResult {

        if (quantity.isEmpty()) {
            return ValidationResult(
                false,
                R.string.blank_error
            )
        }

        if (!quantity.matches(Constant.ONLY_NUMBER_REGEX.toRegex())) {
            return ValidationResult(
                false,
                R.string.number_only
            )
        }

        if (quantity.toInt() < 0) {
            return ValidationResult(
                false,
                R.string.quantity_error_inferior
            )
        }

        if (quantity.toInt() > 999) {
            return ValidationResult(
                false,
                R.string.quantity_error_superior
            )
        }

        return ValidationResult(true)
    }
}
