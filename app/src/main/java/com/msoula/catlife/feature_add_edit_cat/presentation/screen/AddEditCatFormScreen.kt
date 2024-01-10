package com.msoula.catlife.feature_add_edit_cat.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.msoula.catlife.R
import com.msoula.catlife.core.presentation.CustomAddFormButton
import com.msoula.catlife.core.presentation.CustomCatFormDateColumn
import com.msoula.catlife.core.presentation.CustomErrorTextField
import com.msoula.catlife.core.presentation.CustomFormForHeightSpacer
import com.msoula.catlife.core.presentation.CustomTextField
import com.msoula.catlife.core.presentation.OnLifecycleEvent
import com.msoula.catlife.core.presentation.navigation.AddEditCatFormScreenNavArgs
import com.msoula.catlife.core.util.Constant
import com.msoula.catlife.core.util.LocalDim
import com.msoula.catlife.extension.getAllRaces
import com.msoula.catlife.extension.getFileName
import com.msoula.catlife.feature_add_edit_cat.data.state.AddEditCatFormState
import com.msoula.catlife.feature_add_edit_cat.presentation.AddEditCatDateEvent
import com.msoula.catlife.feature_add_edit_cat.presentation.AddEditCatFormEvent
import com.msoula.catlife.feature_calendar.presentation.screen.AutoCompleteTextView
import com.msoula.catlife.globalCurrentDay
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Destination(
    navArgsDelegate = AddEditCatFormScreenNavArgs::class
)
@Suppress("FunctionName")
@Composable
fun AddEditCatFormScreen(
    modifier: Modifier = Modifier,
    state: AddEditCatFormState,
    onUiEvent: (event: AddEditCatFormEvent, onCatAddedOrUpdated: (id: Int?) -> Unit) -> Unit,
    onDateEvent: (AddEditCatDateEvent, Long) -> Unit,
    onDeleteDateForEvent: (AddEditCatDateEvent) -> Unit,
    onClearClick: (onCatAddedOrUpdated: (id: Int?) -> Unit) -> Unit,
    onLifecycleEvent: (OnLifecycleEvent) -> Unit,
    initRaces: (List<String>) -> Unit,
    onCatAddedOrUpdated: (id: Int?) -> Unit,
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val birthdatePickerDateState =
        rememberDatePickerState(initialSelectedDateMillis = globalCurrentDay)

    val vaccinePickerDateState =
        rememberDatePickerState(initialSelectedDateMillis = globalCurrentDay)
    val fleaPickerDateState = rememberDatePickerState(initialSelectedDateMillis = globalCurrentDay)
    val dewormingPickerDateState =
        rememberDatePickerState(initialSelectedDateMillis = globalCurrentDay)

    // Cat picture
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { newUri ->
            newUri?.let { uri ->
                var input: InputStream? = null

                try {
                    val catPictureName = context.getFileName(uri)

                    input = context.contentResolver.openInputStream(newUri)
                        ?: return@rememberLauncherForActivityResult
                    val outputFile = catPictureName?.let {
                        context.filesDir.resolve(it)
                    }
                    outputFile?.let { input.copyTo(it.outputStream()) }

                    val localUri = outputFile?.toUri()
                    localUri?.let {
                        onUiEvent.invoke(
                            AddEditCatFormEvent.OnEditCatProfilePicturePathChanged(
                                it
                            ),
                            onCatAddedOrUpdated
                        )
                    }
                } finally {
                    input?.close()
                }

            }
        }

    // Gender
    val genderOptions = listOf(context.getString(R.string.male), context.getString(R.string.female))
    val genderSelected =
        if (state.catGender) context.getString(R.string.male) else context.getString(R.string.female)
    val onSelectionChange = { genderPicked: String ->
        if (genderPicked == context.getString(R.string.male)) {
            onUiEvent(AddEditCatFormEvent.EditCatGenderChanged(true), onCatAddedOrUpdated)
        } else {
            onUiEvent(AddEditCatFormEvent.EditCatGenderChanged(false), onCatAddedOrUpdated)
        }
    }

    // Neutered
    val neuteredOptions = listOf(context.getString(R.string.yes), context.getString(R.string.no))
    val neuteredSelected =
        if (state.catNeutered) context.getString(R.string.yes) else context.getString(R.string.no)
    val onNeuteredSelectionChanged = { neuteredValue: String ->
        if (neuteredValue == context.getString(R.string.yes)) {
            onUiEvent(AddEditCatFormEvent.EditCatNeuteredChanged(true), onCatAddedOrUpdated)
        } else {
            onUiEvent(AddEditCatFormEvent.EditCatNeuteredChanged(false), onCatAddedOrUpdated)
        }
    }

    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            // section picture
            Image(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable { launcher.launch("image/*") }
                    .align(Alignment.CenterHorizontally)
                    .semantics {
                        contentDescription = "cat profile picture"
                    },
                contentScale = ContentScale.Crop,
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(state.catPictureUri ?: R.drawable.catlife_add_cat).build()
                ),
                contentDescription = context.getString(R.string.uri_cat_picture)
            )

            if (state.catPictureUriError != null) {
                CustomErrorTextField(
                    value = context.getString(state.catPictureUriError),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            CustomFormForHeightSpacer(LocalDim.current.itemSpacing16)

            // section gender
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                genderOptions.forEach { genderPickedText ->
                    val borderStroke = if (genderPickedText != genderSelected) {
                        BorderStroke(2.dp, colorScheme.secondary)
                    } else {
                        BorderStroke(0.dp, Color.Transparent)
                    }

                    Text(
                        text = genderPickedText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        modifier = modifier
                            .clickable { (onSelectionChange(genderPickedText)) }
                            .clip(RoundedCornerShape(size = 12.dp))
                            .background(
                                if (genderPickedText == genderSelected) {
                                    colorScheme.secondary
                                } else {
                                    if (isSystemInDarkTheme()) {
                                        colorScheme.background
                                    } else {
                                        Color.LightGray
                                    }
                                }
                            )
                            .border(
                                borderStroke, RoundedCornerShape(12.dp)
                            )
                            .padding(
                                vertical = 12.dp, horizontal = 24.dp
                            )
                    )
                }
            }

            CustomFormForHeightSpacer(LocalDim.current.itemSpacing16)

            // section name
            CustomTextField(
                value = state.catName,
                onValueChange = {
                    onUiEvent(
                        AddEditCatFormEvent.EditCatNameChanged(it),
                        onCatAddedOrUpdated
                    )
                },
                labelText = R.string.cat_name,
                isError = state.catNameError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                singleLine = true,
                testTag = Constant.CAT_NAME_TEXT_FIELD
            )

            if (state.catNameError != null) {
                CustomErrorTextField(
                    value = context.getString(state.catNameError),
                    modifier = Modifier.align(Alignment.End)
                )
            }

            CustomFormForHeightSpacer(LocalDim.current.itemSpacing16)

            // section birthdate
            CustomCatFormDateColumn(
                modifier = modifier,
                labelText = context.getString(R.string.add_birthdate),
                datePickerState = birthdatePickerDateState,
                date = state.catBirthdate,
                error = state.catBirthdateError,
                onDateEvent = onDateEvent,
                type = AddEditCatDateEvent.OnBirthdateChanged,
                onDeleteDateForEvent = onDeleteDateForEvent,
                optional = false
            )

            // section neutered
            Text(
                text = if (state.catGender) context.getString(R.string.neutered) else context.getString(
                    R.string.neutered_female
                ),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 3.dp)
            )

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                neuteredOptions.forEach { neuteredValuePickedText ->
                    val borderStroke = if (neuteredValuePickedText != neuteredSelected) {
                        BorderStroke(2.dp, colorScheme.secondary)
                    } else {
                        BorderStroke(0.dp, Color.Transparent)
                    }

                    Text(
                        text = neuteredValuePickedText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        modifier = modifier
                            .clickable { onNeuteredSelectionChanged(neuteredValuePickedText) }
                            .clip(RoundedCornerShape(size = 12.dp))
                            .background(
                                if (neuteredValuePickedText == neuteredSelected) {
                                    colorScheme.secondary
                                } else {
                                    if (isSystemInDarkTheme()) {
                                        colorScheme.background
                                    } else {
                                        Color.LightGray
                                    }
                                }
                            )
                            .border(
                                borderStroke, RoundedCornerShape(12.dp)
                            )
                            .padding(
                                vertical = 12.dp, horizontal = 24.dp
                            )
                    )
                }
            }

            CustomFormForHeightSpacer(LocalDim.current.itemSpacing16)

            // section race
            AutoCompleteTextView(
                modifier = modifier.fillMaxWidth(),
                query = state.catRace,
                queryLabel = context.getString(R.string.race),
                predictions = state.allRaces,
                onQueryChanged = {
                    onUiEvent(
                        AddEditCatFormEvent.EditCatRaceChanged(it, context.getAllRaces()),
                        onCatAddedOrUpdated
                    )
                },
                onClearClick = {
                    onClearClick(onCatAddedOrUpdated)
                },
                onItemClick = {
                    onUiEvent(
                        AddEditCatFormEvent.EditCatRaceChanged(it, emptyList()),
                        onCatAddedOrUpdated
                    )
                },
                onFocusChanged = { focusState ->
                    if (focusState.isFocused) {
                        if (state.catRace == "") {
                            initRaces(context.getAllRaces())
                        }
                    } else {
                        onUiEvent(AddEditCatFormEvent.OnRaceFocusChanged, onCatAddedOrUpdated)
                    }
                }
            )

            if (state.catRaceError != null) {
                CustomErrorTextField(
                    value = context.getString(state.catRaceError),
                    modifier = modifier.align(Alignment.End)
                )
            }

            CustomFormForHeightSpacer(LocalDim.current.itemSpacing16)

            // section weight
            CustomTextField(
                value = state.weight,
                onValueChange = {
                    onUiEvent(AddEditCatFormEvent.EditCatWeightChanged(it), onCatAddedOrUpdated)
                },
                labelText = R.string.weight,
                isError = state.weightError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                testTag = Constant.CAT_WEIGHT_FIELD
            )

            if (state.weightError != null) {
                CustomErrorTextField(
                    value = context.getString(state.weightError),
                    modifier = Modifier.align(Alignment.End)
                )
            }

            CustomFormForHeightSpacer(LocalDim.current.itemSpacing16)

            // section coat
            CustomTextField(
                value = state.catCoat,
                onValueChange = {
                    onUiEvent(AddEditCatFormEvent.EditCatCoatChanged(it), onCatAddedOrUpdated)
                },
                labelText = R.string.coat_placeholder,
                isError = state.catCoatError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                singleLine = true,
                testTag = Constant.CAT_COAT_FIELD
            )

            if (state.catCoatError != null) {
                CustomErrorTextField(
                    value = context.getString(state.catCoatError),
                    modifier = Modifier.align(Alignment.End)
                )
            }

            CustomFormForHeightSpacer(LocalDim.current.itemSpacing16)

            // section vaccine
            CustomCatFormDateColumn(
                modifier = modifier,
                labelText = context.getString(R.string.add_vaccine),
                datePickerState = vaccinePickerDateState,
                date = state.catVaccineDate,
                error = state.catVaccineDateError,
                onDateEvent = onDateEvent,
                type = AddEditCatDateEvent.OnVaccineChanged,
                onDeleteDateForEvent = onDeleteDateForEvent
            )

            // section flea
            CustomCatFormDateColumn(
                modifier = modifier,
                labelText = context.getString(R.string.add_flea),
                datePickerState = fleaPickerDateState,
                date = state.catFleaDate,
                error = state.catFleaDateError,
                onDateEvent = onDateEvent,
                type = AddEditCatDateEvent.OnFleaChanged,
                onDeleteDateForEvent = onDeleteDateForEvent
            )

            // section deworming
            CustomCatFormDateColumn(
                modifier = modifier,
                labelText = context.getString(R.string.add_deworming),
                datePickerState = dewormingPickerDateState,
                date = state.catDewormingDate,
                error = state.catDewormingDateError,
                onDateEvent = onDateEvent,
                type = AddEditCatDateEvent.OnDewormingChanged,
                onDeleteDateForEvent = onDeleteDateForEvent
            )

            // section diseases
            CustomTextField(
                value = state.catDiseases,
                onValueChange = {
                    onUiEvent(AddEditCatFormEvent.EditCatDiseasesChanged(it), onCatAddedOrUpdated)
                },
                labelText = R.string.diseases,
                isError = state.catDiseasesError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                maxLines = 15,
                minLines = 3
            )

            if (state.catDiseasesError != null) {
                CustomErrorTextField(
                    value = context.getString(state.catDiseasesError),
                    modifier = Modifier.align(Alignment.End)
                )
            }

            CustomFormForHeightSpacer(LocalDim.current.itemSpacing32)

            // section submit
            CustomAddFormButton(
                modifier,
                { onUiEvent(AddEditCatFormEvent.Submit, onCatAddedOrUpdated) },
                state.enableSubmit,
                state.submitCatText
            )
        }
    }

    BackHandler {
        onLifecycleEvent(OnLifecycleEvent.OnBackPressed)
        navigator.popBackStack()
    }
}
