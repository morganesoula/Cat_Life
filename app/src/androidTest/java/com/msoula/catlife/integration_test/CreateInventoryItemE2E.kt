package com.msoula.catlife.integration_test

import CatLifeTheme
import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import com.msoula.catlife.CatLifeAndroidTest
import com.msoula.catlife.MainActivity
import com.msoula.catlife.R
import com.msoula.catlife.core.presentation.navigation.DestinationsNavHostSetUp
import com.msoula.catlife.robot.HomeScreenRobot
import com.msoula.catlife.robot.InventoryItemScreenRobot
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CreateInventoryItemE2E : CatLifeAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testCreatingInventoryItem() {
        val inventoryRobot = InventoryItemScreenRobot(composeRule)
        val homeScreenRobot = HomeScreenRobot(composeRule)

        composeRule.activity.setContent {
            val navController = rememberNavController()

            CatLifeTheme {
                DestinationsNavHostSetUp(navController)
            }
        }

        inventoryRobot
            .clickInventoryInTheMenu()
            .addItem(context, R.string.add_new_inventory_item_title)
            .assertLabelIsVisible()
            .inputLabel("Croquettes")
            .inputQuantity(1L)
            .addItem()
            .assertLabelIsDisplayed()
            .assertQuantityIsDisplayed()
            .pressBack()
            .assertLabelFormFieldIsNotDisplayed()

        homeScreenRobot
            .clickOnHomeMenu()
            .assertLowQuantityIsDisplayed()

        inventoryRobot
            .clickInventoryInTheMenu()
            .incrementQuantity(4)

        homeScreenRobot
            .clickOnHomeMenu()
            .assertLowQuantityDoesNotExist()
    }
}