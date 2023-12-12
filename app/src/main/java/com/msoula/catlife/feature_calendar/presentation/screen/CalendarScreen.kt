package com.msoula.catlife.feature_calendar.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.KalendarEvents
import com.himanshoe.kalendar.KalendarType
import com.himanshoe.kalendar.color.KalendarColor
import com.himanshoe.kalendar.color.KalendarColors
import com.himanshoe.kalendar.ui.component.day.KalendarDayKonfig
import com.msoula.catlife.R
import com.msoula.catlife.core.presentation.CommonListWithLazyColumn
import com.msoula.catlife.core.presentation.CustomColumnNoData
import com.msoula.catlife.core.presentation.CustomElevatedCard
import com.msoula.catlife.core.presentation.CustomFAButton
import com.msoula.catlife.core.presentation.LinearLoadingScreen
import com.msoula.catlife.core.presentation.navigation.BottomBar
import com.msoula.catlife.core.presentation.navigation.CalendarScreenNavArgs
import com.msoula.catlife.core.util.LocalDim
import com.msoula.catlife.destinations.AddEditEventFormScreenDestination
import com.msoula.catlife.destinations.CustomEventDetailScreenDestination
import com.msoula.catlife.extension.CustomKalendarEvent
import com.msoula.catlife.extension.toCustomKalendarEvent
import com.msoula.catlife.extension.toMilliseconds
import com.msoula.catlife.feature_calendar.data.state.CalendarFeedUiState
import com.msoula.catlife.feature_calendar.data.state.CalendarUiState
import com.msoula.catlife.feature_cat_detail.presentation.screen.DeleteDataAlertDialog
import com.msoula.catlife.feature_inventory.data.state.UiActionEvent
import com.msoula.catlife.globalCurrentDay
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import commsoulacatlifedatabase.CustomEventEntity
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

@Destination(
    navArgsDelegate = CalendarScreenNavArgs::class
)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    calendarUiState: CalendarUiState,
    eventsState: CalendarFeedUiState,
    onUiEvent: (UiActionEvent) -> Unit,
    navController: NavController,
    navigator: DestinationsNavigator
) {
    if (calendarUiState.openDeleteAlert) {
        DeleteDataAlertDialog(
            dismissDialog = {
                onUiEvent(
                    UiActionEvent.OnDismissRequest
                )
            },
            deleteElement = {
                onUiEvent(
                    UiActionEvent.OnDeleteUi(
                        deleteData = true,
                        elementId = calendarUiState.itemId
                    )
                )
            }
        )
    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            CustomFAButton(
                openForm = {
                    navigator.navigate(
                        AddEditEventFormScreenDestination(
                            currentDateSelected = calendarUiState.currentDaySelected.toMilliseconds()
                        )
                    )
                },
                contentDescription = R.string.add_event_title
            )
        },
        bottomBar = {
            BottomBar(navController = navController)
        },
        modifier = modifier
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .padding(paddingValue)
        ) {
            val events: List<CustomKalendarEvent> = when (eventsState) {
                is CalendarFeedUiState.Success -> {
                    eventsState.calendarFeed.map { it.toCustomKalendarEvent() }.toList()
                }

                else -> emptyList()
            }

            Kalendar(
                currentDay = calendarUiState.currentDaySelected,
                modifier = Modifier
                    .wrapContentWidth()
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                kalendarType = KalendarType.Firey,
                onDayClick = { currentDay, _ ->
                    onUiEvent(UiActionEvent.OnCurrentDaySelected(currentDay))
                },
                kalendarDayKonfig = KalendarDayKonfig(
                    textColor = colorScheme.onSurface,
                    size = 56.dp,
                    textSize = 16.sp,
                    selectedTextColor = Color.White
                ),
                events = KalendarEvents(events),
                kalendarColors = KalendarColors(
                    color = listOf(
                        KalendarColor(
                            backgroundColor = colorScheme.background,
                            dayBackgroundColor = colorScheme.secondary,
                            headerTextColor = colorScheme.onBackground
                        )
                    )
                )
            )

            when (eventsState) {
                is CalendarFeedUiState.Loading -> LinearLoadingScreen()
                is CalendarFeedUiState.Empty -> NoEventOnDayDText()
                is CalendarFeedUiState.Success -> {
                    val eventsOfDayD = eventsState.calendarFeed.filter {
                        convertLongToLocalDate(it.startDate) == calendarUiState.currentDaySelected || convertLongToLocalDate(
                            it.endDate
                        ) == calendarUiState.currentDaySelected
                    }

                    if (eventsOfDayD.isEmpty()) {
                        NoEventOnDayDText()
                    }

                    CommonListWithLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        paddingValues = paddingValue,
                        items = eventsOfDayD,
                        swipeToDelete = { event ->
                            onUiEvent(UiActionEvent.OpenDeleteAlertDialog(event.id.toInt()))
                        },
                        idKey = { event -> event.id.toInt() }
                    ) {
                        CurrentEvent(
                            event = it,
                            currentDaySelected = calendarUiState.currentDaySelected,
                            modifier = modifier,
                            navigator = navigator
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CurrentEvent(
    modifier: Modifier = Modifier,
    event: CustomEventEntity,
    currentDaySelected: LocalDate,
    navigator: DestinationsNavigator
) {
    CustomElevatedCard(
        onClick = { navigator.navigate(CustomEventDetailScreenDestination(eventDetailId = event.id.toInt())) }
    ) {
        Column(
            modifier = modifier
                .padding(LocalDim.current.itemSpacing8)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(bottom = 3.dp),
                color = colorScheme.onSurface
            )

            Divider(
                color = colorScheme.onBackground, thickness = 1.dp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = modifier.padding(top = 3.dp, bottom = 3.dp)
            ) {
                if (event.allDay) {
                    Text(
                        text = LocalContext.current.getString(R.string.current_event_all_day_text),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = modifier.padding(top = 3.dp),
                        color = colorScheme.onSurface
                    )
                } else if (event.startDate == event.endDate) {
                    Text(
                        text = event.startTime + " - " + event.endTime,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = modifier.padding(top = 3.dp),
                        color = colorScheme.onSurface
                    )
                } else if (currentDaySelected.atStartOfDayIn(TimeZone.currentSystemDefault())
                        .toEpochMilliseconds() == event.startDate
                ) {
                    Text(
                        text = event.startTime,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = modifier.padding(top = 3.dp),
                        color = colorScheme.onSurface
                    )
                } else if (currentDaySelected.atStartOfDayIn(TimeZone.currentSystemDefault())
                        .toEpochMilliseconds() == event.endDate
                ) {
                    Text(
                        text = event.endTime,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = modifier.padding(top = 3.dp),
                        color = colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun NoEventOnDayDText() {
    CustomColumnNoData(noDataText = R.string.no_events_text, icon = Icons.Outlined.EventBusy)
}

private fun convertLongToLocalDate(value: Long): LocalDate {
    val instant = Instant.fromEpochMilliseconds(value)
    return instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
}

fun previewEventsFeedStateWithData(): CalendarFeedUiState {
    return CalendarFeedUiState.Success(
        listOf(
            CustomEventEntity(
                1,
                "event 1",
                "description event 1",
                "",
                "0.0",
                "0.0",
                globalCurrentDay,
                endDate = 0L,
                startTime = "00:00",
                endTime = "00:00",
                allDay = true
            ),
            CustomEventEntity(
                2,
                "event 2",
                "description event 2",
                "",
                "0.0",
                "0.0",
                globalCurrentDay,
                endDate = 0L,
                startTime = "00:00",
                endTime = "00:00",
                allDay = true
            ),
            CustomEventEntity(
                3,
                "event 3",
                "description event 3",
                "",
                "0.0",
                "0.0",
                1695866400000L,
                endDate = 0L,
                startTime = "00:00",
                endTime = "00:00",
                allDay = true
            )
        )
    )
}
