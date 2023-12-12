package com.msoula.catlife.feature_main.presentation.screen

import CatLifeTheme
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import com.msoula.catlife.CatLifeAndroidTest
import com.msoula.catlife.MainActivity
import com.msoula.catlife.core.preview.previewCatsFeedStateEmpty
import com.msoula.catlife.core.preview.previewCatsFeedStateWithData
import com.msoula.catlife.core.preview.previewEventFeedStateEmpty
import com.msoula.catlife.core.preview.previewInventoryFeedStateEmpty
import com.msoula.catlife.core.preview.previewInventoryFeedStateWithData
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HomeScreenTest : CatLifeAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testHomeScreenUi() {
        composeRule.activity.setContent {
            CatLifeTheme {
                HomeScreen(
                    catState = previewCatsFeedStateEmpty(),
                    inventoryState = previewInventoryFeedStateEmpty(),
                    eventsState = previewEventFeedStateEmpty(),
                    navController = NavController(context),
                    navigator = EmptyDestinationsNavigator
                )
            }
        }

        composeRule.onNodeWithTag("no cats test tag").assertIsDisplayed()
        composeRule.onNodeWithTag("no events test tag").assertIsDisplayed()

        composeRule.activity.setContent {
            CatLifeTheme {
                HomeScreen(
                    catState = previewCatsFeedStateEmpty(),
                    inventoryState = previewInventoryFeedStateWithData(),
                    eventsState = previewEventFeedStateEmpty(),
                    navController = NavController(context),
                    navigator = EmptyDestinationsNavigator
                )
            }
        }

        composeRule.onNodeWithTag("no cats test tag").assertIsDisplayed()
        composeRule.onNodeWithText("Croquettes").assertIsDisplayed()

        composeRule.activity.setContent {
            CatLifeTheme {
                HomeScreen(
                    catState = previewCatsFeedStateWithData(),
                    inventoryState = previewInventoryFeedStateWithData(),
                    eventsState = previewEventFeedStateEmpty(),
                    navController = NavController(context),
                    navigator = EmptyDestinationsNavigator
                )
            }
        }

        composeRule.onNodeWithTag("no cats test tag").assertDoesNotExist()
        composeRule.onNodeWithText("catTest1".uppercase()).assertIsDisplayed()
        composeRule.onNodeWithText("Croquettes").assertIsDisplayed()
    }
}