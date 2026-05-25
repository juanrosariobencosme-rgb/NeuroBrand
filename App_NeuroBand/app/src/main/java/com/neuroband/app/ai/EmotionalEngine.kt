package com.neuroband.app.ai

import com.neuroband.app.data.ble.BiometricData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmotionalEngine @Inject constructor() {
    
    private val _emotionalState = MutableStateFlow<EmotionalAnalysis>(EmotionalAnalysis())
    val emotionalState: Flow<EmotionalAnalysis> = _emotionalState.asStateFlow()
    
    private val _recommendations = MutableStateFlow<List<Recommendation>>(emptyList())
    val recommendations: Flow<List<Recommendation>> = _recommendations.asStateFlow()
    
    private val _prediction = MutableStateFlow<EmotionalPrediction?>(null)
    val prediction: Flow<EmotionalPrediction?> = _prediction.asStateFlow()
    
    private val historicalData = mutableListOf<BiometricData>()
    private val maxHistorySize = 100
    
    fun analyzeBiometricData(biometricData: BiometricData) {
        // Add to historical data
        historicalData.add(biometricData)
        if (historicalData.size > maxHistorySize) {
            historicalData.removeAt(0)
        }
        
        // Perform emotional analysis
        val analysis = performAnalysis(biometricData, historicalData)
        _emotionalState.value = analysis
        
        // Generate recommendations
        val recs = generateRecommendations(analysis)
        _recommendations.value = recs
        
        // Generate prediction
        val pred = generatePrediction(historicalData)
        _prediction.value = pred
    }
    
    private fun performAnalysis(
        currentData: BiometricData,
        history: List<BiometricData>
    ): EmotionalAnalysis {
        // Calculate stress level based on multiple factors
        val stressScore = calculateStressScore(currentData, history)
        
        // Calculate energy level
        val energyScore = calculateEnergyScore(currentData, history)
        
        // Determine emotional state
        val emotionalState = determineEmotionalState(stressScore, energyScore)
        
        // Calculate confidence
        val confidence = calculateConfidence(history)
        
        return EmotionalAnalysis(
            emotionalState = emotionalState,
            stressLevel = stressScore,
            energyLevel = energyScore,
            heartRateVariability = calculateHRV(history),
            confidence = confidence,
            timestamp = System.currentTimeMillis()
        )
    }
    
    private fun calculateStressScore(
        currentData: BiometricData,
        history: List<BiometricData>
    ): Int {
        var score = 0
        
        // Heart rate contribution (40%)
        val heartRateScore = when {
            currentData.heartRate > 100 -> 80
            currentData.heartRate > 90 -> 60
            currentData.heartRate > 80 -> 40
            currentData.heartRate > 70 -> 20
            else -> 10
        }
        score += (heartRateScore * 0.4).toInt()
        
        // GSR contribution (25%)
        val gsrScore = (currentData.gsrValue / 100.0) * 100
        score += (gsrScore * 0.25).toInt()
        
        // Respiration rate contribution (20%)
        val respirationScore = when {
            currentData.respirationRate > 25 -> 80
            currentData.respirationRate > 20 -> 60
            currentData.respirationRate > 16 -> 40
            else -> 20
        }
        score += (respirationScore * 0.2).toInt()
        
        // Historical trend contribution (15%)
        val trendScore = if (history.size >= 5) {
            val recent = history.takeLast(5)
            val avgHeartRate = recent.map { it.heartRate }.average()
            if (currentData.heartRate > avgHeartRate + 10) 80
            else if (currentData.heartRate > avgHeartRate + 5) 60
            else 30
        } else {
            30
        }
        score += (trendScore * 0.15).toInt()
        
        return score.coerceIn(0, 100)
    }
    
    private fun calculateEnergyScore(
        currentData: BiometricData,
        history: List<BiometricData>
    ): Int {
        var score = 50 // Base score
        
        // Heart rate influence
        score += when {
            currentData.heartRate > 90 -> 20
            currentData.heartRate > 70 -> 10
            currentData.heartRate < 60 -> -10
            else -> 0
        }
        
        // Stress level inverse influence
        score -= (currentData.stressLevel * 0.3).toInt()
        
        // Historical trend
        if (history.size >= 10) {
            val recent = history.takeLast(10)
            val avgStress = recent.map { it.stressLevel }.average()
            if (currentData.stressLevel < avgStress) {
                score += 15
            }
        }
        
        return score.coerceIn(0, 100)
    }
    
    private fun determineEmotionalState(
        stressScore: Int,
        energyScore: Int
    ): EmotionalState {
        return when {
            stressScore <= 30 && energyScore >= 60 -> EmotionalState.CALM
            stressScore <= 50 && energyScore >= 40 -> EmotionalState.FOCUSED
            stressScore <= 40 && energyScore <= 40 -> EmotionalState.RELAXED
            stressScore <= 60 -> EmotionalState.MODERATE
            stressScore <= 80 -> EmotionalState.ALERT
            else -> EmotionalState.STRESSED
        }
    }
    
    private fun calculateHRV(history: List<BiometricData>): Int {
        if (history.size < 2) return 50
        
        val heartRates = history.map { it.heartRate }
        val variance = heartRates.map { (it - heartRates.average()).pow(2) }.average()
        val stdDev = kotlin.math.sqrt(variance)
        
        // Convert to HRV-like score (higher is better)
        return ((stdDev / heartRates.average()) * 100).toInt().coerceIn(0, 100)
    }
    
    private fun calculateConfidence(history: List<BiometricData>): Float {
        // Confidence increases with more historical data
        return when {
            history.size >= 50 -> 0.95f
            history.size >= 30 -> 0.85f
            history.size >= 10 -> 0.70f
            history.size >= 5 -> 0.50f
            else -> 0.30f
        }
    }
    
    private fun generateRecommendations(analysis: EmotionalAnalysis): List<Recommendation> {
        val recommendations = mutableListOf<Recommendation>()
        
        when (analysis.emotionalState) {
            EmotionalState.STRESSED -> {
                recommendations.add(
                    Recommendation(
                        type = RecommendationType.BREATHING,
                        title = "Respiración Coherente",
                        description = "5 minutos de respiración 4-7-8 para reducir el estrés",
                        priority = Priority.HIGH,
                        duration = 300
                    )
                )
                recommendations.add(
                    Recommendation(
                        type = RecommendationType.HAPTIC_CALM,
                        title = "Activar Calma Háptica",
                        description = "Vibraciones suaves para inducir relajación",
                        priority = Priority.HIGH,
                        duration = 0
                    )
                )
            }
            EmotionalState.ALERT -> {
                recommendations.add(
                    Recommendation(
                        type = RecommendationType.BREAK,
                        title = "Pausa Consciente",
                        description = "Toma un descanso de 2 minutos",
                        priority = Priority.MEDIUM,
                        duration = 120
                    )
                )
            }
            EmotionalState.MODERATE -> {
                recommendations.add(
                    Recommendation(
                        type = RecommendationType.MINDFULNESS,
                        title = "Mindfulness Rápido",
                        description = "1 minuto de atención plena",
                        priority = Priority.LOW,
                        duration = 60
                    )
                )
            }
            EmotionalState.RELAXED -> {
                recommendations.add(
                    Recommendation(
                        type = RecommendationType.MAINTENANCE,
                        title = "Mantener el Estado",
                        description = "Continúa con tu actividad actual",
                        priority = Priority.LOW,
                        duration = 0
                    )
                )
            }
            EmotionalState.FOCUSED -> {
                recommendations.add(
                    Recommendation(
                        type = RecommendationType.MAINTENANCE,
                        title = "Estado Óptimo",
                        description = "Aprovecha tu estado de concentración",
                        priority = Priority.LOW,
                        duration = 0
                    )
                )
            }
            EmotionalState.CALM -> {
                recommendations.add(
                    Recommendation(
                        type = RecommendationType.MAINTENANCE,
                        title = "Estado de Calma",
                        description = "Tu estado emocional es excelente",
                        priority = Priority.LOW,
                        duration = 0
                    )
                )
            }
        }
        
        return recommendations
    }
    
    private fun generatePrediction(history: List<BiometricData>): EmotionalPrediction? {
        if (history.size < 10) return null
        
        val recent = history.takeLast(10)
        val avgStress = recent.map { it.stressLevel }.average()
        val stressTrend = calculateTrend(recent.map { it.stressLevel.toDouble() })
        
        // Predict stress level in 2 hours
        val predictedStress = (avgStress + (stressTrend * 20)).toInt().coerceIn(0, 100)
        
        return EmotionalPrediction(
            predictedStressLevel = predictedStress,
            predictedEnergyLevel = calculateEnergyPrediction(recent),
            timeHorizon = "2h",
            confidence = 0.75f,
            timestamp = System.currentTimeMillis()
        )
    }
    
    private fun calculateTrend(values: List<Double>): Double {
        if (values.size < 2) return 0.0
        
        val n = values.size
        val sumX = (n * (n - 1)) / 2.0
        val sumY = values.sum()
        val sumXY = values.mapIndexed { index, value -> index * value }.sum()
        val sumX2 = (n * (n - 1) * (2 * n - 1)) / 6.0
        
        val slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX)
        return slope
    }
    
    private fun calculateEnergyPrediction(history: List<BiometricData>): Int {
        val avgEnergy = history.map { it.energyLevel }.average()
        return avgEnergy.toInt().coerceIn(0, 100)
    }
    
    fun clearHistory() {
        historicalData.clear()
    }
}

data class EmotionalAnalysis(
    val emotionalState: EmotionalState = EmotionalState.MODERATE,
    val stressLevel: Int = 50,
    val energyLevel: Int = 50,
    val heartRateVariability: Int = 50,
    val confidence: Float = 0.5f,
    val timestamp: Long = System.currentTimeMillis()
)

data class EmotionalPrediction(
    val predictedStressLevel: Int,
    val predictedEnergyLevel: Int,
    val timeHorizon: String,
    val confidence: Float,
    val timestamp: Long
)

data class Recommendation(
    val type: RecommendationType,
    val title: String,
    val description: String,
    val priority: Priority,
    val duration: Int // in seconds
)

enum class EmotionalState {
    CALM,
    FOCUSED,
    RELAXED,
    MODERATE,
    ALERT,
    STRESSED
}

enum class RecommendationType {
    BREATHING,
    MEDITATION,
    HAPTIC_CALM,
    BREAK,
    MINDFULNESS,
    MAINTENANCE,
    EXERCISE,
    SLEEP
}

enum class Priority {
    LOW,
    MEDIUM,
    HIGH
}
