package com.msoula.catlife.feature_calendar.presentation.screen

import CatLifeTheme
import androidx.activity.compose.setContent
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import com.msoula.catlife.CatLifeAndroidTest
import com.msoula.catlife.MainActivity
import com.msoula.catlife.R
import com.msoula.catlife.core.preview.PreviewAddEditEventFormScreen
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
@HiltAndroidTest
class AddEditEventFormScreenTest : CatLifeAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()
    @Test
    fun testAddEditEventFormUi() {
        composeRule.activity.setContent {
            CatLifeTheme {
                PreviewAddEditEventFormScreen()
            }
        }

        composeRule.onNodeWithText("Title").performTextInput("test event 1")
        composeRule.onNodeWithText("Add").assertIsNotEnabled()

        composeRule.onNodeWithText("Place").performClick()
        composeRule.waitUntilAtLeastOneExists(hasContentDescription("autocomplete"))
        composeRule.onNodeWithContentDescription("autocomplete").performClick()
        composeRule.onNodeWithText("Add").assertIsNotEnabled()

        composeRule
            .onNodeWithTag(context.getString(R.string.place_switch_test_tag))
            .assertIsDisplayed()
            .performTouchInput { swipeRight() }
        
        composeRule.onNodeWithText("Add").assertIsEnabled()

        composeRule.onNodeWithText("test event 1").performTextClearance()
        composeRule.onNodeWithText("Add").assertIsNotEnabled()
    }
}