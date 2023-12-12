package com.msoula.catlife.feature_note.presentation.screen

import CatLifeTheme
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import com.msoula.catlife.CatLifeAndroidTest
import com.msoula.catlife.MainActivity
import com.msoula.catlife.core.preview.previewCatsFeedStateWithData
import com.msoula.catlife.feature_note.data.state.NoteUiState
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class NotesScreenTest : CatLifeAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testNotesScreenUi() {
        composeRule.activity.setContent {
            CatLifeTheme {
                NotesScreen(
                    catState = previewCatsFeedStateWithData(),
                    noteState = previewNotesFeedStateWithData(),
                    noteUiState = NoteUiState(),
                    navController = NavController(context),
                    onUiAction = { _, _ -> },
                    navigator = EmptyDestinationsNavigator,
                    goBackToListScreen = {}
                )
            }
        }

        composeRule.onNodeWithText("Note 1").assertIsDisplayed()
    }
}