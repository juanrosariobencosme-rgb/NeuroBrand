package com.neuroband.app.ui.theme

import androidx.compose.ui.graphics.Color

// NeuroBrand Color Palette
val SoftBlue = Color(0xFFAFCBFF)
val MintGreen = Color(0xFFB7F5D1)
val Lavender = Color(0xFFDCCBFF)
val WarmWhite = Color(0xFFF8FAFC)
val SmokeGray = Color(0xFFCBD5E1)
val DeepBlack = Color(0xFF0F172A)

// Material 3 Theme Colors
val Primary = SoftBlue
val OnPrimary = DeepBlack
val PrimaryContainer = SoftBlue.copy(alpha = 0.2f)
val OnPrimaryContainer = DeepBlack

val Secondary = MintGreen
val OnSecondary = DeepBlack
val SecondaryContainer = MintGreen.copy(alpha = 0.2f)
val OnSecondaryContainer = DeepBlack

val Tertiary = Lavender
val OnTertiary = DeepBlack
val TertiaryContainer = Lavender.copy(alpha = 0.2f)
val OnTertiaryContainer = DeepBlack

val Background = WarmWhite
val OnBackground = DeepBlack

val Surface = WarmWhite
val OnSurface = DeepBlack

val SurfaceVariant = SmokeGray.copy(alpha = 0.3f)
val OnSurfaceVariant = DeepBlack

val Error = Color(0xFFEF4444)
val ErrorContainer = Color(0xFFFECACA)
val OnError = Color(0xFFFFFFFF)
val OnErrorContainer = DeepBlack

// Custom Colors for Biometric States
val StressHigh = Color(0xFFEF4444)
val StressMedium = Color(0xFFF59E0B)
val StressLow = Color(0xFF10B981)

val EnergyHigh = Color(0xFF10B981)
val EnergyMedium = Color(0xFFF59E0B)
val EnergyLow = Color(0xFF6B7280)
