package com.msoula.catlife.core.util

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val LocalDim = compositionLocalOf { Dimensions() }
val LocalTextSize = compositionLocalOf { TextSize() }

data class Dimensions(
    val itemSpacing8: Dp = 8.dp,
    val itemSpacing12: Dp = 12.dp,
    val itemSpacing16: Dp = 16.dp,
    val itemSpacing24: Dp = 24.dp,
    val itemSpacing32: Dp = 32.dp,
    val lazyRowPadding5: Dp = 5.dp,
    val cardElevation8: Dp = 8.dp
)

data class TextSize(
    val textSize16: TextUnit = 16.sp,
    val textSize18: TextUnit = 18.sp,
    val textSize24: TextUnit = 24.sp,
    val textSize10: TextUnit = 10.sp
)
