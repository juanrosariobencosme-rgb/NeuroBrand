package com.neuroband.app.data.supabase.repositories

import com.neuroband.app.data.supabase.SupabaseClient
import io.github.jan-tennert.supabase.postgrest.from
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) {
    
    suspend fun getUserById(userId: String): User? {
        return try {
            supabaseClient.database
                .from("users")
                .select {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingle<User>()
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun updateUser(user: User): Boolean {
        return try {
            supabaseClient.database
                .from("users")
                .update(user) {
                    filter {
                        eq("id", user.id)
                    }
                }
            true
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun createUser(user: User): Boolean {
        return try {
            supabaseClient.database
                .from("users")
                .insert(user)
            true
        } catch (e: Exception) {
            false
        }
    }
}

data class User(
    val id: String,
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val themePreference: String = "dark",
    val notificationEnabled: Boolean = true,
    val hapticFeedbackEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val dataSharingEnabled: Boolean = false,
    val analyticsEnabled: Boolean = false
)
