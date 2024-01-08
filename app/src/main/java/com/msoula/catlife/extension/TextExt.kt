package com.msoula.catlife.extension

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import java.text.Normalizer

fun setToBold(fullText: String, stringToHighlight: String): AnnotatedString {
    val normalizedFullText = fullText.normalize()
    val normalizedStringToHighlight = stringToHighlight.normalize()

    return buildAnnotatedString {
        val startIndex = normalizedFullText.indexOf(normalizedStringToHighlight, ignoreCase = true)

        if (startIndex >= 0) {
            val endIndex = startIndex + normalizedStringToHighlight.length
            append(fullText.substring(0, startIndex))

            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(fullText.substring(startIndex, endIndex))
            }

            append(fullText.substring(endIndex))
        } else {
            append(fullText)
        }
    }
}

private fun String.normalize(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
    val noDiacritics = normalized.replace("\\p{Mn}+".toRegex(), "")

    val noPunctuation = noDiacritics.replace("\\p{Punct}".toRegex(), "")

    return noPunctuation.lowercase()
}