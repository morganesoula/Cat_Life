package com.msoula.catlife.integration_test

import CatLifeTheme
import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.msoula.catlife.CatLifeAndroidTest
import com.msoula.catlife.MainActivity
import com.msoula.catlife.core.presentation.navigation.DestinationsNavHostSetUp
import com.msoula.catlife.robot.HomeScreenRobot
import com.msoula.catlife.robot.NoteScreenRobot
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CreateNoteE2E : CatLifeAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testCreatingNote() {
        val homeRobot = HomeScreenRobot(composeRule)
        val noteRobot = NoteScreenRobot(composeRule)
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        composeRule.activity.setContent {
            val navController = rememberNavController()

            CatLifeTheme {
                DestinationsNavHostSetUp(navController)
            }
        }

        // Populate with one cat
        homeRobot
            .clickNewCat()
            .inputCatName("Lotus")
            .selectCatBirthdate(1)
            .selectRace("Balinese")
            .inputWeight(2.7f)
            .defineFurColor("Black")
            .inputDiseases("No diseases known")
            .addCat(device)
            .pressBack()

        noteRobot
            .clickNoteInMenu()
            .clickNewNote()
            .assertAddButtonIsNotEnabled()
            .clickOnPickCat()
            .selectCat("Lotus")
            .assertAddButtonIsNotEnabled()
            .inputTitle("Random test title 1")
            .assertAddButtonIsNotEnabled()
            .inputDescription("Random test description 1")
            .assertAddButtonIsEnabled()
            .addCat()
            .assertTitleIsVisible()
            .pressBack()
            .assertTitleFieldIsNotVisible()
    }
}