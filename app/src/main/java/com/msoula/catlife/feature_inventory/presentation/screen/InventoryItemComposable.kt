package com.msoula.catlife.feature_inventory.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import com.msoula.catlife.core.util.LocalDim
import com.msoula.catlife.core.util.LocalTextSize

@Composable
fun InventoryItemComposable(
    modifier: Modifier = Modifier,
    inventoryItemId: Int?,
    label: String = "",
    quantity: String = "0",
    decrementQuantity: (itemId: Int) -> Unit,
    incrementQuantity: (itemId: Int) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(LocalDim.current.itemSpacing16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(6f),
            fontSize = LocalTextSize.current.textSize18,
            color = colorScheme.onSurface,
            lineHeight = LocalTextSize.current.textSize24
        )

        TextButton(
            enabled = quantity.toInt() > 0,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.secondary),
            onClick = { inventoryItemId?.let { decrementQuantity(inventoryItemId) } }
        ) {
            Icon(
                Icons.Default.Remove,
                modifier = Modifier.fillMaxSize(1.0f),
                contentDescription = null,
                tint = colorScheme.onPrimary
            )
        }

        Text(
            text = quantity,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            color = colorScheme.onSurface
        )

        TextButton(
            enabled = quantity.toInt() < 999,
            modifier = Modifier
                .weight(1f)
                .semantics { contentDescription = "increment quantity" },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.secondary
            ),
            onClick = { inventoryItemId?.let { incrementQuantity(inventoryItemId) } }
        ) {
            Icon(
                Icons.Default.Add,
                modifier = Modifier.fillMaxSize(1.0f),
                contentDescription = null,
                tint = colorScheme.onPrimary
            )
        }
    }
}
