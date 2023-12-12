package com.msoula.catlife.feature_cat_detail.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.msoula.catlife.R
import com.msoula.catlife.core.presentation.CatLifeDetailScreenTopBar
import com.msoula.catlife.core.presentation.CustomAnyDetailElevatedCard
import com.msoula.catlife.core.presentation.CustomFormForHeightSpacer
import com.msoula.catlife.core.presentation.CustomFormForWidthSpacer
import com.msoula.catlife.core.presentation.navigation.CatDetailScreenNavArgs
import com.msoula.catlife.core.util.LocalDim
import com.msoula.catlife.core.util.LocalTextSize
import com.msoula.catlife.destinations.AddEditCatFormScreenDestination
import com.msoula.catlife.extension.accordingToLocale
import com.msoula.catlife.feature_cat_detail.data.state.CatDetailState
import com.msoula.catlife.feature_inventory.data.state.UiActionEvent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(
    navArgsDelegate = CatDetailScreenNavArgs::class
)
@Composable
fun CatDetailScreen(
    modifier: Modifier = Modifier,
    state: CatDetailState,
    onUiAction: (UiActionEvent, () -> Unit) -> Unit,
    navigator: DestinationsNavigator,
    goBackToListScreen: () -> Unit
) {
    val context = LocalContext.current

    if (state.openDeleteAlert) {
        DeleteDataAlertDialog(
            dismissDialog = {
                onUiAction(UiActionEvent.OnDismissRequest) {}
            },
            deleteElement = {
                onUiAction(
                    UiActionEvent.OnDeleteUi(
                        deleteData = true,
                        elementId = state.catId
                    ),
                    goBackToListScreen
                )
            }
        )
    }

    Scaffold(modifier = modifier) { paddingValues ->
        BoxWithConstraints(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .height(maxHeight.div(2.3f))
                    .padding(bottom = 1.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Card(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(top = 40.dp, start = 20.dp, end = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // section name and CRUD buttons
                        Row(
                            modifier = modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .weight(3f),
                                text = state.catName.uppercase(),
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Start
                            )

                            IconButton(
                                onClick = {
                                    navigator.navigate(
                                        AddEditCatFormScreenDestination(catUpdateId = state.catId)
                                    )
                                },
                                modifier = modifier
                                    .wrapContentWidth()
                                    .size(30.dp)
                                    .weight(0.5f),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = colorScheme.secondary
                                )
                            ) {
                                Icon(
                                    Icons.Outlined.Edit,
                                    contentDescription = context.getString(R.string.update_cat_title),
                                    modifier = modifier.padding(5.dp),
                                    tint = colorScheme.onSecondary
                                )
                            }

                            IconButton(
                                onClick = {
                                    onUiAction(
                                        UiActionEvent.OpenDeleteAlertDialog(state.catId),
                                        goBackToListScreen
                                    )
                                },
                                modifier = modifier
                                    .wrapContentWidth()
                                    .size(30.dp)
                                    .weight(0.5f),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = colorScheme.secondary
                                )
                            ) {
                                Icon(
                                    Icons.Outlined.Delete,
                                    contentDescription = context.getString(R.string.delete_confirmation),
                                    modifier = modifier.padding(5.dp),
                                    tint = colorScheme.onSecondary
                                )
                            }
                        }

                        CustomFormForHeightSpacer(height = 4.dp)

                        // section birthdate
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                Icons.Outlined.Cake,
                                contentDescription = context.getString(R.string.birthdate)
                            )

                            CustomFormForWidthSpacer(LocalDim.current.itemSpacing8)

                            Text(
                                text = state.catBirthdate.accordingToLocale(context),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        CustomFormForHeightSpacer(LocalDim.current.itemSpacing24)

                        // section race, fur & weight
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            CustomAnyDetailElevatedCard(
                                data = state.race,
                                label = context.getString(R.string.race_title)
                            )

                            CustomFormForWidthSpacer(width = 8.dp)

                            CustomAnyDetailElevatedCard(
                                data = state.fur,
                                label = context.getString(R.string.fur)
                            )

                            CustomFormForWidthSpacer(width = 8.dp)

                            CustomAnyDetailElevatedCard(
                                data = state.weight.toString() + " " + context.getString(R.string.lbs),
                                label = context.getString(R.string.weight_title)
                            )
                        }

                        CustomFormForHeightSpacer(LocalDim.current.itemSpacing24)

                        Text(
                            text = context.getString(R.string.medical_title),
                            modifier = modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            fontWeight = FontWeight.Medium
                        )

                        CustomFormForHeightSpacer(LocalDim.current.itemSpacing8)

                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            CustomAnyDetailElevatedCard(
                                data = if (state.gender) context.getString(R.string.male) else context.getString(
                                    R.string.female
                                ),
                                label = context.getString(R.string.gender_title)
                            )

                            CustomFormForWidthSpacer(width = LocalDim.current.itemSpacing8)

                            CustomAnyDetailElevatedCard(

                                data = if (state.neutered) context.getString(R.string.yes) else context.getString(
                                    R.string.no
                                ),
                                label = context.getString(R.string.neutered)
                            )
                        }

                        CustomFormForHeightSpacer(LocalDim.current.itemSpacing8)

                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            if (state.lastVaccineDate != 0L) {
                                CustomAnyDetailElevatedCard(
                                    modifier = modifier.padding(end = 8.dp),
                                    data = state.lastVaccineDate.accordingToLocale(
                                        context
                                    ), label = context.getString(R.string.last_vaccine)
                                )
                            } else {
                                Spacer(modifier = modifier.weight(1f))
                            }

                            if (state.lastFleaDate != 0L) {
                                CustomAnyDetailElevatedCard(
                                    modifier = modifier.padding(end = 8.dp),
                                    data = state.lastFleaDate.accordingToLocale(
                                        context
                                    ), label = context.getString(R.string.last_flea_treatment)
                                )
                            } else {
                                Spacer(modifier = modifier.weight(1f))
                            }

                            if (state.lastDewormingDate != 0L) {
                                CustomAnyDetailElevatedCard(
                                    data = state.lastDewormingDate.accordingToLocale(
                                        context
                                    ), label = context.getString(R.string.last_de_worming)
                                )
                            } else {
                                Spacer(modifier = modifier.weight(1f))
                            }
                        }

                        CustomFormForHeightSpacer(LocalDim.current.itemSpacing24)

                        state.diseases?.let { diseases ->
                            if (diseases.isNotBlank()) {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = colorScheme.secondary.copy(
                                            alpha = 0.4f
                                        )
                                    )
                                ) {
                                    Column(modifier = modifier.padding(LocalDim.current.itemSpacing8)) {
                                        Row(
                                            modifier = modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                        ) {
                                            Icon(
                                                Icons.Outlined.Warning,
                                                contentDescription = context.getString(R.string.important_title),
                                                modifier = modifier
                                                    .wrapContentWidth()
                                                    .padding(end = 8.dp)
                                            )

                                            Text(
                                                text = context.getString(R.string.important_title),
                                                fontSize = LocalTextSize.current.textSize16,
                                                style = MaterialTheme.typography.headlineSmall
                                            )
                                        }
                                        CustomFormForHeightSpacer(height = 4.dp)

                                        Text(
                                            text = diseases,
                                            modifier = modifier,
                                            fontSize = LocalTextSize.current.textSize16,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }

                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = modifier
                    .height(maxHeight.div(1.7f))
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                val profilePathURI: Any? = if (state.catProfilePath?.toUri()?.path != "null") {
                    state.catProfilePath?.toUri()
                } else {
                    R.drawable.catlife_placeholder
                }

                Image(
                    modifier = modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)),
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(context).data(profilePathURI).build()
                    ),
                    contentDescription = context.getString(R.string.uri_cat_picture),
                    contentScale = ContentScale.Crop
                )
            }

            Box(modifier = modifier.fillMaxSize()) {
                CatLifeDetailScreenTopBar(navigateUp = { navigator.popBackStack() })
            }
        }
    }

    BackHandler {
        navigator.popBackStack()
    }
}
