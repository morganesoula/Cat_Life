package com.msoula.catlife.feature_note.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.msoula.catlife.R
import com.msoula.catlife.core.presentation.CustomFormForHeightSpacer
import com.msoula.catlife.core.util.LocalDim
import com.msoula.catlife.core.util.LocalTextSize
import com.msoula.catlife.extension.accordingToLocale

@Composable
fun NoteRowItemComposable(
    noteDate: Long?,
    noteTitle: String,
    catName: String,
    catProfilePath: String?
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(LocalDim.current.itemSpacing16)
    ) {
        Text(
            text = noteTitle,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onSurface,
            lineHeight = LocalTextSize.current.textSize24
        )

        CustomFormForHeightSpacer(height = LocalDim.current.itemSpacing16)

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text =
                noteDate?.let {
                    (it * 1000).accordingToLocale(context)
                } ?: "",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = LocalTextSize.current.textSize10,
                modifier = Modifier
                    .weight(3f)
                    .padding(top = 20.dp),
                color = colorScheme.onSurface
            )

            if (catProfilePath.isNullOrEmpty()) {
                Text(
                    text = catName,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End,
                    color = colorScheme.onSurface
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(context)
                            .data(catProfilePath.toUri())
                            .build()
                    ),
                    contentDescription = context.getString(R.string.uri_cat_picture),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .width(30.dp)
                        .height(30.dp)
                )
            }
        }
    }
}