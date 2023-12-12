package com.msoula.catlife.feature_cat_detail.presentation.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.msoula.catlife.R

@Composable
fun DeleteDataAlertDialog(
    dismissDialog: () -> Unit,
    deleteElement: () -> Unit,
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = { dismissDialog() },
        title = {
            Text(
                text = context.getString(R.string.delete_confirmation),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    deleteElement()
                }
            ) {
                Text(
                    text = context.getString(R.string.yes),
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    dismissDialog()
                }
            ) {
                Text(
                    text = context.getString(R.string.no),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                )
            }
        }
    )
}
