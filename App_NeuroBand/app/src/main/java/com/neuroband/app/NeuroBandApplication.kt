package com.neuroband.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NeuroBandApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
    }
}
