package com.msoula.catlife.feature_calendar.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.msoula.catlife.R
import com.msoula.catlife.core.presentation.CustomAddFormButton
import com.msoula.catlife.core.presentation.CustomDatePickerDialog
import com.msoula.catlife.core.presentation.CustomErrorTextField
import com.msoula.catlife.core.presentation.CustomFormForHeightSpacer
import com.msoula.catlife.core.presentation.CustomFormForWidthSpacer
import com.msoula.catlife.core.presentation.CustomTextField
import com.msoula.catlife.core.presentation.CustomTimePickerDialog
import com.msoula.catlife.core.presentation.OnLifecycleEvent
import com.msoula.catlife.core.presentation.navigation.AddEditEventFormScreenNavArgs
import com.msoula.catlife.core.util.LocalDim
import com.msoula.catlife.extension.convertToKotlinLocalDate
import com.msoula.catlife.extension.toDateString
import com.msoula.catlife.feature_calendar.data.state.AddEditEventFormEvent
import com.msoula.catlife.feature_calendar.data.state.CalendarEventFormState
import com.msoula.catlife.globalCurrentDay
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.datetime.toJavaLocalDate
import java.time.LocalTime

@Destination(
    navArgsDelegate = AddEditEventFormScreenNavArgs::class
)
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddEditEventFormScreen(
    modifier: Modifier = Modifier,
    state: CalendarEventFormState,
    onUiEvent: (event: AddEditEventFormEvent, onEventAddedOrUpdated: (id: Int?) -> Unit) -> Unit,
    onLifecycleEvent: (OnLifecycleEvent) -> Unit,
    onEventAddedOrUpdated: (id: Int?) -> Unit,
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current

    val startDatePickerState = rememberDatePickerState(initialSelectedDateMillis = globalCurrentDay)
    val endDatePickerState = rememberDatePickerState(initialSelectedDateMillis = globalCurrentDay)

    val startTimePickerState = rememberTimePickerState(is24Hour = true)
    val endTimePickerState = rememberTimePickerState(is24Hour = true)

    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = context.getString(R.string.network_needed_places),
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = modifier.fillMaxWidth()
            )

            CustomFormForHeightSpacer(LocalDim.current.itemSpacing16)

            // section title
            CustomTextField(
                value = state.currentEventTitle,
                onValueChange = {
                    onUiEvent(
                        AddEditEventFormEvent.OnEventTitleChanged(
                            it
                        ),
                        onEventAddedOrUpdated
                    )
                },
                labelText = R.string.current_event_title_text,
                isError = state.eventTitleError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                singleLine = true
            )

            if (state.eventTitleError != null) {
                CustomErrorTextField(
                    value = context.getString(state.eventTitleError),
                    modifier = Modifier.align(Alignment.End)
                )
            }

            CustomFormForHeightSpacer(LocalDim.current.itemSpacing16)

            // section place
            AutoCompleteTextView(
                modifier = modifier,
                query = state.currentEventPlace,
                queryLabel = context.getString(R.string.current_event_place_text),
                predictions = state.listOfPredictions ?: emptyList(),
                onQueryChanged = {
                    onUiEvent(
                        AddEditEventFormEvent.OnEventPlaceChanged(it),
                        onEventAddedOrUpdated
                    )

                    if (state.currentEventPlaceLat != 0.0) {
                        onUiEvent(
                            AddEditEventFormEvent.ClearMapData,
                            onEventAddedOrUpdated
                        )
                    }
                },
                onClearClick = {
                    onUiEvent(
                        AddEditEventFormEvent.OnEventPlaceChanged(""),
                        onEventAddedOrUpdated
                    )
                },
                onItemClick = {
                    onUiEvent(
                        AddEditEventFormEvent.OnEventPlaceSelected(
                            it.address,
                            it.geometry.location.latitude,
                            it.geometry.location.longitude
                        ),
                        onEventAddedOrUpdated
                    )
                },
                onClearPredictionsList = {
                    onUiEvent(AddEditEventFormEvent.ClearPredictions, onEventAddedOrUpdated)
                },
                onFocusChanged = null
            )


            if (state.eventPlaceError != null) {
                CustomErrorTextField(
                    value = context.getString(state.eventPlaceError),
                    modifier = Modifier.align(Alignment.End)
                )
            }

            CustomFormForHeightSpacer(LocalDim.current.itemSpacing16)

            // section date
            // section all day
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colorScheme.background)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text(
                        text = context.getString(R.string.current_event_all_day_text),
                        modifier = Modifier
                            .weight(3f)
                            .padding(start = LocalDim.current.itemSpacing8),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface.copy(alpha = 0.8f)
                    )

                    Switch(
                        checked = state.currentEventAllDayChecked,
                        onCheckedChange = {
                            onUiEvent(
                                AddEditEventFormEvent.OnEventAllDayChecked(it),
                                onEventAddedOrUpdated
                            )
                        },
                        modifier = Modifier
                            .wrapContentWidth()
                            .semantics { testTagsAsResourceId = true }
                            .testTag(context.getString(R.string.place_switch_test_tag)),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = colorScheme.secondary,
                            checkedBorderColor = colorScheme.secondary,
                            checkedTrackColor = colorScheme.background,
                            uncheckedThumbColor = colorScheme.onSurface.copy(alpha = 0.8f),
                            uncheckedBorderColor = colorScheme.onSurface.copy(alpha = 0.8f),
                            uncheckedTrackColor = Color.LightGray

                        )
                    )
                }
            }

            if (state.eventAllDayCheckedError != null) {
                CustomErrorTextField(
                    context.getString(state.eventAllDayCheckedError),
                    modifier.align(Alignment.End)
                )
            }

            CustomFormForHeightSpacer(LocalDim.current.itemSpacing8)

            // section start
            if (!state.currentEventAllDayChecked) {
                Text(
                    text = context.getString(R.string.start),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomDatePickerDialog(
                    modifier = modifier,
                    datePickerState = startDatePickerState,
                    text = state.currentEventStartDate.toDateString(context, true)
                ) {
                    onUiEvent(
                        AddEditEventFormEvent.OnEventStartDateChanged(
                            startDatePickerState.selectedDateMillis!!.convertToKotlinLocalDate()
                                .toJavaLocalDate()
                        ),
                        onEventAddedOrUpdated
                    )
                }

                if (!state.currentEventAllDayChecked) {
                    CustomFormForWidthSpacer(8.dp)

                    CustomTimePickerDialog(
                        modifier = modifier,
                        timePickerState = startTimePickerState,
                        text = state.currentEventStartTime
                    ) { hour, minute ->
                        onUiEvent(
                            AddEditEventFormEvent.OnEventStartTimeChanged(
                                LocalTime.of(
                                    hour,
                                    minute
                                )
                            ),
                            onEventAddedOrUpdated
                        )
                    }
                }
            }

            if (state.eventStartDateError != null) {
                CustomErrorTextField(
                    context.getString(state.eventStartDateError),
                    modifier.align(Alignment.End)
                )
            }

            CustomFormForHeightSpacer(LocalDim.current.itemSpacing16)

            // section end
            if (!state.currentEventAllDayChecked) {
                Text(
                    text = context.getString(R.string.end),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 10.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CustomDatePickerDialog(
                        modifier = modifier,
                        datePickerState = endDatePickerState,
                        text = if (state.currentEventEndDate != 0L) state.currentEventEndDate.toDateString(
                            context,
                            true
                        ) else context.getString(R.string.to_define)
                    ) {
                        onUiEvent(
                            AddEditEventFormEvent.OnEventEndDateChanged(
                                endDatePickerState.selectedDateMillis!!.convertToKotlinLocalDate()
                                    .toJavaLocalDate()
                            ),
                            onEventAddedOrUpdated
                        )
                    }

                    CustomFormForWidthSpacer(8.dp)

                    CustomTimePickerDialog(
                        modifier = modifier,
                        timePickerState = endTimePickerState,
                        text = state.currentEventEndTime
                    ) { hour, minute ->
                        onUiEvent(
                            AddEditEventFormEvent.OnEventEndTimeChanged(
                                LocalTime.of(
                                    hour,
                                    minute
                                )
                            ),
                            onEventAddedOrUpdated
                        )
                    }
                }
            }

            if (state.eventEndDateError != null) {
                CustomErrorTextField(
                    context.getString(state.eventEndDateError),
                    Modifier.align(Alignment.End)
                )
            }

            if (state.eventStartAndEndTimeError != null) {
                CustomErrorTextField(
                    context.getString(state.eventStartAndEndTimeError),
                    Modifier.align(Alignment.End)
                )
            }

            CustomFormForHeightSpacer(LocalDim.current.itemSpacing16)

            // section description
            CustomTextField(
                value = state.currentEventDescription ?: "",
                onValueChange = {
                    onUiEvent(
                        AddEditEventFormEvent.OnEventDescriptionChanged(
                            it
                        ),
                        onEventAddedOrUpdated
                    )
                },
                labelText = R.string.current_event_description_text,
                isError = state.eventDescriptionError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                maxLines = 15,
                minLines = 3
            )

            if (state.eventDescriptionError != null) {
                CustomErrorTextField(
                    context.getString(state.eventDescriptionError), Modifier.align(Alignment.End)
                )
            }

            CustomFormForHeightSpacer(LocalDim.current.itemSpacing32)

            CustomAddFormButton(
                modifier,
                { onUiEvent(AddEditEventFormEvent.Submit, onEventAddedOrUpdated) },
                state.enableSubmit,
                state.submitEventText
            )
        }
    }

    BackHandler {
        onLifecycleEvent(OnLifecycleEvent.OnBackPressed)
        navigator.popBackStack()
    }
}
