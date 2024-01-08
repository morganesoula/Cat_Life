package com.msoula.catlife.feature_calendar.presentation.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.msoula.catlife.extension.setToBold
import com.msoula.catlife.feature_calendar.custom_places.data.CustomPlace
import notEmpty

@Composable
fun <T> AutoCompleteTextView(
    modifier: Modifier,
    query: String,
    queryLabel: String,
    onClearPredictionsList: () -> Unit = {},
    onQueryChanged: (String) -> Unit = {},
    predictions: List<T>,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onItemClick: (T) -> Unit = {},
    onFocusChanged: ((focusState: FocusState) -> Unit)?
) {
    val view = LocalView.current
    val lazyListState = rememberLazyListState()

    Column(
        modifier = modifier
            .heightIn(max = TextFieldDefaults.MinHeight * 6)
    ) {
        QuerySearch(
            query = query,
            label = queryLabel,
            onQueryChanged = onQueryChanged,
            onDoneActionClick = {
                view.clearFocus()
                onDoneActionClick.invoke()
            },
            onClearClick = {
                onClearClick.invoke()
            },
            onClearPredictionsList = onClearPredictionsList,
            onFocusChanged = onFocusChanged
        )

        Box(
            modifier = modifier
                .fillMaxWidth()
                .border(1.dp, colorScheme.surface)
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = modifier
                    .heightIn(max = TextFieldDefaults.MinHeight * 6)
            ) {
                if (predictions.notEmpty()) {
                    items(predictions) { prediction ->
                        Row(
                            modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .clickable {
                                    view.clearFocus()
                                    onItemClick(prediction)
                                }
                                .semantics {
                                    contentDescription = "autocomplete"
                                }
                        ) {
                            val text = when (prediction) {
                                is CustomPlace -> setToBold(prediction.address, query)
                                else -> setToBold(prediction.toString(), query)
                            }

                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuerySearch(
    modifier: Modifier = Modifier,
    query: String,
    label: String,
    onClearPredictionsList: () -> Unit,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onQueryChanged: (String) -> Unit,
    onFocusChanged: ((focusState: FocusState) -> Unit)?
) {
    var showClearButton by remember { mutableStateOf(false) }

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                showClearButton = (focusState.isFocused)
                onClearPredictionsList.invoke()
                if (focusState.isFocused) {
                    onFocusChanged?.let { it(focusState) }
                }
            },
        value = query,
        onValueChange = onQueryChanged,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        trailingIcon = {
            if (showClearButton) {
                IconButton(onClick = { onClearClick.invoke() }) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                }
            }
        },
        keyboardActions = KeyboardActions(onDone = {
            onClearPredictionsList.invoke()
            onDoneActionClick.invoke()
        }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Sentences
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorScheme.background,
            unfocusedContainerColor = colorScheme.background,
            focusedIndicatorColor = colorScheme.secondary,
            focusedLabelColor = colorScheme.onSurface,
        )
    )
}