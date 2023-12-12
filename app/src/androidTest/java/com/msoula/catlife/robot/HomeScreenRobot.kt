package com.msoula.catlife.robot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.SearchCondition
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import com.msoula.catlife.MainActivity
import com.msoula.catlife.core.util.Constant
import kotlin.time.Duration.Companion.seconds

class HomeScreenRobot(
    private val composeRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {

    private lateinit var catName: String
    private lateinit var catWeight: String
    private lateinit var catRace: String
    private lateinit var catDiseases: String

    fun clickOnHomeMenu(): HomeScreenRobot {
        composeRule.onNodeWithText("Home").performClick()
        return this
    }

    fun clickNewCat(): HomeScreenRobot {
        composeRule.onNodeWithContentDescription("add new cat").performClick()
        return this
    }

    fun inputCatName(name: String): HomeScreenRobot {
        composeRule.onNodeWithText("Cat name").performTextInput(name)
        catName = name

        return this
    }

    fun selectCatBirthdate(day: Int): HomeScreenRobot {
        composeRule.onAllNodesWithText("To define").onFirst().performClick()
        composeRule.onNodeWithText(day.toString()).performClick()
        composeRule.onNodeWithText("OK").performClick()
        return this
    }

    fun selectRace(race: String): HomeScreenRobot {
        composeRule.onNodeWithText("Race").performTextInput(race)
        catRace = race
        return this
    }

    fun inputWeight(lbs: Float): HomeScreenRobot {
        composeRule.onNodeWithText("Weight (lbs)").performTextInput(lbs.toString())
        catWeight = lbs.toString()
        return this
    }

    fun defineFurColor(color: String): HomeScreenRobot {
        composeRule.onNodeWithText("Color of the fur").performTextInput(color)
        closeKeyboard()
        return this
    }

    fun inputDiseases(diseases: String): HomeScreenRobot {
        composeRule.onNodeWithText("Known diseases (optional):").performTextInput(diseases)
        catDiseases = diseases
        closeKeyboard()
        return this
    }

    fun addCat(device: UiDevice): HomeScreenRobot {
        device.waitForObject(By.textContains("Add"))
            .scroll(Direction.DOWN, 1f)

        composeRule.onNodeWithTag(Constant.SUBMIT_CAT_BUTTON).assertIsDisplayed()
        composeRule.onNodeWithTag(Constant.SUBMIT_CAT_BUTTON).performClick()
        return this
    }

    fun assertNameIsVisible(): HomeScreenRobot {
        composeRule.onNodeWithText(catName.uppercase(), useUnmergedTree = true).assertIsDisplayed()
        return this
    }

    fun assertWeightIsVisible(): HomeScreenRobot {
        composeRule.onNodeWithText("$catWeight lbs").assertIsDisplayed()
        return this
    }

    fun assertRaceIsVisible(): HomeScreenRobot {
        composeRule.onNodeWithText(catRace).assertIsDisplayed()
        return this
    }

    fun assertDiseasesAreVisible(): HomeScreenRobot {
        composeRule.onNodeWithText(catDiseases).assertIsDisplayed()
        return this
    }

    fun assertVaccineDateIsNotVisible(): HomeScreenRobot {
        composeRule.onNodeWithText("Last vaccine date").assertDoesNotExist()
        return this
    }

    fun pressBack(): HomeScreenRobot {
        Espresso.pressBack()
        return this
    }

    fun displayCatDetail(name: String): HomeScreenRobot {
        composeRule.onNodeWithText(name.uppercase()).performClick()
        return this
    }

    fun assertLowQuantityIsDisplayed(): HomeScreenRobot {
        composeRule.onNodeWithText("Almost out").assertIsDisplayed()
        return this
    }

    fun assertLowQuantityDoesNotExist(): HomeScreenRobot {
        composeRule.onNodeWithText("Almost out").assertDoesNotExist()
        return this
    }
    private fun closeKeyboard() {
        Espresso.closeSoftKeyboard()
    }

    private fun <R> UiDevice.wait(condition: SearchCondition<R>, timeout: kotlin.time.Duration): R {
        return wait(condition, timeout.inWholeMilliseconds)
    }
    private fun UiDevice.waitForObject(selector: BySelector, timeout: kotlin.time.Duration = 5.seconds): UiObject2 {
        if (wait(Until.hasObject(selector), timeout)) {
            return findObject(selector)
        }

        error("Object with selector [$selector] not found")
    }


}