package com.msoula.catlife.feature_note_detail.presentation.screen

import CatLifeTheme
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.msoula.catlife.CatLifeAndroidTest
import com.msoula.catlife.MainActivity
import com.msoula.catlife.core.preview.PreviewNoteDetailScreen
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class NoteDetailScreenTest : CatLifeAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testNoteDetailScreenUi() {
        composeRule.activity.setContent {
            CatLifeTheme {
                PreviewNoteDetailScreen(noteDataSource)
            }
        }

        composeRule.onNodeWithText("test note 1").assertIsDisplayed()
        composeRule.onNodeWithText("random test 1 description").assertIsDisplayed()
    }
}