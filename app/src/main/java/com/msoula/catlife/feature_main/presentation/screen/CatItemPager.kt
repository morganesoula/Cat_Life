package com.msoula.catlife.feature_main.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.msoula.catlife.R
import com.msoula.catlife.core.util.LocalTextSize

@Composable
fun CatItemPager(
    modifier: Modifier = Modifier,
    profilePath: String?,
    catName: String,
    catId: Int,
    openCatDetail: (Int) -> Unit
) {
    val context = LocalContext.current
    val data = profilePath?.takeIf { !it.contains("null") } ?: R.drawable.catlife_placeholder

    Box(modifier = modifier) {
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(data)
                    .build()
            ),
            contentDescription = null,
            modifier = modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        TextButton(
            modifier = modifier
                .width(60.dp)
                .height(60.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            onClick = {
                openCatDetail(catId)
            },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.surface
            ),
            border = BorderStroke(1.dp, colorScheme.onSurface)
        ) {
            Text(
                text = if (catName.isNotEmpty()) catName.first().uppercase() else "",
                fontSize = LocalTextSize.current.textSize24,
                color = colorScheme.onSurface
            )
        }
    }
}
