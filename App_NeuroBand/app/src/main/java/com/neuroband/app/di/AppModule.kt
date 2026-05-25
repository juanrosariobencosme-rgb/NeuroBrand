package com.neuroband.app.di

import android.content.Context
import com.neuroband.app.data.ble.BleManager
import com.neuroband.app.data.ble.BleRepository
import com.neuroband.app.data.ble.BleScanner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideBleScanner(
        @ApplicationContext context: Context
    ): BleScanner {
        return BleScanner(context)
    }
    
    @Provides
    @Singleton
    fun provideBleManager(
        @ApplicationContext context: Context
    ): BleManager {
        return BleManager(context)
    }
    
    @Provides
    @Singleton
    fun provideBleRepository(
        bleManager: BleManager,
        bleScanner: BleScanner
    ): BleRepository {
        return BleRepository(bleManager, bleScanner)
    }
}
