package com.msoula.catlife.core.presentation

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.msoula.catlife.R
import com.msoula.catlife.core.util.Constant
import com.msoula.catlife.core.util.LocalDim
import com.msoula.catlife.core.util.LocalTextSize
import com.msoula.catlife.extension.toDateString
import com.msoula.catlife.feature_add_edit_cat.presentation.AddEditCatDateEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatLifeDetailScreenTopBar(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit
) {
    val context = LocalContext.current

    TopAppBar(
        modifier = modifier,
        title = { },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        navigationIcon = {
            IconButton(onClick = { navigateUp() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = context.getString(R.string.back)
                )
            }
        }
    )
}

@Composable
fun CustomFAButton(
    modifier: Modifier = Modifier,
    openForm: () -> Unit,
    @StringRes contentDescription: Int
) {
    val context = LocalContext.current

    FloatingActionButton(
        modifier = modifier.then(
            Modifier
                .size(60.dp)
                .offset(y = 45.dp)
        ),
        shape = RoundedCornerShape(50.dp),
        onClick = { openForm() },
        containerColor = colorScheme.secondary,
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = context.getString(contentDescription),
            tint = colorScheme.onSecondary
        )
    }
}

@Composable
fun ColumnScope.CustomAddFormButton(
    modifier: Modifier = Modifier,
    submit: () -> Unit,
    enableSubmit: Boolean,
    @StringRes buttonText: Int
) {
    val context = LocalContext.current

    Button(
        modifier = modifier
            .align(Alignment.End)
            .testTag(Constant.SUBMIT_CAT_BUTTON),
        onClick = {
            Log.i("XXX", "Pressing button")
            submit()
        },
        enabled = enableSubmit,
        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.secondary)
    ) {
        Text(
            text = context.getString(buttonText),
            style = MaterialTheme.typography.headlineMedium,
            modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
}

@Composable
fun RowScope.CustomAnyDetailElevatedCard(
    modifier: Modifier = Modifier,
    data: String,
    label: String
) {
    Card(
        modifier = modifier
            .weight(1f)
            .fillMaxWidth(),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(1.dp, colorScheme.onSurface.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = data,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CatLifeHorizontalPager(
    modifier: Modifier = Modifier,
    wrapContentHeight: Boolean = false,
    navigateToDetail: (Int) -> Unit,
    id: Int,
    pagerState: PagerState,
    content: @Composable () -> Unit,
) {
    Column {
        HorizontalPager(state = pagerState) { _ ->
            ElevatedCard(
                modifier = if (wrapContentHeight) {
                    modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(start = 8.dp, end = 8.dp)
                } else {
                    modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(start = 8.dp, end = 8.dp)
                },
                onClick = { navigateToDetail(id) }
            ) {
                content()
            }
        }

        CustomFormForHeightSpacer(4.dp)

        if (pagerState.pageCount > 1) {
            Row(
                modifier = modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(
                        start = LocalDim.current.itemSpacing8,
                        end = LocalDim.current.itemSpacing8
                    ),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                    Box(
                        modifier = modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CustomDividerHomeScreen(modifier: Modifier = Modifier) {
    CustomFormForHeightSpacer(8.dp)
    Divider(
        thickness = 1.dp,
        color = colorScheme.secondary.copy(alpha = 0.4f),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
    )
    CustomFormForHeightSpacer(8.dp)
}

@OptIn(ExperimentalComposeUiApi::class)
@Suppress("FunctionName")
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes labelText: Int,
    isError: Int? = null,
    keyboardOptions: KeyboardOptions,
    singleLine: Boolean? = null,
    testTag: String = "",
    maxLines: Int? = null,
    minLines: Int? = null
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        onValueChange = { onValueChange.invoke(it) },
        label = {
            Text(
                context.getString(labelText),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        },
        isError = isError != null,
        textStyle = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { testTagsAsResourceId = true }
            .testTag(testTag),
        keyboardOptions = keyboardOptions,
        singleLine = singleLine ?: false,
        maxLines = maxLines ?: 1,
        keyboardActions = KeyboardActions(onDone = {
            focusManager.moveFocus(FocusDirection.Down)
        }),
        minLines = minLines ?: 1,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = colorScheme.background,
            focusedContainerColor = Color.Transparent,
            focusedTextColor = colorScheme.onSurface,
            focusedIndicatorColor = colorScheme.secondary,
            focusedLabelColor = colorScheme.onSurface
        )
    )
}

@Suppress("FunctionName")
@Composable
fun CustomErrorTextField(
    value: String,
    modifier: Modifier
) {
    Text(
        text = value,
        color = colorScheme.error,
        style = MaterialTheme.typography.bodyMedium,
        fontStyle = FontStyle.Italic,
        modifier = modifier,
        textAlign = TextAlign.End
    )
}

@Suppress("FunctionName")
@Composable
fun CustomFormForHeightSpacer(height: Dp) {
    Spacer(modifier = Modifier.height(height))
}

@Suppress("FunctionName")
@Composable
fun CustomFormForWidthSpacer(width: Dp) {
    Spacer(modifier = Modifier.width(width))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCatFormDateColumn(
    modifier: Modifier,
    labelText: String,
    datePickerState: DatePickerState,
    date: Long,
    error: Int?,
    onDateEvent: (AddEditCatDateEvent, Long) -> Unit,
    type: AddEditCatDateEvent,
    onDeleteDateForEvent: (AddEditCatDateEvent) -> Unit,
    optional: Boolean = true
) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }

    Column {
        Text(
            text = labelText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Start,
            modifier = modifier.padding(start = 3.dp, bottom = 3.dp)
        )

        Row {
            Button(
                onClick = { showDatePicker = true },
                modifier = modifier
                    .padding(end = 5.dp)
                    .fillMaxWidth()
                    .weight(4f),
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.secondary)
            ) {
                Text(
                    text = if (date != 0L) date.toDateString() else if (optional) context.getString(
                        R.string.optional
                    ) else context.getString(R.string.to_define),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                )
            }

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorScheme.secondary,
                                contentColor = colorScheme.onSecondary
                            ),
                            onClick = {
                                showDatePicker = false
                                onDateEvent(type, datePickerState.selectedDateMillis!!)
                            }) {
                            Text(text = context.getString(R.string.confirm))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showDatePicker = false
                        }) {
                            Text(text = context.getString(R.string.cancel))
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState,
                        colors = DatePickerDefaults.colors(
                            headlineContentColor = colorScheme.secondary,
                            selectedDayContainerColor = colorScheme.secondary
                        ),
                        showModeToggle = false
                    )
                }
            }

            if (optional) {
                if (date != 0L) {
                    Button(
                        onClick = { onDeleteDateForEvent(type) },
                        modifier = modifier
                            .fillMaxWidth()
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.secondary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = context.getString(
                                R.string.clear_date_content
                            ),
                            tint = colorScheme.onPrimary,
                            modifier = modifier.padding(LocalDim.current.lazyRowPadding5)
                        )
                    }
                }
            }

        }
    }

    if (error != null) {
        CustomErrorTextField(
            value = context.getString(error),
            modifier = modifier.fillMaxWidth()
        )
    }

    CustomFormForHeightSpacer(LocalDim.current.itemSpacing16)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.CustomTimePickerDialog(
    modifier: Modifier,
    timePickerState: TimePickerState,
    text: String,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    val context = LocalContext.current
    var showTimePicker by remember {
        mutableStateOf(false)
    }

    Button(
        onClick = { showTimePicker = true },
        modifier = modifier
            .fillMaxWidth()
            .weight(1f),
        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.secondary)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
        )
    }

    if (showTimePicker) {
        AlertDialog(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = colorScheme.surface,
                    shape = RoundedCornerShape(size = 12.dp)
                ),
            onDismissRequest = { showTimePicker = false })
        {
            Column(
                modifier = modifier
                    .background(
                        color = Color.LightGray.copy(alpha = 0.3f)
                    )
                    .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        selectorColor = colorScheme.secondary,
                        timeSelectorSelectedContainerColor = colorScheme.surface,
                        timeSelectorSelectedContentColor = colorScheme.secondary
                    )
                )
                Row(
                    modifier = modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { showTimePicker = false }
                    ) {
                        Text(text = context.getString(R.string.cancel))
                    }

                    CustomFormForWidthSpacer(width = LocalDim.current.itemSpacing8)

                    TextButton(
                        onClick = {
                            showTimePicker = false
                            onConfirm(timePickerState.hour, timePickerState.minute)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.secondary,
                            contentColor = colorScheme.onSecondary
                        )
                    ) {
                        Text(text = context.getString(R.string.confirm))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.CustomDatePickerDialog(
    modifier: Modifier = Modifier,
    datePickerState: DatePickerState,
    text: String,
    onConfirm: () -> Unit
) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }

    Button(
        onClick = { showDatePicker = true },
        modifier = modifier
            .fillMaxWidth()
            .weight(1f),
        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.secondary)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.secondary,
                        contentColor = colorScheme.onSecondary
                    ),
                    onClick = {
                        showDatePicker = false
                        onConfirm()
                    }) {
                    Text(text = context.getString(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) {
                    Text(text = context.getString(R.string.cancel))
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    headlineContentColor = colorScheme.secondary,
                    selectedDayContainerColor = colorScheme.secondary
                ),
                showModeToggle = false
            )
        }
    }
}

@Suppress("FunctionName")
@Composable
fun CustomColumnNoData(
    modifier: Modifier = Modifier,
    @StringRes noDataText: Int,
    icon: ImageVector,
    testTag: String = "",
    isHomeScreen: Boolean = false
) {
    val context = LocalContext.current

    Column(
        modifier = if (isHomeScreen) modifier
            .fillMaxWidth()
            .wrapContentHeight() else modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isHomeScreen) {
            CustomFormForHeightSpacer(16.dp)
        }

        Icon(
            modifier = modifier.size(if (isHomeScreen) 30.dp else 50.dp),
            imageVector = icon,
            contentDescription = null,
            tint = colorScheme.onSurface
        )

        CustomFormForHeightSpacer(4.dp)

        Text(
            modifier = modifier.testTag(testTag),
            text = context.getString(noDataText),
            fontSize = LocalTextSize.current.textSize18,
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Suppress("FunctionName")
@Composable
fun LinearLoadingScreen() {
    Row(
        horizontalArrangement = Arrangement.Center, modifier = Modifier
            .fillMaxWidth()
            .padding(
                LocalDim.current.itemSpacing32
            )
    ) {
        LinearProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> CommonListWithLazyColumn(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    items: List<T>,
    swipeToDelete: (item: T) -> Unit,
    idKey: (T) -> Int,
    content: @Composable (T) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = LocalDim.current.itemSpacing8,
                start = LocalDim.current.itemSpacing16,
                end = LocalDim.current.itemSpacing16
            )
            .padding(paddingValues)
    ) {
        items(
            items = items,
            key = idKey
        ) { entity ->
            val currentItem by rememberUpdatedState(newValue = entity)

            val dismissState = rememberDismissState(
                confirmValueChange = {
                    if (it == DismissValue.DismissedToStart) {
                        swipeToDelete(currentItem)
                        false
                    } else false
                },
                positionalThreshold = { 100.dp.toPx() }
            )

            CommonSwipeToDismiss(dismissState = dismissState, modifier = Modifier) {
                content(entity)
            }

            CustomFormForHeightSpacer(LocalDim.current.itemSpacing16)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
@Composable
private fun LazyItemScope.CommonSwipeToDismiss(
    dismissState: DismissState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    SwipeToDismiss(
        state = dismissState,
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = Dp(1f))
            .animateItemPlacement()
            .clip(RoundedCornerShape(5.dp)),
        directions = setOf(DismissDirection.EndToStart),
        background = {
            DismissBackground(dismissState = dismissState)
        },
        dismissContent = {
            content()
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DismissBackground(
    modifier: Modifier = Modifier,
    dismissState: DismissState
) {
    val direction = dismissState.dismissDirection

    val color = when (direction) {
        DismissDirection.EndToStart -> Color.Red
        else -> Color.Transparent
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (direction == DismissDirection.EndToStart) {
            Spacer(
                modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color.Red)
            )
            Icon(
                Icons.Outlined.Delete,
                modifier = modifier.padding(end = 5.dp),
                contentDescription = "delete",
                tint = colorScheme.surface
            )
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomElevatedCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .padding(bottom = 6.dp, end = 6.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(5.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        shape = RoundedCornerShape(5.dp),
        onClick = { onClick() }
    ) {
        content()
    }
}