package menu_bar

import ApplicationTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import tooltip_area.TooltipIconArea

@Composable
fun LeftMenuBar(
    searchListener: () -> Unit,
) {
    Row(modifier = Modifier.background(ApplicationTheme.colors.mainBackgroundWindowDarkColor)) {
        Column(
            modifier = Modifier.widthIn(max = 42.dp).fillMaxHeight().width(42.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TooltipIconArea(
                text = "Поиск",//todo
                drawableResName = Drawable.drawable_ic_search,
                showPointerEvent = true,
                modifier = Modifier.padding(top = 10.dp),
                onClick = searchListener
            )

            TooltipIconArea(
                text = "Теги",//todo
                drawableResName = Drawable.drawable_ic_tag,
                showPointerEvent = true,
                modifier = Modifier.padding(top = 10.dp),
            ) {
                println("открыли меню")
            }

            TooltipIconArea(
                text = "Время",//todo
                drawableResName = Drawable.drawable_ic_timer_off,
                showPointerEvent = true,
                modifier = Modifier.padding(top = 10.dp),
                iconSize = 22.dp,
                pointerInnerPadding = 4.dp
            ) {
                println("открыли меню")
            }

            TooltipIconArea(
                text = "Активность",//todo
                drawableResName = Drawable.drawable_ic_activity,
                showPointerEvent = true,
                modifier = Modifier.padding(top = 10.dp),
            ) {
                println("открыли меню")
            }

            TooltipIconArea(
                text = "Открыть случайную заметку",//todo
                drawableResName = Drawable.drawable_ic_random,
                showPointerEvent = true,
                modifier = Modifier.padding(top = 10.dp),
            ) {
                println("открыли меню")
            }

            Spacer(modifier = Modifier.weight(1f, fill = true))

            TooltipIconArea(
                text = Strings.another_storage,
                drawableResName = Drawable.drawable_ic_folder,
                showPointerEvent = true,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                println("открыли меню")
            }
            TooltipIconArea(
                text = Strings.settings,
                drawableResName = Drawable.drawable_ic_settings,
                showPointerEvent = true,
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                println("открыли меню")
            }
        }

        Divider(
            modifier = Modifier.fillMaxHeight().width(1.dp),
            thickness = 1.dp,
            color = ApplicationTheme.colors.divider
        )
    }
}