package com.msoula.catlife.feature_note_detail.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.msoula.catlife.R
import com.msoula.catlife.core.presentation.CatLifeDetailScreenTopBar
import com.msoula.catlife.core.presentation.CustomFormForHeightSpacer
import com.msoula.catlife.core.presentation.navigation.NoteDetailScreenNavArgs
import com.msoula.catlife.core.util.LocalDim
import com.msoula.catlife.core.util.LocalTextSize
import com.msoula.catlife.destinations.AddEditNoteFormScreenDestination
import com.msoula.catlife.destinations.NotesScreenDestination
import com.msoula.catlife.feature_cat_detail.presentation.screen.DeleteDataAlertDialog
import com.msoula.catlife.feature_inventory.data.state.UiActionEvent
import com.msoula.catlife.feature_note_detail.data.NoteDetailUiState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(
    navArgsDelegate = NoteDetailScreenNavArgs::class
)
@Composable
fun NoteDetailScreen(
    modifier: Modifier = Modifier,
    state: NoteDetailUiState,
    onUiAction: (UiActionEvent, () -> Unit) -> Unit,
    navigator: DestinationsNavigator,
    goBackToListScreen: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(state.noteDeleted) {
        if (state.noteDeleted) {
            navigator.navigate(NotesScreenDestination)
        }
    }

    if (state.openDeleteAlert) {
        DeleteDataAlertDialog(
            dismissDialog = {
                onUiAction(UiActionEvent.OnDismissRequest) {}
            },
            deleteElement = {
                onUiAction(
                    UiActionEvent.OnDeleteUi(
                        deleteData = true,
                        elementId = state.noteId ?: -1
                    ),
                    goBackToListScreen
                )
            }
        )
    }

    Scaffold(
        modifier = modifier
    ) { paddingValues ->
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
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // section title and CRUD buttons
                        Row(
                            modifier = modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .weight(3f),
                                text = state.noteTitle,
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Start
                            )

                            IconButton(
                                onClick = {
                                    navigator.navigate(
                                        AddEditNoteFormScreenDestination(
                                            noteUpdateId = state.noteId ?: -1
                                        )
                                    )
                                },
                                modifier = modifier
                                    .wrapContentWidth()
                                    .size(30.dp)
                                    .weight(0.5f),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Icon(
                                    Icons.Outlined.Edit,
                                    contentDescription = context.getString(R.string.update_note_title),
                                    modifier = modifier.padding(5.dp),
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                            }

                            IconButton(
                                onClick = {
                                    onUiAction(
                                        UiActionEvent.OpenDeleteAlertDialog(state.noteId ?: -1),
                                        goBackToListScreen
                                    )
                                },
                                modifier = modifier
                                    .wrapContentWidth()
                                    .size(30.dp)
                                    .weight(0.5f),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Icon(
                                    Icons.Outlined.Delete,
                                    contentDescription = context.getString(R.string.delete_confirmation),
                                    modifier = modifier.padding(5.dp),
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }

                        CustomFormForHeightSpacer(LocalDim.current.itemSpacing24)

                        Text(
                            text = state.noteDescription,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(
                                    LocalDim.current.itemSpacing8
                                ),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = LocalTextSize.current.textSize16
                        )
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

            Box(
                modifier = modifier
                    .fillMaxWidth()
            ) {
                CatLifeDetailScreenTopBar(navigateUp = { navigator.popBackStack() })
            }
        }
    }

    BackHandler {
        navigator.popBackStack()
    }
}