package com.msoula.catlife.feature_note.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.msoula.catlife.R
import com.msoula.catlife.core.presentation.CustomAddFormButton
import com.msoula.catlife.core.presentation.CustomErrorTextField
import com.msoula.catlife.core.presentation.CustomFormForHeightSpacer
import com.msoula.catlife.core.presentation.CustomTextField
import com.msoula.catlife.core.presentation.OnLifecycleEvent
import com.msoula.catlife.core.presentation.navigation.AddEditNoteFormScreenNavArgs
import com.msoula.catlife.core.util.LocalDim
import com.msoula.catlife.feature_note.data.state.AddEditNoteFormEvent
import com.msoula.catlife.feature_note.data.state.AddEditNoteFormState
import com.msoula.catlife.feature_note.data.state.CatForNoteUiState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import commsoulacatlifedatabase.CatEntity

@OptIn(ExperimentalMaterial3Api::class)
@Destination(
    navArgsDelegate = AddEditNoteFormScreenNavArgs::class
)
@Composable
fun AddEditNoteFormScreen(
    modifier: Modifier = Modifier,
    state: AddEditNoteFormState,
    onUiEvent: (event: AddEditNoteFormEvent, onNoteAddedOrUpdated: (id: Int?) -> Unit) -> Unit,
    onLifecycleEvent: (OnLifecycleEvent) -> Unit,
    catState: CatForNoteUiState,
    onNoteAddedOrUpdated: (id: Int?) -> Unit,
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current

    val availableCats: Map<Int, CatEntity> = when (catState) {
        is CatForNoteUiState.Success -> catState.catForNote.associateBy { cat ->
            (cat.id.toInt())
        }

        else -> emptyMap()
    }

    var expanded by remember { mutableStateOf(false) }
    var selectedCatText by remember {
        mutableStateOf(availableCats[0]?.name ?: "")
    }

    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(
                    rememberScrollState()
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // section cat
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }) {
                TextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .semantics { contentDescription = "cats dropdown" },
                    readOnly = true,
                    value = if (state.currentNoteId == 0) {
                        selectedCatText
                    } else {
                        availableCats.values.firstOrNull { it.id.toInt() == state.currentCatId }?.name
                            ?: ""
                    },
                    onValueChange = { },
                    label = {
                        Row {
                            Text(
                                context.getString(R.string.pick_cat),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = context.getString(R.string.pick_cat)
                            )
                        }

                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        focusedContainerColor = colorScheme.background,
                        unfocusedContainerColor = colorScheme.background,
                        focusedIndicatorColor = colorScheme.secondary
                    )
                )

                ExposedDropdownMenu(
                    modifier = modifier
                        .background(colorScheme.background)
                        .border(1.dp, colorScheme.surface),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    availableCats.forEach { selectedCat ->
                        DropdownMenuItem(
                            modifier = modifier.background(colorScheme.background),
                            text = {
                                Text(
                                    text = selectedCat.value.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                selectedCatText = selectedCat.value.name
                                expanded = false
                                onUiEvent(
                                    AddEditNoteFormEvent.OnCatChanged(selectedCat.value),
                                    onNoteAddedOrUpdated
                                )
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            Spacer(modifier = modifier.height(LocalDim.current.itemSpacing16))

            //section title
            CustomTextField(
                value = state.currentTitle,
                onValueChange = {
                    onUiEvent(AddEditNoteFormEvent.OnTitleChanged(it), onNoteAddedOrUpdated)
                },
                labelText = R.string.note_title_title_text,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                singleLine = true
            )

            if (state.currentTitleErrorMessage != null) {
                CustomErrorTextField(
                    value = context.getString(state.currentTitleErrorMessage),
                    modifier = Modifier.align(Alignment.End)
                )
            }

            Spacer(modifier = modifier.height(LocalDim.current.itemSpacing16))

            //section description
            CustomTextField(
                value = state.currentDescription,
                onValueChange = {
                    onUiEvent(AddEditNoteFormEvent.OnDescriptionChanged(it), onNoteAddedOrUpdated)
                },
                labelText = R.string.note_description_title_text,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = false,
                maxLines = 30,
                minLines = 3
            )

            if (state.currentDescriptionErrorMessage != null) {
                CustomErrorTextField(
                    value = context.getString(state.currentDescriptionErrorMessage),
                    modifier = Modifier.align(Alignment.End)
                )
            }

            CustomFormForHeightSpacer(LocalDim.current.itemSpacing32)

            CustomAddFormButton(
                modifier,
                { onUiEvent(AddEditNoteFormEvent.Submit, onNoteAddedOrUpdated) },
                state.enableSubmit,
                state.submitNoteText
            )
        }
    }

    BackHandler {
        onLifecycleEvent(OnLifecycleEvent.OnBackPressed)
        navigator.popBackStack()
    }
}