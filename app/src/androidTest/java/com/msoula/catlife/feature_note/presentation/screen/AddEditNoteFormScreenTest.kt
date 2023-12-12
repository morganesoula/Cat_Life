package com.msoula.catlife.feature_note.presentation.screen

import CatLifeTheme
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.msoula.catlife.CatLifeAndroidTest
import com.msoula.catlife.MainActivity
import com.msoula.catlife.core.preview.PreviewAddEditNoteFormScreen
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AddEditNoteFormScreenTest : CatLifeAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testAddEditNoteScreenUi() {
        composeRule.activity.setContent {
            CatLifeTheme {
                PreviewAddEditNoteFormScreen()
            }
        }

        composeRule.onNodeWithText("Add").assertIsNotEnabled()

        composeRule
            .onNodeWithContentDescription("cats dropdown")
            .performClick()

        composeRule.onNodeWithText("catTest1").assertIsDisplayed()
        composeRule.onNodeWithText("catTest1").performClick()

        composeRule.onNodeWithText("Add").assertIsNotEnabled()

        composeRule.onNodeWithText("Title").performClick().performTextInput("Note test 1")
        composeRule.onNodeWithText("Add").assertIsNotEnabled()

        composeRule.onNodeWithText("Description").performClick().performTextInput("Description test 1")
        composeRule.onNodeWithText("Add").assertIsEnabled()
    }
}