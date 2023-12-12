package com.msoula.catlife.feature_add_edit_cat.domain.use_case.cat_characteristics

import android.net.Uri
import com.msoula.catlife.R
import com.msoula.catlife.core.domain.use_case.ValidationResult

class ValidateCatPicturePath {

    fun execute(catProfilePath: Uri?): ValidationResult {

        if (catProfilePath == null) {
            return ValidationResult(false, R.string.blank_error)
        }

        return ValidationResult(true)
    }
}