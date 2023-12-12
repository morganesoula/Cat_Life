package com.msoula.catlife.feature_add_edit_cat.presentation.screen

import CatLifeTheme
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.msoula.catlife.CatLifeAndroidTest
import com.msoula.catlife.MainActivity
import com.msoula.catlife.core.preview.PreviewAddEditCatFormScreen
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AddEditCatFormScreenTest : CatLifeAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testAddEditCatFormScreenUi() {
        composeRule.activity.setContent {
            CatLifeTheme {
                PreviewAddEditCatFormScreen()
            }
        }

        composeRule.onNodeWithText("Add").assertIsNotEnabled()

        composeRule.onNodeWithText("Male").assertIsDisplayed()
        composeRule.onNodeWithText("Female").performClick()
        composeRule.onNodeWithText("Add").assertIsNotEnabled()

        composeRule.onNodeWithText("Cat name").performTextInput("Lotus")
        composeRule.onNodeWithText("Add").assertIsNotEnabled()

        composeRule.onAllNodesWithText("To define").onFirst().performClick()
        composeRule.onNodeWithText("1").performClick()
        composeRule.onNodeWithText("OK").performClick()
        composeRule.onNodeWithText("Add").assertIsNotEnabled()

        composeRule.onNodeWithText("No").performClick()
        composeRule.onNodeWithText("Race").performClick().performTextInput("Siamese")
        composeRule.onNodeWithText("Add").assertIsNotEnabled()

        composeRule.onNodeWithText("Weight (lbs)").performTextInput("3.5")
        composeRule.onNodeWithText("Add").assertIsNotEnabled()

        composeRule.onNodeWithText("Color of the fur").performTextInput("Black and white")
        composeRule.onNodeWithText("Add").assertIsEnabled()
    }
}