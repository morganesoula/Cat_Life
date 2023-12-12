package com.msoula.catlife.feature_inventory.presentation.screen

import CatLifeTheme
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import com.msoula.catlife.CatLifeAndroidTest
import com.msoula.catlife.MainActivity
import com.msoula.catlife.core.preview.previewInventoryFeedStateWithData
import com.msoula.catlife.feature_inventory.data.state.InventoryState
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class InventoryScreenTest : CatLifeAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testInventoryScreenUi() {
        composeRule.activity.setContent {
            CatLifeTheme {
                InventoryScreen(
                    state = previewInventoryFeedStateWithData(),
                    inventoryUiState = InventoryState(),
                    onUiActionEvent = {},
                    onUpdateItemQuantity = { _, _ -> },
                    navController = NavController(context),
                    navigator = EmptyDestinationsNavigator
                )
            }
        }

        composeRule.onNodeWithText("Jouet").assertIsDisplayed()
    }
}