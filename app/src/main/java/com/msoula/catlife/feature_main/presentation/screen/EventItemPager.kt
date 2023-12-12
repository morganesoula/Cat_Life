package com.msoula.catlife.feature_main.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.msoula.catlife.R
import com.msoula.catlife.core.presentation.CustomFormForHeightSpacer
import com.msoula.catlife.core.presentation.CustomFormForWidthSpacer
import com.msoula.catlife.core.util.LocalDim
import com.msoula.catlife.core.util.LocalTextSize
import com.msoula.catlife.extension.toDateString

@Composable
fun EventItemPager(
    modifier: Modifier = Modifier,
    title: String,
    place: String,
    startDate: Long,
    currentDateFormatted: String
) {
    val context = LocalContext.current

    val startDateString = startDate.toDateString(context, true)

    Column(modifier = modifier.padding(10.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(start = 5.dp),
            fontWeight = FontWeight.SemiBold,
            fontSize = LocalTextSize.current.textSize18,
            color = MaterialTheme.colorScheme.onSurface
        )

        CustomFormForHeightSpacer(height = LocalDim.current.itemSpacing16)

        Row {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
            CustomFormForWidthSpacer(width = LocalDim.current.itemSpacing8)
            Text(
                text = place,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        CustomFormForHeightSpacer(height = LocalDim.current.itemSpacing16)

        Row {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
            CustomFormForWidthSpacer(width = LocalDim.current.itemSpacing8)
            Text(
                text = if (startDateString == currentDateFormatted) context.getString(R.string.today) else startDateString,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}