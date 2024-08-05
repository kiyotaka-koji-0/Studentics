package com.studentics.kiyo.utils

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.studentics.kiyo.R


fun getFont(name: String): FontFamily {
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    val googleFont = GoogleFont(name = name)
    val font = FontFamily( Font(googleFont = googleFont, fontProvider = provider))

    return font
}