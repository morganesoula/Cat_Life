package com.msoula.catlife.feature_inventory.presentation.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RemoveShoppingCart
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.msoula.catlife.R
import com.msoula.catlife.core.presentation.CommonListWithLazyColumn
import com.msoula.catlife.core.presentation.CustomColumnNoData
import com.msoula.catlife.core.presentation.CustomElevatedCard
import com.msoula.catlife.core.presentation.CustomFAButton
import com.msoula.catlife.core.presentation.CustomFormForHeightSpacer
import com.msoula.catlife.core.presentation.LinearLoadingScreen
import com.msoula.catlife.core.presentation.navigation.BottomBar
import com.msoula.catlife.core.util.LocalDim
import com.msoula.catlife.destinations.AddEditInventoryItemFormScreenDestination
import com.msoula.catlife.feature_cat_detail.presentation.screen.DeleteDataAlertDialog
import com.msoula.catlife.feature_inventory.data.state.InventoryItemsFeedUiState
import com.msoula.catlife.feature_inventory.data.state.InventoryState
import com.msoula.catlife.feature_inventory.data.state.UiActionEvent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun InventoryScreen(
    modifier: Modifier = Modifier,
    state: InventoryItemsFeedUiState,
    inventoryUiState: InventoryState,
    onUiActionEvent: (UiActionEvent) -> Unit,
    onUpdateItemQuantity: (itemId: Int, increment: Boolean) -> Unit,
    navController: NavController,
    navigator: DestinationsNavigator
) {
    if (inventoryUiState.openDeleteAlert) {
        DeleteDataAlertDialog(
            dismissDialog = {
                onUiActionEvent(UiActionEvent.OnDismissRequest)
            },
            deleteElement = {
                onUiActionEvent(
                    UiActionEvent.OnDeleteUi(
                        deleteData = true,
                        elementId = inventoryUiState.itemId
                    )
                )
            }
        )
    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            CustomFAButton(
                openForm = { navigator.navigate(AddEditInventoryItemFormScreenDestination) },
                contentDescription = R.string.add_new_inventory_item_title
            )
        },
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { paddingValue ->
        when (state) {
            is InventoryItemsFeedUiState.Loading -> LinearLoadingScreen()
            is InventoryItemsFeedUiState.Empty -> NoItems()
            is InventoryItemsFeedUiState.Success ->
                CommonListWithLazyColumn(
                    modifier = modifier,
                    paddingValues = paddingValue,
                    items = state.inventoryItemsFeed,
                    swipeToDelete = { item ->
                        onUiActionEvent(UiActionEvent.OpenDeleteAlertDialog(item.id.toInt()))
                    },
                    idKey = { inventoryItem -> inventoryItem.id.toInt() }
                ) {
                    CustomElevatedCard {
                        InventoryItemComposable(
                            modifier = modifier,
                            inventoryItemId = it.id.toInt(),
                            label = it.label,
                            quantity = it.quantity.toString(),
                            incrementQuantity = { itemId: Int ->
                                onUpdateItemQuantity(itemId, true)
                            },
                            decrementQuantity = { itemId: Int ->
                                onUpdateItemQuantity(itemId, false)
                            }
                        )
                    }

                    CustomFormForHeightSpacer(LocalDim.current.itemSpacing16)
                }
        }
    }
}

@Composable
fun NoItems() {
    CustomColumnNoData(
        noDataText = R.string.no_inventory_items_title,
        icon = Icons.Outlined.RemoveShoppingCart
    )
}
