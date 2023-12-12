package com.msoula.catlife.feature_cat_detail.presentation.screen

import CatLifeTheme
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.msoula.catlife.CatLifeAndroidTest
import com.msoula.catlife.MainActivity
import com.msoula.catlife.R
import com.msoula.catlife.core.preview.PreviewCatDetailScreen
import com.msoula.catlife.core.preview.cats
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CatDetailScreenTest : CatLifeAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testCatDetailScreenUi() {
        composeRule.activity.setContent {
            CatLifeTheme {
                PreviewCatDetailScreen(catDataSource, noteDataSource)
            }
        }

        composeRule.onNodeWithText(cats().first().name.uppercase()).assertIsDisplayed()
        composeRule.onNodeWithText(cats().first().race).assertIsDisplayed()
        composeRule.onNodeWithText(cats().first().coat).assertIsDisplayed()
        composeRule.onNodeWithText(cats().first().weight.toString() + " lbs").assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.diseases)).assertDoesNotExist()
    }
}