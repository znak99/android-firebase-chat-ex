package io.github.znak99.androidfirebasechatex.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import io.github.znak99.androidfirebasechatex.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

private val RalewayFont = GoogleFont("Raleway")

private val fontFamily = FontFamily(
    Font(googleFont = RalewayFont, fontProvider = provider)
)

private val typography = Typography()
val Typography = Typography(
    displayLarge = typography.displayLarge.copy(fontFamily = fontFamily),
    displayMedium = typography.displayMedium.copy(fontFamily = fontFamily),
    displaySmall = typography.displaySmall.copy(fontFamily = fontFamily),

    headlineLarge = typography.headlineLarge.copy(fontFamily = fontFamily),
    headlineMedium = typography.headlineMedium.copy(fontFamily = fontFamily),
    headlineSmall = typography.headlineSmall.copy(fontFamily = fontFamily),

    titleLarge = typography.titleLarge.copy(fontFamily = fontFamily),
    titleMedium = typography.titleMedium.copy(fontFamily = fontFamily),
    titleSmall = typography.titleSmall.copy(fontFamily = fontFamily),

    bodyLarge = typography.bodyLarge.copy(fontFamily = fontFamily),
    bodyMedium = typography.bodyMedium.copy(fontFamily = fontFamily),
    bodySmall = typography.bodySmall.copy(fontFamily = fontFamily),

    labelLarge = typography.labelLarge.copy(fontFamily = fontFamily),
    labelMedium = typography.labelMedium.copy(fontFamily = fontFamily),
    labelSmall = typography.labelSmall.copy(fontFamily = fontFamily),
)

val RalewayFontFamily = FontFamily(
    Font(googleFont = RalewayFont, fontProvider = provider),
    Font(googleFont = RalewayFont, fontProvider = provider, weight = FontWeight.Thin),
    Font(googleFont = RalewayFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = RalewayFont, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = RalewayFont, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = RalewayFont, fontProvider = provider, weight = FontWeight.Bold),
    Font(googleFont = RalewayFont, fontProvider = provider, weight = FontWeight.ExtraBold),
    Font(googleFont = RalewayFont, fontProvider = provider, weight = FontWeight.Black)
)