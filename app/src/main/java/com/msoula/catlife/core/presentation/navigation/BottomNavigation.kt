package com.msoula.catlife.core.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.NoteAdd
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.msoula.catlife.NavGraphs
import com.msoula.catlife.R
import com.msoula.catlife.appCurrentDestinationAsState
import com.msoula.catlife.destinations.CalendarScreenDestination
import com.msoula.catlife.destinations.Destination
import com.msoula.catlife.destinations.HomeScreenDestination
import com.msoula.catlife.destinations.InventoryScreenDestination
import com.msoula.catlife.destinations.NotesScreenDestination
import com.msoula.catlife.globalCurrentDay
import com.msoula.catlife.startAppDestination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.Direction

enum class BottomBarDestination(
    val direction: Direction,
    val icon: ImageVector,
    @StringRes val label: Int,
    val contentDescription: String
) {
    Home(
        HomeScreenDestination,
        icon = Icons.Outlined.Home,
        label = R.string.home_nav_label,
        contentDescription = "Home"
    ),
    Calendar(
        CalendarScreenDestination(selectedStartDate = globalCurrentDay),
        icon = Icons.Outlined.DateRange,
        label = R.string.calendar_nav_label,
        contentDescription = "Calendar"
    ),
    Notes(
        NotesScreenDestination,
        icon = Icons.Outlined.NoteAdd,
        label = R.string.note_nav_label,
        contentDescription = "Note"
    ),
    Inventory(
        InventoryScreenDestination,
        icon = Icons.Outlined.ShoppingCart,
        label = R.string.inventory_nav_label,
        contentDescription = "Inventory"
    )
}

@Composable
fun BottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentDestination: Destination = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    NavigationBar(
        modifier = modifier
            .border(1.dp, Color.Transparent, RoundedCornerShape(8.dp)),
        tonalElevation = 8.dp,
        containerColor = colorScheme.onSecondary
    ) {
        BottomBarDestination.entries.forEach { destination ->
            NavigationBarItem(
                modifier = modifier
                    .semantics {
                        contentDescription = destination.contentDescription
                    },
                selected = if (destination.direction.route.contains("calendar") && currentDestination.route.contains("calendar")) true else currentDestination == destination.direction,
                onClick = {
                    navController.navigate(destination.direction) {
                        popUpTo(NavGraphs.root.startAppDestination)
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        destination.icon,
                        contentDescription = stringResource(id = destination.label)
                    )
                },
                label = { Text(text = stringResource(id = destination.label)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorScheme.secondary,
                    unselectedIconColor = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.4f) else Color.DarkGray,
                    selectedTextColor = colorScheme.secondary,
                    unselectedTextColor = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.4f) else colorScheme.onSurfaceVariant,
                    indicatorColor = colorScheme.onSecondary
                )
            )
        }
    }
}
