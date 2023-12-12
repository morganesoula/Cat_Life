package com.msoula.catlife.integration_test

import CatLifeTheme
import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import com.msoula.catlife.CatLifeAndroidTest
import com.msoula.catlife.MainActivity
import com.msoula.catlife.R
import com.msoula.catlife.core.presentation.navigation.DestinationsNavHostSetUp
import com.msoula.catlife.robot.CalendarScreenRobot
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CreateEventE2E: CatLifeAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testCreatingEvent() {
        val calendarRobot = CalendarScreenRobot(composeRule)

        composeRule.activity.setContent {
            val navController = rememberNavController()

            CatLifeTheme {
                DestinationsNavHostSetUp(navController)
            }
        }

        calendarRobot
            .clickOnCalendarMenu()
            .clickNewEvent()
            .inputTitle("Random event title 1")
            .assertAddButtonIsNotEnabled()
            .inputPlace("1 rue des souris, 35000 Rennes")
            .assertAddButtonIsNotEnabled()
            .toggleAllDay(context.getString(R.string.place_switch_test_tag))
            .closeKeyboard()
            .assertAddButtonIsEnabled()
            .clearTitleField()
            .assertAddButtonIsNotEnabled()
            .inputTitle("Random event title 1")
            .closeKeyboard()
            .assertAddButtonIsEnabled()
            .addEvent()
            .assertTitleIsDisplayed()
            .assertUnkownPlaceIsDisplayed()
            .pressBack()
            .assertTitleFieldIsNotVisible()
            .assertAllDayIsDisplayed()
    }
}