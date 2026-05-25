package com.neuroband.app.data.ble

import android.bluetooth.BluetoothDevice
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BleRepository @Inject constructor(
    private val bleManager: NeuroBandBleManager,
    private val bleScanner: BleScanner
) {
    
    private val TAG = "BleRepository"
    
    val connectionState: Flow<ConnectionState> = bleManager.connectionState
    val biometricData: Flow<BiometricData?> = bleManager.biometricData
    val batteryLevel: Flow<Int?> = bleManager.batteryLevel
    val isScanning: Flow<Boolean> = bleScanner.isScanning
    val discoveredDevices: Flow<List<BluetoothDevice>> = bleScanner.discoveredDevices
    
    val scanStatus: Flow<ScanStatus> = combine(
        isScanning,
        discoveredDevices
    ) { scanning, devices ->
        when {
            scanning -> ScanStatus.Scanning(devices)
            devices.isEmpty() -> ScanStatus.Idle
            else -> ScanStatus.DevicesFound(devices)
        }
    }
    
    fun isBluetoothEnabled(): Boolean {
        return bleScanner.isBluetoothEnabled()
    }
    
    fun startScan() {
        Log.d(TAG, "Starting BLE scan")
        bleScanner.startScan()
    }
    
    fun stopScan() {
        Log.d(TAG, "Stopping BLE scan")
        bleScanner.stopScan()
    }
    
    fun connectToDevice(device: BluetoothDevice) {
        Log.d(TAG, "Connecting to device: ${device.name}")
        stopScan()
        bleManager.connect(device)
    }
    
    fun disconnect() {
        Log.d(TAG, "Disconnecting from device")
        bleManager.disconnect()
    }
    
    fun sendVibrationCommand(intensity: Int) {
        Log.d(TAG, "Sending vibration command with intensity: $intensity")
        bleManager.sendVibrationCommand(intensity)
    }
    
    fun clearDiscoveredDevices() {
        bleScanner.clearDiscoveredDevices()
    }
}

sealed class ScanStatus {
    object Idle : ScanStatus()
    data class Scanning(val devices: List<BluetoothDevice>) : ScanStatus()
    data class DevicesFound(val devices: List<BluetoothDevice>) : ScanStatus()
}
