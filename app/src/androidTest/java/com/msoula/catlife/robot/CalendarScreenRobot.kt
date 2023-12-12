package com.msoula.catlife.robot

import android.support.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.msoula.catlife.MainActivity
import dagger.hilt.android.testing.HiltAndroidTest

@HiltAndroidTest
class CalendarScreenRobot(
    private val composeRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {

    private lateinit var eventTitle: String
    fun clickOnCalendarMenu(): CalendarScreenRobot {
        composeRule.onNodeWithText("Calendar").performClick()
        return this
    }

    fun clickNewEvent(): CalendarScreenRobot {
        composeRule.onNodeWithContentDescription("Add new event").performClick()
        return this
    }

    fun inputTitle(title: String): CalendarScreenRobot {
        composeRule.onNodeWithText("Title").performTextInput(title)
        eventTitle = title
        return this
    }

    fun clearTitleField(): CalendarScreenRobot {
        composeRule.onNodeWithText("Title").performTextClearance()
        return this
    }

    fun inputPlace(place: String): CalendarScreenRobot {
        composeRule.onNodeWithText("Place").performTextInput(place)
        return this
    }

    fun toggleAllDay(@StringRes resourceId: String): CalendarScreenRobot {
        composeRule.onNodeWithTag(resourceId).performTouchInput { swipeRight() }
        return this
    }

    fun closeKeyboard(): CalendarScreenRobot {
        Espresso.closeSoftKeyboard()
        return this
    }

    fun assertAddButtonIsEnabled(): CalendarScreenRobot {
        composeRule.onNodeWithText("Add").assertIsEnabled()
        return this
    }

    fun assertAddButtonIsNotEnabled(): CalendarScreenRobot {
        composeRule.onNodeWithText("Add").assertIsNotEnabled()
        return this
    }

    fun addEvent(): CalendarScreenRobot {
        composeRule.onNodeWithText("Add").performClick()
        return this
    }

    fun assertTitleIsDisplayed(): CalendarScreenRobot {
        composeRule.onNodeWithText(eventTitle).assertIsDisplayed()
        return this
    }

    fun assertUnkownPlaceIsDisplayed(): CalendarScreenRobot {
        composeRule.onNodeWithText("Unknown place").assertIsDisplayed()
        return this
    }

    fun pressBack(): CalendarScreenRobot {
        Espresso.pressBack()
        return this
    }

    fun assertTitleFieldIsNotVisible(): CalendarScreenRobot {
        composeRule.onNodeWithText("Title").assertDoesNotExist()
        return this
    }

    fun assertAllDayIsDisplayed(): CalendarScreenRobot {
        composeRule.onNodeWithText("All day").assertIsDisplayed()
        return this
    }
}