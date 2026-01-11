package pl.sofantastica.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import pl.sofantastica.R

val BellezaFontFamily = FontFamily(Font(R.font.belleza_regular, FontWeight.W500))
val RobotoFontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal))

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelMedium = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 13.sp,
        lineHeight = 21.sp,
        letterSpacing = 6.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 16.sp,
        lineHeight = 21.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 13.sp,
        lineHeight = 21.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = BellezaFontFamily,
        fontSize = 56.sp,
        letterSpacing = 0.5.sp,

    ),
    titleMedium = TextStyle(
        fontFamily = BellezaFontFamily,
        fontSize = 24.sp,
        letterSpacing = 0.5.sp,

        )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)