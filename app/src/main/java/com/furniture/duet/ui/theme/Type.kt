package com.furniture.duet.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.furniture.duet.R

val MarcellusFontFamily = FontFamily(Font(R.font.marcellus_sc, FontWeight.Normal))
val MonsieurLaDoulaise = FontFamily(Font(R.font.monsieur_la_doulaise, FontWeight.Normal))
val MondaFontFamily = FontFamily(Font(R.font.monda, FontWeight.Normal))

// Set of Material typography styles to start with
val Typography = Typography(
    //H1 Heading
    labelMedium = TextStyle(
        fontFamily = MarcellusFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        color = FurnitureDetailTextColor,
        letterSpacing = 0.5.sp
    ),
    //Button
    labelSmall = TextStyle(
        fontFamily = MondaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    //Home Page
    titleLarge = TextStyle(
        fontFamily = MonsieurLaDoulaise,
        fontWeight = FontWeight.Normal,
        fontSize = 64.sp,
        color = TitleColor,
        letterSpacing = 0.5.sp
    ),
    //H2 Heading
    titleMedium = TextStyle(
        fontFamily = MarcellusFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        color = FurnitureDetailTextColor,
        letterSpacing = 0.5.sp
    ),
    //H3 Heading
    titleSmall = TextStyle(
        fontFamily = MondaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp,
        color = TitleColor
    ),
    //Price (Option)
    bodyLarge = TextStyle(
        fontFamily = MondaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    //Text
    bodyMedium = TextStyle(
        fontFamily = MondaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    //Price (the price on the cards)
    bodySmall = TextStyle(
        fontFamily = MondaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp
    ),
    displayMedium = TextStyle(
        fontFamily = MondaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)