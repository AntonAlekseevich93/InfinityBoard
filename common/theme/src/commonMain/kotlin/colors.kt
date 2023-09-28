import androidx.compose.ui.graphics.Color

data class AppColors(
   val mainBackgroundColor: Color,
   val mainTextColor: Color,
   val mainPlaceholderTextColor: Color,
   val mainIconsColor: Color,
   val cardBackgroundDark: Color,
   val cardBackgroundLight: Color,
)

val lightPaletteTheme = AppColors(
    mainBackgroundColor = Color(0xFF151515),
    mainTextColor = Color(0xFFe5e5e5),
    mainPlaceholderTextColor = Color(0xFFb2b2b2),
    mainIconsColor = Color.White,
    cardBackgroundDark = Color(0xFF2A2A2A),
    cardBackgroundLight = Color(0xFF3A3A3D),
)

val darkPaletteTheme = AppColors(
    mainBackgroundColor = Color(0xFF151515),
    mainTextColor = Color(0xFFe5e5e5),
    mainPlaceholderTextColor = Color(0xFFb2b2b2),
    mainIconsColor = Color.White,
    cardBackgroundDark = Color(0xFF2A2A2A),
    cardBackgroundLight = Color(0xFF3A3A3D),
)