package com.msoula.catlife.feature_event_detail.presentation.screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.msoula.catlife.R
import com.msoula.catlife.core.presentation.CatLifeDetailScreenTopBar
import com.msoula.catlife.core.presentation.CustomAnyDetailElevatedCard
import com.msoula.catlife.core.presentation.CustomFormForHeightSpacer
import com.msoula.catlife.core.presentation.CustomFormForWidthSpacer
import com.msoula.catlife.core.presentation.navigation.EventDetailScreenNavArgs
import com.msoula.catlife.core.util.LocalDim
import com.msoula.catlife.core.util.LocalTextSize
import com.msoula.catlife.destinations.CalendarScreenDestination
import com.msoula.catlife.destinations.HomeScreenDestination
import com.msoula.catlife.extension.accordingToLocale
import com.msoula.catlife.feature_cat_detail.presentation.screen.DeleteDataAlertDialog
import com.msoula.catlife.feature_event_detail.data.state.CustomEventDetailState
import com.msoula.catlife.feature_inventory.data.state.UiActionEvent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(
    navArgsDelegate = EventDetailScreenNavArgs::class
)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CustomEventDetailScreen(
    modifier: Modifier = Modifier,
    state: CustomEventDetailState,
    onUiAction: (UiActionEvent, () -> Unit) -> Unit,
    navigateToEditEventForm: (idOfEvent: Int) -> Unit,
    navigator: DestinationsNavigator,
    goBackToListScreen: () -> Unit
) {
    val context = LocalContext.current
    val parisCoordinates = LatLng(48.8588897, 2.320041)

    val cameraPosition: CameraPosition = if (state.eventPlaceLat != 0.0) {
        CameraPosition.fromLatLngZoom(
            LatLng(
                state.eventPlaceLat,
                state.eventPlaceLng
            ), 15f
        )
    } else {
        CameraPosition.fromLatLngZoom(parisCoordinates, 5f)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = cameraPosition
    }

    if (state.openDeleteAlert) {
        DeleteDataAlertDialog(
            dismissDialog = {
                onUiAction(UiActionEvent.OnDismissRequest) {}
            },
            deleteElement = {
                onUiAction(
                    UiActionEvent.OnDeleteUi(
                        deleteData = true,
                        elementId = state.eventId
                    ),
                    goBackToListScreen
                )
            }
        )
    }

    LaunchedEffect(state.eventPlaceLat) {
        cameraPositionState.move(
            CameraUpdateFactory.newCameraPosition(
                if (coordinatesAreNotUnknown(state)) {
                    CameraPosition.fromLatLngZoom(
                        LatLng(
                            state.eventPlaceLat,
                            state.eventPlaceLng
                        ), 15f
                    )
                } else {
                    CameraPosition.fromLatLngZoom(parisCoordinates, 1f)
                }
            )
        )
    }

    val time: String =
        if (state.eventAllDay) {
            context.getString(R.string.current_event_all_day_text)
        } else {
            state.eventTimeStart + " - " + state.eventTimeEnd
        }

    Scaffold(
        modifier = modifier,
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .height(maxHeight.div(2.3f))
                    .padding(bottom = 1.dp),
                contentAlignment = Alignment.TopStart,
            ) {
                Card(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(top = 40.dp, start = 20.dp, end = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // section title and CRUD buttons
                        Row(
                            modifier = modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = state.eventTitle,
                                modifier = modifier.weight(3f),
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Start
                            )

                            IconButton(
                                onClick = { navigateToEditEventForm.invoke(state.eventId) },
                                modifier = modifier
                                    .wrapContentWidth()
                                    .size(30.dp)
                                    .weight(0.5f),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = colorScheme.secondary
                                )
                            ) {
                                Icon(
                                    Icons.Outlined.Edit,
                                    contentDescription = context.getString(R.string.update_event_title),
                                    modifier = modifier.padding(5.dp),
                                    tint = colorScheme.onSecondary
                                )
                            }

                            IconButton(
                                onClick = {
                                    onUiAction(
                                        UiActionEvent.OpenDeleteAlertDialog(state.eventId),
                                        goBackToListScreen
                                    )
                                },
                                modifier = modifier
                                    .wrapContentWidth()
                                    .size(30.dp)
                                    .weight(0.5f),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = colorScheme.secondary
                                )
                            ) {
                                Icon(
                                    Icons.Outlined.Delete,
                                    contentDescription = context.getString(R.string.delete_confirmation),
                                    modifier = modifier.padding(5.dp),
                                    tint = colorScheme.onSecondary
                                )
                            }
                        }

                        CustomFormForHeightSpacer(height = LocalDim.current.itemSpacing24)

                        Row(modifier = modifier.fillMaxWidth()) {
                            val annotatedText = if (state.eventPlace.contains(",")) {
                                buildAnnotatedString {
                                    append(state.eventPlace.replaceFirst(",", "\n"))
                                }
                            } else state.eventPlace

                            CustomAnyDetailElevatedCard(
                                data = annotatedText.toString(),
                                label = context.getString(R.string.current_event_place_text)
                            )
                        }

                        CustomFormForHeightSpacer(height = LocalDim.current.itemSpacing8)

                        Row(modifier = modifier.fillMaxWidth()) {
                            val annotatedTime = if (time.contains("-")) {
                                buildAnnotatedString {
                                    append(time.replaceFirst("-", "\n-\n"))
                                }
                            } else time

                            val date = dateToDisplay(
                                state.eventAllDay,
                                context,
                                state.eventDateStart,
                                state.eventDateEnd
                            )

                            val annotatedDate = if (date.contains("-")) {
                                buildAnnotatedString {
                                    append(date.replaceFirst("-", "\n-\n"))
                                }
                            } else date

                            CustomAnyDetailElevatedCard(
                                data = annotatedDate.toString(),
                                label = context.getString(R.string.date_title)
                            )

                            CustomFormForWidthSpacer(width = LocalDim.current.itemSpacing8)

                            CustomAnyDetailElevatedCard(
                                data = annotatedTime.toString(),
                                label = context.getString(R.string.schedule_title)
                            )
                        }

                        CustomFormForHeightSpacer(height = LocalDim.current.itemSpacing16)

                        if (state.eventDescription.isNotBlank()) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Blue.copy(
                                        alpha = 0.1f
                                    )
                                )
                            ) {
                                Column(modifier = modifier.padding(LocalDim.current.itemSpacing8)) {
                                    Row(
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                    ) {
                                        Icon(
                                            Icons.Outlined.Lightbulb,
                                            contentDescription = context.getString(R.string.information_title),
                                            modifier = modifier
                                                .wrapContentWidth()
                                                .padding(end = 8.dp)
                                        )

                                        Text(
                                            text = context.getString(R.string.information_title),
                                            fontSize = LocalTextSize.current.textSize16,
                                            style = MaterialTheme.typography.headlineSmall
                                        )
                                    }
                                    CustomFormForHeightSpacer(height = 4.dp)

                                    Text(
                                        text = state.eventDescription,
                                        modifier = modifier,
                                        fontSize = LocalTextSize.current.textSize16,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                            }
                        }
                    }
                }
            }

            Box(
                modifier = modifier
                    .height(maxHeight.div(1.7f))
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                GoogleMap(
                    modifier = modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(zoomControlsEnabled = false)
                ) {
                    if (coordinatesAreNotUnknown(state)) {
                        Marker(
                            state = MarkerState(
                                position = LatLng(
                                    state.eventPlaceLat,
                                    state.eventPlaceLng
                                )
                            )
                        )
                    }
                }

                if (!coordinatesAreNotUnknown(state)) {
                    Text(
                        text = context.getString(R.string.unknown_place),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = LocalTextSize.current.textSize24,
                        fontWeight = FontWeight.Bold,
                        modifier = modifier
                            .width(200.dp)
                            .height(40.dp)
                            .background(Color.Black.copy(alpha = 0.5f)),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Box(modifier = modifier.fillMaxSize()) {
                Row(modifier = modifier.fillMaxWidth()) {
                    CatLifeDetailScreenTopBar(
                        navigateUp = {
                            navigator.navigate(CalendarScreenDestination(state.eventDateStart)) {
                                popUpTo(HomeScreenDestination.route)
                            }
                        },
                        modifier = modifier
                            .wrapContentSize()
                            .weight(1f)
                    )
                    Text(
                        text = context.getString(R.string.network_needed_map),
                        modifier = modifier
                            .fillMaxWidth()
                            .weight(3f),
                        fontSize = 10.sp,
                        textAlign = TextAlign.Start,
                        color = Color.Black
                    )
                }
            }
        }
    }

    BackHandler {
        navigator.navigate(CalendarScreenDestination(state.eventDateStart)) {
            popUpTo(HomeScreenDestination.route)
        }
    }
}

private fun coordinatesAreNotUnknown(state: CustomEventDetailState): Boolean =
    !(state.eventPlaceLat == 48.8588897 || state.eventPlaceLng == 2.320041 || state.eventPlaceLat == 0.0 || state.eventPlaceLng == 0.0)

private fun dateToDisplay(
    allDay: Boolean,
    context: Context,
    startDate: Long,
    endDate: Long
): String = if (allDay || startDate == endDate) {
    startDate.accordingToLocale(context)
} else {
    "${startDate.accordingToLocale(context)} - ${endDate.accordingToLocale(context)}"
}
