package com.neuroband.app.data.supabase.repositories

import com.neuroband.app.data.ble.BiometricData
import com.neuroband.app.data.supabase.SupabaseClient
import io.github.jan-tennert.supabase.postgrest.from
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BiometricRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) {
    
    suspend fun saveBiometricData(
        deviceId: String,
        biometricData: BiometricData
    ): Boolean {
        return try {
            val data = mapOf(
                "device_id" to deviceId,
                "heart_rate" to biometricData.heartRate,
                "respiration_rate" to biometricData.respirationRate,
                "body_temperature" to biometricData.bodyTemperature,
                "energy_level" to biometricData.energyLevel,
                "gsr_value" to biometricData.gsrValue,
                "timestamp" to java.util.Date()
            )
            
            supabaseClient.database
                .from("biometric_data")
                .insert(data)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun getBiometricData(
        deviceId: String,
        limit: Int = 100
    ): List<BiometricRecord> {
        return try {
            supabaseClient.database
                .from("biometric_data")
                .select {
                    filter {
                        eq("device_id", deviceId)
                    }
                    order("timestamp", ascending = false)
                    limit(limit)
                }
                .decodeList<BiometricRecord>()
        } catch (e: Exception) {
            emptyList()
        }
    }
}

data class BiometricRecord(
    val id: Long,
    val deviceId: String,
    val timestamp: String,
    val heartRate: Int,
    val respirationRate: Double,
    val bodyTemperature: Double,
    val energyLevel: Int,
    val gsrValue: Int
)
