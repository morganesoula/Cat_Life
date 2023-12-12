package com.msoula.catlife.robot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.msoula.catlife.MainActivity

class NoteScreenRobot(
    private val composeRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {

    private lateinit var noteTitle: String

    fun clickNoteInMenu(): NoteScreenRobot {
        composeRule.onNodeWithContentDescription("Note").performClick()
        return this
    }

    fun clickNewNote(): NoteScreenRobot {
        composeRule.onNodeWithContentDescription("Add new note").performClick()
        return this
    }

    fun clickOnPickCat(): NoteScreenRobot {
        composeRule.onNodeWithText("Pick cat").performClick()
        return this
    }

    fun selectCat(catName: String): NoteScreenRobot {
        composeRule.onAllNodesWithText(catName).onLast().performClick()
        return this
    }

    fun inputTitle(title: String): NoteScreenRobot {
        composeRule.onNodeWithText("Title").performTextInput(title)
        noteTitle = title
        return this
    }

    fun inputDescription(description: String): NoteScreenRobot {
        composeRule.onNodeWithText("Description").performTextInput(description)
        return this
    }

    fun assertAddButtonIsEnabled(): NoteScreenRobot {
        composeRule.onNodeWithText("Add").assertIsEnabled()
        return this
    }

    fun assertAddButtonIsNotEnabled(): NoteScreenRobot {
        composeRule.onNodeWithText("Add").assertIsNotEnabled()
        return this
    }

    fun addCat(): NoteScreenRobot {
        composeRule.onNodeWithText("Add").performClick()
        return this
    }

    fun assertTitleIsVisible(): NoteScreenRobot {
        composeRule.onNodeWithText(noteTitle).assertIsDisplayed()
        return this
    }

    fun pressBack(): NoteScreenRobot {
        Espresso.pressBack()
        return this
    }

    fun assertTitleFieldIsNotVisible(): NoteScreenRobot {
        composeRule.onNodeWithText("Title").assertDoesNotExist()
        return this
    }
}