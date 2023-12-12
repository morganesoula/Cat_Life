package com.msoula.catlife

import CatLifeTheme
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.places.api.Places
import com.msoula.catlife.core.presentation.navigation.DestinationsNavHostSetUp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.todayIn

val globalCurrentDay = Clock.System.todayIn(TimeZone.currentSystemDefault())
    .atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)

        setContent {
            val navController = rememberNavController()
            CatLifeApp(navController = navController)
        }
    }
}

@Composable
fun CatLifeApp(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    CatLifeTheme {
        Surface(modifier = modifier) {
            DestinationsNavHostSetUp(
                navController
            )
        }
    }
}
