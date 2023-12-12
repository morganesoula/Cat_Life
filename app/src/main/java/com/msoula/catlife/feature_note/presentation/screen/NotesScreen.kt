package com.msoula.catlife.feature_note.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material.icons.outlined.ContentPasteOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.msoula.catlife.R
import com.msoula.catlife.core.presentation.CommonListWithLazyColumn
import com.msoula.catlife.core.presentation.CustomColumnNoData
import com.msoula.catlife.core.presentation.CustomElevatedCard
import com.msoula.catlife.core.presentation.CustomFAButton
import com.msoula.catlife.core.presentation.CustomFormForWidthSpacer
import com.msoula.catlife.core.presentation.LinearLoadingScreen
import com.msoula.catlife.core.presentation.navigation.BottomBar
import com.msoula.catlife.core.util.LocalDim
import com.msoula.catlife.core.util.LocalTextSize
import com.msoula.catlife.destinations.AddEditNoteFormScreenDestination
import com.msoula.catlife.destinations.NoteDetailScreenDestination
import com.msoula.catlife.feature_cat_detail.presentation.screen.DeleteDataAlertDialog
import com.msoula.catlife.feature_inventory.data.state.UiActionEvent
import com.msoula.catlife.feature_main.data.state.CatsFeedUiState
import com.msoula.catlife.feature_note.data.state.NoteFeedUiState
import com.msoula.catlife.feature_note.data.state.NoteUiState
import com.msoula.catlife.globalCurrentDay
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import commsoulacatlifedatabase.CatEntity
import commsoulacatlifedatabase.Note

@Destination
@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    catState: CatsFeedUiState,
    noteState: NoteFeedUiState,
    noteUiState: NoteUiState,
    navController: NavController,
    onUiAction: (UiActionEvent, () -> Unit) -> Unit,
    navigator: DestinationsNavigator,
    goBackToListScreen: () -> Unit
) {
    val cats = when (catState) {
        is CatsFeedUiState.Success -> catState.catsFeed
        else -> emptyList()
    }

    if (noteUiState.openDeleteAlert) {
        DeleteDataAlertDialog(
            dismissDialog = {
                onUiAction(UiActionEvent.OnDismissRequest) {}
            }
        ) {
            onUiAction(
                UiActionEvent.OnDeleteUi(
                    deleteData = true,
                    elementId = noteUiState.itemId
                ),
                goBackToListScreen
            )
        }
    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            CustomFAButton(
                openForm = { navigator.navigate(AddEditNoteFormScreenDestination()) },
                contentDescription = R.string.add_note_title
            )
        },
        bottomBar = {
            BottomBar(navController = navController)
        },
        modifier = modifier
    ) { paddingValues: PaddingValues ->
        Column(modifier = modifier.padding(paddingValues)) {
            if (cats.size > 1) {
                CatHeader(
                    modifier = modifier
                        .wrapContentHeight(),
                    cats = cats,
                    onCatFiltered = { id ->
                        onUiAction(UiActionEvent.OnCatFiltered(id)) {}
                    }
                )
            }

            when (noteState) {
                is NoteFeedUiState.Loading -> LinearLoadingScreen()
                is NoteFeedUiState.Empty -> NoNotes()
                is NoteFeedUiState.Success -> {
                    Column {
                        CommonListWithLazyColumn(
                            paddingValues = paddingValues,
                            items = noteState.notes,
                            swipeToDelete = { note: Note ->
                                onUiAction(UiActionEvent.OpenDeleteAlertDialog(note.id.toInt())) {}
                            },
                            idKey = { note -> note.id.toInt() }
                        ) {
                            CustomElevatedCard(
                                onClick = {
                                    navigator.navigate(
                                        NoteDetailScreenDestination(
                                            noteDetailId = it.id.toInt()
                                        )
                                    )
                                }
                            ) {
                                NoteRowItemComposable(
                                    noteDate = it.date,
                                    noteTitle = it.title,
                                    catName = cats.first { cat: CatEntity -> cat.id == it.catId }.name,
                                    catProfilePath = cats.first { cat: CatEntity -> cat.id == it.catId }.profilePicturePath
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun NoNotes() {
    CustomColumnNoData(
        noDataText = R.string.no_note_title,
        icon = Icons.Outlined.ContentPasteOff
    )
}

@Composable
fun CatHeader(modifier: Modifier = Modifier, cats: List<CatEntity>, onCatFiltered: (Int?) -> Unit) {
    val context = LocalContext.current
    var selectedCatId by remember { mutableIntStateOf(-1) }

    Spacer(modifier = modifier.height(LocalDim.current.itemSpacing16))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = LocalDim.current.itemSpacing16)
    ) {
        Box(
            modifier = modifier
                .size(width = 70.dp, height = 70.dp)
                .border(
                    width = if (selectedCatId == -1) 3.dp else 0.dp,
                    color = if (selectedCatId == -1) colorScheme.secondary else Color.Transparent,
                    shape = CircleShape
                )
                .padding(8.dp)
        ) {
            Image(
                imageVector = Icons.Default.FilterListOff,
                contentDescription = null,
                colorFilter = ColorFilter.tint(colorScheme.onSurface),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colorScheme.secondary.copy(alpha = 0.2f))
                    .width(60.dp)
                    .height(60.dp)
                    .clickable(
                        onClick = {
                            selectedCatId = -1
                            onCatFiltered(null)
                        }
                    )
            )
        }

        CustomFormForWidthSpacer(width = LocalDim.current.itemSpacing8)

        cats.forEach { cat: CatEntity ->
            Box(
                modifier = modifier
                    .size(width = 70.dp, height = 70.dp)
                    .border(
                        width = if (selectedCatId == cat.id.toInt()) 3.dp else 0.dp,
                        color = if (selectedCatId == cat.id.toInt()) colorScheme.secondary else Color.Transparent,
                        shape = CircleShape
                    )
                    .padding(8.dp)
            ) {
                if (cat.profilePicturePath != "null") {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(context)
                                .data(cat.profilePicturePath.toUri())
                                .build()
                        ),
                        contentDescription = context.getString(R.string.uri_cat_picture),
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .width(60.dp)
                            .height(60.dp)
                            .clip(CircleShape)
                            .clickable {
                                selectedCatId = cat.id.toInt()
                                onCatFiltered(cat.id.toInt())
                            }

                    )
                } else {
                    TextButton(
                        modifier = modifier
                            .width(60.dp)
                            .height(60.dp),
                        onClick = {
                            selectedCatId = cat.id.toInt()
                            onCatFiltered(cat.id.toInt())
                        },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.secondary.copy(
                                alpha = 0.2f
                            )
                        )
                    ) {
                        Text(
                            text = cat.name.first().uppercase(),
                            fontSize = LocalTextSize.current.textSize24,
                            color = colorScheme.onSurface
                        )
                    }
                }

                CustomFormForWidthSpacer(width = LocalDim.current.itemSpacing24)
            }
        }

        Spacer(modifier = modifier.height(LocalDim.current.itemSpacing16))
    }
}

fun previewNotesFeedStateWithData(): NoteFeedUiState {
    return NoteFeedUiState.Success(
        listOf(
            Note(
                1,
                1,
                "",
                "catTest1",
                date = globalCurrentDay,
                content = "Descrption 1",
                title = "Note 1"
            ),
            Note(
                2,
                2,
                "",
                "catTest2",
                date = globalCurrentDay,
                content = "Descrption 2",
                title = "Note 2"
            )
        )
    )
}