package com.example.sound

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class GameFeedback(context: Context) {

    private val vibrator: Vibrator? = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    } catch (_: Exception) {
        null
    }

    private var toneGenerator: ToneGenerator? = try {
        ToneGenerator(AudioManager.STREAM_MUSIC, 85)
    } catch (_: Exception) {
        null
    }

    fun onCorrectAnswer() {
        // Crisp haptic
        vibrate(40)
        // High upbeat chime
        playTone(ToneGenerator.TONE_PROP_BEEP2, 120)
    }

    fun onWrongAnswer() {
        // Double shake haptic
        vibratePattern(longArrayOf(0, 70, 50, 140))
        // Error buzzer tone
        playTone(ToneGenerator.TONE_PROP_NACK, 250)
    }

    fun onStreakBoost() {
        vibrate(80)
        playTone(ToneGenerator.TONE_CDMA_CONFIRM, 180)
    }

    fun onPowerUpUsed() {
        vibrate(50)
        playTone(ToneGenerator.TONE_PROP_PROMPT, 150)
    }

    fun onShieldAbsorbed() {
        vibratePattern(longArrayOf(0, 100, 50, 100))
        playTone(ToneGenerator.TONE_SUP_CONFIRM, 200)
    }

    fun onTimerTick() {
        playTone(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE, 50)
    }

    fun onGameOver() {
        vibratePattern(longArrayOf(0, 120, 80, 200))
        playTone(ToneGenerator.TONE_PROP_NACK, 400)
    }

    private fun vibrate(durationMs: Long) {
        try {
            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(durationMs)
                }
            }
        } catch (_: Exception) {
            // Ignore if vibration disallowed on device
        }
    }

    private fun vibratePattern(timings: LongArray) {
        try {
            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createWaveform(timings, -1))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(timings, -1)
                }
            }
        } catch (_: Exception) {
            // Ignore
        }
    }

    private fun playTone(toneType: Int, durationMs: Int) {
        try {
            toneGenerator?.startTone(toneType, durationMs)
        } catch (_: Exception) {
            // Ignore audio glitches
        }
    }

    fun release() {
        try {
            toneGenerator?.release()
            toneGenerator = null
        } catch (_: Exception) {
            // Ignore
        }
    }
}
