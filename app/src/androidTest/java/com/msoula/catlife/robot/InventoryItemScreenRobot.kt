package com.msoula.catlife.robot

import android.content.Context
import android.support.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.msoula.catlife.MainActivity

class InventoryItemScreenRobot(
    private val composeRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {

    private lateinit var labelInput: String
    private lateinit var quantityInput: String

    fun clickInventoryInTheMenu(): InventoryItemScreenRobot {
        composeRule.onNodeWithContentDescription("Inventory").performClick()
        return this
    }

    fun addItem(context: Context, @StringRes inventoryItemFAB: Int): InventoryItemScreenRobot {
        composeRule.onNodeWithContentDescription(context.getString(inventoryItemFAB)).performClick()
        return this
    }

    fun assertLabelIsVisible(): InventoryItemScreenRobot {
        composeRule.onNodeWithText("Label").assertIsDisplayed()
        return this
    }

    fun inputLabel(label: String): InventoryItemScreenRobot {
        composeRule.onNodeWithText("Label").performTextInput(label)
        labelInput = label
        return this
    }

    fun inputQuantity(quantity: Long): InventoryItemScreenRobot {
        composeRule.onNodeWithText("Quantity").performTextInput(quantity.toString())
        quantityInput = quantity.toString()
        return this
    }

    fun addItem(): InventoryItemScreenRobot {
        composeRule.onNodeWithText("Add").performClick()
        return this
    }

    fun assertLabelIsDisplayed(): InventoryItemScreenRobot {
        composeRule.onNodeWithText(labelInput).assertIsDisplayed()
        return this
    }

    fun assertQuantityIsDisplayed(): InventoryItemScreenRobot {
        composeRule.onNodeWithText(quantityInput).assertIsDisplayed()
        return this
    }

    fun pressBack(): InventoryItemScreenRobot {
        Espresso.pressBack()
        return this
    }

    fun assertLabelFormFieldIsNotDisplayed() : InventoryItemScreenRobot {
        composeRule.onNodeWithText("Label").assertDoesNotExist()
        return this
    }

    fun incrementQuantity(times: Int): InventoryItemScreenRobot {
        repeat(times) {
            composeRule.onNodeWithContentDescription("increment quantity").performClick()
        }
        return this
    }
}