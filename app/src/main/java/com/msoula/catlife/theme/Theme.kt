import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.msoula.catlife.theme.Shapes
import com.msoula.catlife.theme.Typography
import com.msoula.catlife.theme.md_theme_dark_background
import com.msoula.catlife.theme.md_theme_dark_backgroundSwitch
import com.msoula.catlife.theme.md_theme_dark_error
import com.msoula.catlife.theme.md_theme_dark_onBackground
import com.msoula.catlife.theme.md_theme_dark_onError
import com.msoula.catlife.theme.md_theme_dark_onPrimary
import com.msoula.catlife.theme.md_theme_dark_onSecondary
import com.msoula.catlife.theme.md_theme_dark_onSurface
import com.msoula.catlife.theme.md_theme_dark_primary
import com.msoula.catlife.theme.md_theme_dark_secondary
import com.msoula.catlife.theme.md_theme_dark_surface
import com.msoula.catlife.theme.md_theme_light_background
import com.msoula.catlife.theme.md_theme_light_backgroundSwitch
import com.msoula.catlife.theme.md_theme_light_error
import com.msoula.catlife.theme.md_theme_light_onBackground
import com.msoula.catlife.theme.md_theme_light_onError
import com.msoula.catlife.theme.md_theme_light_onPrimary
import com.msoula.catlife.theme.md_theme_light_onSecondary
import com.msoula.catlife.theme.md_theme_light_onSurface
import com.msoula.catlife.theme.md_theme_light_primary
import com.msoula.catlife.theme.md_theme_light_secondary
import com.msoula.catlife.theme.md_theme_light_surface

private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    secondaryContainer = md_theme_light_backgroundSwitch
)


private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    secondaryContainer = md_theme_dark_backgroundSwitch
)

@Composable
fun CatLifeTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (!useDarkTheme) {
        LightColors
    } else {
        DarkColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}