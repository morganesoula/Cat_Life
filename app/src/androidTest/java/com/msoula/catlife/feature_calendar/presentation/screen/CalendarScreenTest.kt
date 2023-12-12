package com.msoula.catlife.feature_calendar.presentation.screen

import CatLifeTheme
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.msoula.catlife.CatLifeAndroidTest
import com.msoula.catlife.MainActivity
import com.msoula.catlife.core.preview.PreviewCalendarScreen
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CalendarScreenTest : CatLifeAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testCalendarScreenUi() {
        composeRule.activity.setContent {
            CatLifeTheme {
                PreviewCalendarScreen()
            }
        }

        composeRule.onNodeWithText("event 1").assertIsDisplayed()
    }
}