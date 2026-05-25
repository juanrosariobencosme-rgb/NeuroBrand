package com.neuroband.app.services

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HapticFeedbackService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    
    fun vibrate(intensity: Int) {
        if (!hasVibrator()) return
        
        val amplitude = (intensity / 100.0 * 255).toInt().coerceIn(1, 255)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createOneShot(100, amplitude)
            vibrator?.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(100)
        }
    }
    
    fun vibratePattern(pattern: HapticPattern) {
        if (!hasVibrator()) return
        
        when (pattern) {
            HapticPattern.CALM -> {
                // Gentle, slow pulses
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val timings = longArrayOf(0, 200, 200, 200, 200, 200)
                    val amplitudes = intArrayOf(0, 100, 0, 100, 0, 0)
                    val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                    vibrator?.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(longArrayOf(0, 200, 200, 200, 200, 200), -1)
                }
            }
            HapticPattern.ALERT -> {
                // Sharp, quick pulses
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val timings = longArrayOf(0, 50, 50, 50, 50, 50)
                    val amplitudes = intArrayOf(0, 200, 0, 200, 0, 0)
                    val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                    vibrator?.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(longArrayOf(0, 50, 50, 50, 50, 50), -1)
                }
            }
            HapticPattern.SUCCESS -> {
                // Rising pattern
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val timings = longArrayOf(0, 100, 100, 150, 100, 200)
                    val amplitudes = intArrayOf(0, 100, 0, 150, 0, 200)
                    val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                    vibrator?.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(longArrayOf(0, 100, 100, 150, 100, 200), -1)
                }
            }
            HapticPattern.BREATHING_GUIDE -> {
                // Rhythmic breathing pattern (4-7-8)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val timings = longArrayOf(
                        0, 4000,  // Inhale (4s)
                        7000,      // Hold (7s)
                        8000,      // Exhale (8s)
                        0
                    )
                    val amplitudes = intArrayOf(0, 128, 64, 32, 0)
                    val effect = VibrationEffect.createWaveform(timings, amplitudes, 0) // Repeat
                    vibrator?.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(longArrayOf(0, 4000, 7000, 8000, 0), 0)
                }
            }
            HapticPattern.NOTIFICATION -> {
                // Standard notification pattern
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val timings = longArrayOf(0, 100, 50, 100)
                    val amplitudes = intArrayOf(0, 150, 0, 150)
                    val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                    vibrator?.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(longArrayOf(0, 100, 50, 100), -1)
                }
            }
        }
    }
    
    fun stopVibration() {
        vibrator?.cancel()
    }
    
    fun hasVibrator(): Boolean {
        return vibrator?.hasVibrator() == true
    }
    
    fun hasAmplitudeControl(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.hasAmplitudeControl() == true
        } else {
            false
        }
    }
}

enum class HapticPattern {
    CALM,
    ALERT,
    SUCCESS,
    BREATHING_GUIDE,
    NOTIFICATION
}
