package com.msoula.catlife.core.domain.use_case

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: Int? = null
)
