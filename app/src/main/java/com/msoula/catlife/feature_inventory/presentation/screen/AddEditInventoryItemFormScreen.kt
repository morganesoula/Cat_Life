package com.msoula.catlife.feature_inventory.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.msoula.catlife.R
import com.msoula.catlife.core.presentation.CustomAddFormButton
import com.msoula.catlife.core.presentation.CustomErrorTextField
import com.msoula.catlife.core.presentation.CustomFormForHeightSpacer
import com.msoula.catlife.core.presentation.CustomTextField
import com.msoula.catlife.core.presentation.OnLifecycleEvent
import com.msoula.catlife.core.util.LocalDim
import com.msoula.catlife.feature_inventory.data.state.AddEditInventoryFormState
import com.msoula.catlife.feature_inventory.data.state.InventoryItemFormEvent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun AddEditInventoryItemFormScreen(
    modifier: Modifier = Modifier,
    state: AddEditInventoryFormState,
    onUiEvent: (event: InventoryItemFormEvent, onInventoryItemAdded: () -> Unit) -> Unit,
    onLifecycleEvent: (OnLifecycleEvent) -> Unit,
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current

    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Column(
            Modifier
                .background(colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            CustomTextField(
                value = state.currentLabel,
                onValueChange = {
                    onUiEvent(InventoryItemFormEvent.InitLabel(it)) { navigator.popBackStack() }
                },
                labelText = R.string.label_title,
                isError = state.currentLabelError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                singleLine = true
            )

            if (state.currentLabelError != null) {
                CustomErrorTextField(
                    value = context.getString(state.currentLabelError),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = modifier.height(LocalDim.current.itemSpacing16))

            CustomTextField(
                value = state.currentQuantity,
                onValueChange = {
                    onUiEvent(
                        InventoryItemFormEvent.InitQuantity(
                            it
                        )
                    ) { navigator.popBackStack() }
                },
                labelText = R.string.quantity_title,
                isError = state.currentQuantityError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            if (state.currentQuantityError != null) {
                CustomErrorTextField(
                    value = context.getString(state.currentQuantityError),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            CustomFormForHeightSpacer(LocalDim.current.itemSpacing32)

            CustomAddFormButton(
                modifier,
                { onUiEvent(
                    InventoryItemFormEvent.SubmitElement
                ) { navigator.popBackStack() } },
                state.enableSubmit,
                R.string.add_submit_text_button
            )
        }
    }

    BackHandler {
        onLifecycleEvent(OnLifecycleEvent.OnBackPressed)
        navigator.popBackStack()
    }
}
