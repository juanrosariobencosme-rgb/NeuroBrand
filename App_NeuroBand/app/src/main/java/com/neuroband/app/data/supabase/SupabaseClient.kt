package com.neuroband.app.data.supabase

import io.github.jan-tennert.supabase.SupabaseClient
import io.github.jan-tennert.supabase.createSupabaseClient
import io.github.jan-tennert.supabase.gotrue.GoTrue
import io.github.jan-tennert.supabase.postgrest.Postgrest
import io.github.jan-tennert.supabase.realtime.Realtime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseClient @Inject constructor() {
    
    // TODO: Replace with your actual Supabase credentials
    private val supabaseUrl = "https://your-project.supabase.co"
    private val supabaseKey = "your-anon-key"
    
    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = supabaseUrl,
        supabaseKey = supabaseKey
    ) {
        install(GoTrue)
        install(Postgrest)
        install(Realtime)
    }
    
    val auth = client.auth
    val database = client.postgrest
    val realtime = client.realtime
}
