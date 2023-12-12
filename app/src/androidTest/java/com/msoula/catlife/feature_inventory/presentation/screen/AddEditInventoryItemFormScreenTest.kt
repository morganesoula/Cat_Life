package com.msoula.catlife.feature_inventory.presentation.screen

import CatLifeTheme
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.msoula.catlife.CatLifeAndroidTest
import com.msoula.catlife.MainActivity
import com.msoula.catlife.core.preview.PreviewAddEditInventoryItemFormScreen
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AddEditInventoryItemFormScreenTest : CatLifeAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testAddEditInventoryScreenUi() {
        composeRule.activity.setContent {
            CatLifeTheme {
                PreviewAddEditInventoryItemFormScreen()
            }
        }

        composeRule.onNodeWithText("Add").assertIsNotEnabled()
        composeRule.onNodeWithText("Label").performClick().performTextInput("Croquettes")
        composeRule.onNodeWithText("Add").assertIsNotEnabled()

        composeRule.onNodeWithText("Quantity").performClick().performTextInput("6")
        composeRule.onNodeWithText("Add").assertIsEnabled()
    }
}