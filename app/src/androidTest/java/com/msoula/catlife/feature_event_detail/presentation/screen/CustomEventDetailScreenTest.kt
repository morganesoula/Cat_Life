package com.msoula.catlife.feature_event_detail.presentation.screen

import CatLifeTheme
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.msoula.catlife.CatLifeAndroidTest
import com.msoula.catlife.MainActivity
import com.msoula.catlife.R
import com.msoula.catlife.core.preview.PreviewEventDetailScreen
import com.msoula.catlife.core.preview.event
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CustomEventDetailScreenTest : CatLifeAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testEventDetailScreenUi() {
        composeRule.activity.setContent {
            CatLifeTheme {
                PreviewEventDetailScreen(calendarEventDataSource)
            }
        }

        composeRule.onNodeWithText(event().title).assertIsDisplayed()
        composeRule.onNodeWithText(event().description ?: "").assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.current_event_all_day_text)).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.unknown_place)).assertDoesNotExist()
        composeRule.onNodeWithText(context.getString(R.string.network_needed_map)).assertIsDisplayed()
    }
}