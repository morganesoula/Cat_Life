package com.msoula.catlife.feature_main.presentation.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.ProductionQuantityLimits
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.msoula.catlife.R
import com.msoula.catlife.core.presentation.CatLifeHorizontalPager
import com.msoula.catlife.core.presentation.CustomColumnNoData
import com.msoula.catlife.core.presentation.CustomDividerHomeScreen
import com.msoula.catlife.core.presentation.CustomFAButton
import com.msoula.catlife.core.presentation.CustomFormForHeightSpacer
import com.msoula.catlife.core.presentation.LinearLoadingScreen
import com.msoula.catlife.core.presentation.navigation.BottomBar
import com.msoula.catlife.core.util.LocalDim
import com.msoula.catlife.core.util.LocalTextSize
import com.msoula.catlife.destinations.AddEditCatFormScreenDestination
import com.msoula.catlife.destinations.CatDetailScreenDestination
import com.msoula.catlife.destinations.CustomEventDetailScreenDestination
import com.msoula.catlife.extension.toDateString
import com.msoula.catlife.feature_calendar.data.state.CalendarFeedUiState
import com.msoula.catlife.feature_inventory.data.state.InventoryItemsFeedUiState
import com.msoula.catlife.feature_main.data.state.CatsFeedUiState
import com.msoula.catlife.globalCurrentDay
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import commsoulacatlifedatabase.CatEntity
import commsoulacatlifedatabase.CustomEventEntity
import commsoulacatlifedatabase.InventoryItemEntity

@RootNavGraph(start = true)
@Destination
@Suppress("FunctionName")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    catState: CatsFeedUiState,
    inventoryState: InventoryItemsFeedUiState,
    eventsState: CalendarFeedUiState,
    navController: NavController,
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val currentDateFormatted = globalCurrentDay.toDateString(context, false)

    Scaffold(
        modifier = modifier,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            Box {
                CustomFAButton(
                    modifier = modifier
                        .semantics { contentDescription = "add new cat" },
                    openForm = {
                        navigator.navigate(AddEditCatFormScreenDestination())
                    },
                    contentDescription = R.string.add_cat_title
                )
            }
        },
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            when (catState) {
                is CatsFeedUiState.Loading -> LinearLoadingScreen()
                is CatsFeedUiState.Empty -> NoCats()
                is CatsFeedUiState.Success -> {
                    CategoryMainPageTitle(
                        titleResId = R.string.your_cats_main_page_title,
                        icon = Icons.Outlined.Pets
                    )

                    CatsHorizontalPager(
                        cats = catState.catsFeed,
                        navigator = navigator
                    )
                }
            }

            if (catState != CatsFeedUiState.Empty) CustomDividerHomeScreen(modifier)

            when (eventsState) {
                is CalendarFeedUiState.Loading -> LinearLoadingScreen()
                is CalendarFeedUiState.Empty -> if (catState == CatsFeedUiState.Empty) Unit else NoEvents()
                is CalendarFeedUiState.Success -> {
                    CategoryMainPageTitle(
                        titleResId = R.string.events_main_page_title,
                        icon = Icons.Outlined.Event
                    )

                    EventsHorizontalPager(
                        modifier = modifier,
                        events = eventsState.calendarFeed.sortedBy { it.startDate },
                        currentDateFormatted = currentDateFormatted,
                        navigator = navigator
                    )
                }
            }

            if (inventoryState is InventoryItemsFeedUiState.Success) {
                CustomDividerHomeScreen(modifier)
            }

            when (inventoryState) {
                is InventoryItemsFeedUiState.Loading -> LinearLoadingScreen()
                is InventoryItemsFeedUiState.Empty -> Unit
                is InventoryItemsFeedUiState.Success -> StorageBody(inventoryItemFeed = inventoryState.inventoryItemsFeed)
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CatsHorizontalPager(
    modifier: Modifier = Modifier,
    cats: List<CatEntity>,
    navigator: DestinationsNavigator
) {
    val pagerState = rememberPagerState(pageCount = { maxOf(cats.size, 1) })
    val currentCat = cats.getOrNull(pagerState.currentPage)

    CustomFormForHeightSpacer(height = LocalDim.current.itemSpacing8)

    CatLifeHorizontalPager(
        modifier = modifier,
        navigateToDetail = { id -> navigator.navigate(CatDetailScreenDestination(catDetailId = id)) },
        id = currentCat?.id?.toInt() ?: -1,
        pagerState = pagerState,
        content = {
            CatItemPager(
                profilePath = currentCat?.profilePicturePath,
                catName = currentCat?.name ?: "",
                catId = currentCat?.id?.toInt() ?: -1,
                openCatDetail = { navigator.navigate(CatDetailScreenDestination(catDetailId = currentCat?.id?.toInt() ?: -1)) }
            )
        }
    )

    CustomFormForHeightSpacer(height = LocalDim.current.itemSpacing8)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventsHorizontalPager(
    modifier: Modifier = Modifier,
    events: List<CustomEventEntity>,
    currentDateFormatted: String,
    navigator: DestinationsNavigator
) {
    val pagerState = rememberPagerState(pageCount = { events.size })
    val currentEvent = events.getOrNull(pagerState.currentPage)

    CustomFormForHeightSpacer(height = LocalDim.current.itemSpacing8)

    CatLifeHorizontalPager(
        modifier = modifier.wrapContentHeight(),
        wrapContentHeight = true,
        navigateToDetail = { navigator.navigate(CustomEventDetailScreenDestination(eventDetailId = currentEvent?.id?.toInt() ?: -1)) },
        id = currentEvent?.id?.toInt() ?: -1,
        pagerState = pagerState,
        content = {
            EventItemPager(
                modifier = modifier,
                title = currentEvent?.title ?: "",
                place = currentEvent?.place ?: "",
                startDate = currentEvent?.startDate ?: 0L,
                currentDateFormatted = currentDateFormatted
            )
        }
    )

    CustomFormForHeightSpacer(height = LocalDim.current.itemSpacing8)
}

@Suppress("FunctionName")
@Composable
fun NoCats(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(LocalDim.current.itemSpacing16),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomFormForHeightSpacer(LocalDim.current.itemSpacing16)

        Text(
            modifier = modifier
                .fillMaxWidth()
                .testTag("no cats test tag"),
            text = context.getString(R.string.no_cats_text),
            fontSize = LocalTextSize.current.textSize18,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
    }

    Spacer(modifier = modifier.height(LocalDim.current.itemSpacing16))
}

@Suppress("FunctionName")
@Composable
fun StorageBody(inventoryItemFeed: List<InventoryItemEntity>, modifier: Modifier = Modifier) {
    CategoryMainPageTitle(
        titleResId = R.string.your_stock_main_page_title,
        icon = Icons.Outlined.ProductionQuantityLimits
    )

    ElevatedCard(
        modifier = modifier
            .padding(LocalDim.current.itemSpacing8)
            .clip(RoundedCornerShape(5.dp))
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            var currentIndex: Int

            inventoryItemFeed.forEachIndexed { index, item ->
                currentIndex = index
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurface
                    )
                    Text(
                        text = item.quantity.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurface
                    )
                }

                if (inventoryItemFeed.size > 1) {
                    if (currentIndex < inventoryItemFeed.size - 1) {
                        CustomFormForHeightSpacer(8.dp)
                        Divider(
                            modifier = modifier.padding(start = 10.dp, end = 10.dp),
                            color = colorScheme.onBackground,
                            thickness = 1.dp
                        )
                        CustomFormForHeightSpacer(8.dp)
                    }
                }
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
fun NoEvents(modifier: Modifier = Modifier) {
    CategoryMainPageTitle(titleResId = R.string.events_main_page_title, icon = Icons.Default.Event)
    CustomColumnNoData(
        noDataText = R.string.no_events_text,
        icon = Icons.Outlined.EventBusy,
        testTag = "no events test tag",
        isHomeScreen = true
    )
    Spacer(modifier = modifier.height(LocalDim.current.itemSpacing16))
}

@Composable
fun CategoryMainPageTitle(modifier: Modifier = Modifier, titleResId: Int, icon: ImageVector) {
    val context = LocalContext.current

    Spacer(modifier = modifier.height(LocalDim.current.itemSpacing12))
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp),
        verticalAlignment = CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.wrapContentWidth(),
            tint = colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = context.getString(titleResId),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
