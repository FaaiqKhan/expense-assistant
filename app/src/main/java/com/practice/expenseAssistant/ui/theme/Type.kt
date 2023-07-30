package com.practice.expenseAssistant.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.sp
import com.practice.expenseAssistant.R

val Lora = FontFamily(
    Font(R.font.lora_bold, FontWeight.Bold),
    Font(R.font.lora_medium, FontWeight.Medium),
    Font(R.font.lora_regular, FontWeight.Normal),
    Font(R.font.lora_semi_bold, FontWeight.SemiBold),
    Font(R.font.lora_italic, FontWeight.Thin),
    Font(R.font.lora_bold_italic, FontWeight.Bold),
    Font(R.font.lora_medium_italic, FontWeight.Medium),
    Font(R.font.lora_semi_bold_italic, FontWeight.SemiBold),
)

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontSize = 20.sp,
        fontFamily = Lora,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        fontWeight = FontWeight.Bold
    ),
    displayMedium = TextStyle(
        fontSize = 18.sp,
        fontFamily = Lora,
        lineHeight = 20.sp,
        letterSpacing = 0.4.sp,
        fontWeight = FontWeight.Bold
    ),
    displaySmall = TextStyle(
        fontSize = 16.sp,
        fontFamily = Lora,
        lineHeight = 20.sp,
        letterSpacing = 0.4.sp,
        fontWeight = FontWeight.Bold
    ),
    headlineLarge = TextStyle(
        fontSize = 16.sp,
        fontFamily = Lora,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        fontWeight = FontWeight.SemiBold
    ),
    headlineMedium = TextStyle(
        fontSize = 14.sp,
        fontFamily = Lora,
        lineHeight = 20.sp,
        letterSpacing = 0.4.sp,
        fontWeight = FontWeight.SemiBold
    ),
    headlineSmall = TextStyle(
        fontSize = 12.sp,
        fontFamily = Lora,
        lineHeight = 20.sp,
        letterSpacing = 0.4.sp,
        fontWeight = FontWeight.SemiBold
    ),
    titleLarge = TextStyle(
        fontSize = 16.sp,
        fontFamily = Lora,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        fontWeight = FontWeight.Medium
    ),
    titleMedium = TextStyle(
        fontSize = 14.sp,
        fontFamily = Lora,
        lineHeight = 20.sp,
        letterSpacing = 0.4.sp,
        fontWeight = FontWeight.Medium
    ),
    titleSmall = TextStyle(
        fontSize = 12.sp,
        fontFamily = Lora,
        lineHeight = 20.sp,
        letterSpacing = 0.4.sp,
        fontWeight = FontWeight.Medium
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        fontFamily = Lora,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontFamily = Lora,
        lineHeight = 22.sp,
        letterSpacing = 0.4.sp,
        fontWeight = FontWeight.Normal
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        fontFamily = Lora,
        lineHeight = 20.sp,
        letterSpacing = 0.4.sp,
        fontWeight = FontWeight.Normal
    ),
    labelLarge = TextStyle(
        fontSize = 16.sp,
        fontFamily = Lora,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        fontWeight = FontWeight.Thin
    ),
    labelMedium = TextStyle(
        fontSize = 14.sp,
        fontFamily = Lora,
        lineHeight = 22.sp,
        letterSpacing = 0.4.sp,
        fontWeight = FontWeight.Thin
    ),
    labelSmall = TextStyle(
        fontSize = 12.sp,
        fontFamily = Lora,
        lineHeight = 20.sp,
        letterSpacing = 0.4.sp,
        fontWeight = FontWeight.Thin
    )
)