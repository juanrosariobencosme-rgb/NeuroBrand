package com.neuroband.app.services

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioFeedbackService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<SoundType, Int>()
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: kotlinx.coroutines.flow.StateFlow<Boolean> = _isPlaying.asStateFlow()
    
    private val _currentSound = MutableStateFlow<SoundType?>(null)
    val currentSound: kotlinx.coroutines.flow.StateFlow<SoundType?> = _currentSound.asStateFlow()
    
    init {
        initializeSoundPool()
    }
    
    private fun initializeSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        
        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()
        
        // Load sounds (TODO: Add actual sound files to res/raw/)
        // soundMap[SoundType.CALM] = soundPool?.load(context, R.raw.calm_sound, 1) ?: 0
        // soundMap[SoundType.ALERT] = soundPool?.load(context, R.raw.alert_sound, 1) ?: 0
        // soundMap[SoundType.BREATHING] = soundPool?.load(context, R.raw.breathing_guide, 1) ?: 0
        // soundMap[SoundType.SUCCESS] = soundPool?.load(context, R.raw.success_sound, 1) ?: 0
        // soundMap[SoundType.NOTIFICATION] = soundPool?.load(context, R.raw.notification_sound, 1) ?: 0
    }
    
    fun playSound(soundType: SoundType, volume: Float = 1.0f, loop: Int = 0) {
        val soundId = soundMap[soundType] ?: return
        
        _isPlaying.value = true
        _currentSound.value = soundType
        
        soundPool?.play(
            soundId,
            volume,
            volume,
            1,
            loop,
            1.0f
        )
    }
    
    fun stopSound() {
        soundPool?.stop(1)
        _isPlaying.value = false
        _currentSound.value = null
    }
    
    fun pauseSound() {
        soundPool?.pause(1)
        _isPlaying.value = false
    }
    
    fun resumeSound() {
        soundPool?.resume(1)
        _isPlaying.value = true
    }
    
    fun setVolume(volume: Float) {
        soundPool?.setVolume(1, volume, volume)
    }
    
    fun release() {
        soundPool?.release()
        soundPool = null
        soundMap.clear()
    }
    
    fun playBreathingGuide() {
        // Play breathing guide with specific timing
        playSound(SoundType.BREATHING, loop = -1)
    }
    
    fun playCalmMusic() {
        playSound(SoundType.CALM, loop = -1)
    }
    
    fun playAlertSound() {
        playSound(SoundType.ALERT, loop = 0)
    }
    
    fun playSuccessSound() {
        playSound(SoundType.SUCCESS, loop = 0)
    }
    
    fun playNotificationSound() {
        playSound(SoundType.NOTIFICATION, loop = 0)
    }
}

enum class SoundType {
    CALM,
    ALERT,
    BREATHING,
    SUCCESS,
    NOTIFICATION,
    MEDITATION,
    NATURE,
    WHITE_NOISE
}
