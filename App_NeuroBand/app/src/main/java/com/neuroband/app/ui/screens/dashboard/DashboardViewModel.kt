package com.neuroband.app.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neuroband.app.data.ble.BleRepository
import com.neuroband.app.data.ble.BiometricData
import com.neuroband.app.ui.screens.dashboard.EmotionalState
import com.neuroband.app.ui.theme.Lavender
import com.neuroband.app.ui.theme.MintGreen
import com.neuroband.app.ui.theme.SoftBlue
import com.neuroband.app.ui.theme.StressHigh
import com.neuroband.app.ui.theme.StressLow
import com.neuroband.app.ui.theme.StressMedium
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val bleRepository: BleRepository
) : ViewModel() {
    
    val biometricData: StateFlow<BiometricData?> = bleRepository.biometricData
    val batteryLevel: StateFlow<Int?> = bleRepository.batteryLevel
    
    val emotionalState: StateFlow<EmotionalState> = bleRepository.biometricData
        .map { data ->
            calculateEmotionalState(data)
        }
    
    private fun calculateEmotionalState(biometricData: BiometricData?): EmotionalState {
        val stressLevel = biometricData?.stressLevel ?: 0
        val heartRate = biometricData?.heartRate ?: 0
        
        return when {
            stressLevel <= 30 && heartRate <= 80 -> EmotionalState(
                title = "Estado: Calmado",
                description = "Tu estado emocional es estable y tranquilo",
                color = MintGreen
            )
            stressLevel <= 60 && heartRate <= 100 -> EmotionalState(
                title = "Estado: Moderado",
                description = "Niveles de estrés dentro de rangos normales",
                color = SoftBlue
            )
            stressLevel <= 80 -> EmotionalState(
                title = "Estado: Alerta",
                description = "Detectamos niveles elevados de estrés",
                color = Lavender
            )
            else -> EmotionalState(
                title = "Estado: Alto Estrés",
                description = "Recomendamos tomar medidas para relajarte",
                color = StressHigh
            )
        }
    }
    
    fun disconnect() {
        viewModelScope.launch {
            bleRepository.disconnect()
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        // Keep connection alive when navigating away
    }
}
