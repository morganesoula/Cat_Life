package com.msoula.catlife.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.msoula.catlife.R

val fonts = FontFamily(
    Font(R.font.jost_black, weight = FontWeight.Normal),
    Font(R.font.jost_black_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(R.font.jost_bold, weight = FontWeight.Bold),
    Font(R.font.jost_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),
    Font(R.font.jost_extra_bold, weight = FontWeight.ExtraBold),
    Font(R.font.jost_extra_bold_italic, weight = FontWeight.ExtraBold, style = FontStyle.Italic),
    Font(R.font.jost_light, weight = FontWeight.Light),
    Font(R.font.jost_light_italic, weight = FontWeight.Light, style = FontStyle.Italic),
    Font(R.font.jost_extra_light, weight = FontWeight.ExtraLight),
    Font(R.font.jost_extra_light_italic, weight = FontWeight.ExtraLight, style = FontStyle.Italic),
    Font(R.font.jost_thin, weight = FontWeight.Thin),
    Font(R.font.jost_thin_italic, weight = FontWeight.Thin, style = FontStyle.Italic),
    Font(R.font.jost_medium, weight = FontWeight.Medium),
    Font(R.font.jost_medium_italic, weight = FontWeight.Medium, style = FontStyle.Italic),
    Font(R.font.jost_semibold, weight = FontWeight.SemiBold),
    Font(R.font.jost_semibold_italic, weight = FontWeight.SemiBold, style = FontStyle.Italic)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),

    headlineSmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    )
)