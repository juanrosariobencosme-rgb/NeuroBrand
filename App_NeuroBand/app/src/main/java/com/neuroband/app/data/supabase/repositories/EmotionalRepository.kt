package com.neuroband.app.data.supabase.repositories

import com.neuroband.app.data.supabase.SupabaseClient
import io.github.jan-tennert.supabase.postgrest.from
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmotionalRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) {
    
    suspend fun saveEmotionalRecord(
        userId: String,
        emotionalState: String,
        stressLevel: Int,
        energyLevel: Int,
        moodScore: Int,
        context: String? = null,
        notes: String? = null
    ): Boolean {
        return try {
            val record = mapOf(
                "user_id" to userId,
                "emotional_state" to emotionalState,
                "stress_level" to stressLevel,
                "energy_level" to energyLevel,
                "mood_score" to moodScore,
                "context" to context,
                "notes" to notes,
                "timestamp" to java.util.Date()
            )
            
            supabaseClient.database
                .from("emotional_records")
                .insert(record)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun getEmotionalRecords(
        userId: String,
        limit: Int = 100
    ): List<EmotionalRecord> {
        return try {
            supabaseClient.database
                .from("emotional_records")
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                    order("timestamp", ascending = false)
                    limit(limit)
                }
                .decodeList<EmotionalRecord>()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getEmotionalTrends(
        userId: String,
        days: Int = 7
    ): List<EmotionalTrend> {
        return try {
            // This would typically use a more complex query with date filtering
            supabaseClient.database
                .from("emotional_trends")
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                    order("date", ascending = false)
                    limit(days)
                }
                .decodeList<EmotionalTrend>()
        } catch (e: Exception) {
            emptyList()
        }
    }
}

data class EmotionalRecord(
    val id: Long,
    val userId: String,
    val timestamp: String,
    val emotionalState: String,
    val stressLevel: Int,
    val energyLevel: Int,
    val moodScore: Int,
    val context: String? = null,
    val notes: String? = null
)

data class EmotionalTrend(
    val userId: String,
    val date: String,
    val avgStress: Double,
    val avgEnergy: Double,
    val avgMood: Double,
    val recordCount: Int
)
