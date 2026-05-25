package com.neuroband.app.data.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.ble.data.Data
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NeuroBandBleManager @Inject constructor(
    private val context: Context
) : BleManager(context) {
    
    private val TAG = "NeuroBandBleManager"
    
    // BLE Service and Characteristic UUIDs (example values, should be updated with actual device specs)
    companion object {
        val SERVICE_UUID: java.util.UUID = java.util.UUID.fromString("0000180A-0000-1000-8000-00805F9B34FB")
        val HEART_RATE_CHARACTERISTIC_UUID: java.util.UUID = java.util.UUID.fromString("00002A37-0000-1000-8000-00805F9B34FB")
        val BATTERY_LEVEL_CHARACTERISTIC_UUID: java.util.UUID = java.util.UUID.fromString("00002A19-0000-1000-8000-00805F9B34FB")
        val STRESS_LEVEL_CHARACTERISTIC_UUID: java.util.UUID = java.util.UUID.fromString("00002A38-0000-1000-8000-00805F9B34FB")
        val RESPIRATION_RATE_CHARACTERISTIC_UUID: java.util.UUID = java.util.UUID.fromString("00002A34-0000-1000-8000-00805F9B34FB")
        val BODY_TEMPERATURE_CHARACTERISTIC_UUID: java.util.UUID = java.util.UUID.fromString("00002A1C-0000-1000-8000-00805F9B34FB")
        val GSR_CHARACTERISTIC_UUID: java.util.UUID = java.util.UUID.fromString("00002A35-0000-1000-8000-00805F9B34FB")
        val CONTROL_POINT_CHARACTERISTIC_UUID: java.util.UUID = java.util.UUID.fromString("00002A43-0000-1000-8000-00805F9B34FB")
        
        val CCCD_UUID: java.util.UUID = java.util.UUID.fromString("00002902-0000-1000-8000-00805F9B34FB")
    }
    
    // Connection state
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()
    
    // Biometric data
    private val _biometricData = MutableStateFlow<BiometricData?>(null)
    val biometricData: StateFlow<BiometricData?> = _biometricData.asStateFlow()
    
    // Battery level
    private val _batteryLevel = MutableStateFlow<Int?>(null)
    val batteryLevel: StateFlow<Int?> = _batteryLevel.asStateFlow()
    
    // GATT callbacks
    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    Log.d(TAG, "Connected to GATT server")
                    _connectionState.value = ConnectionState.Connected
                    gatt?.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.d(TAG, "Disconnected from GATT server")
                    _connectionState.value = ConnectionState.Disconnected
                }
                BluetoothProfile.STATE_CONNECTING -> {
                    Log.d(TAG, "Connecting to GATT server")
                    _connectionState.value = ConnectionState.Connecting
                }
            }
        }
        
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "Services discovered")
                enableNotifications(gatt)
            } else {
                Log.e(TAG, "Service discovery failed with status: $status")
            }
        }
        
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            characteristic?.value?.let { data ->
                when (characteristic.uuid) {
                    HEART_RATE_CHARACTERISTIC_UUID -> {
                        val heartRate = parseHeartRate(data)
                        updateBiometricData { it.copy(heartRate = heartRate) }
                    }
                    BATTERY_LEVEL_CHARACTERISTIC_UUID -> {
                        val battery = data[0].toInt() and 0xFF
                        _batteryLevel.value = battery
                    }
                    STRESS_LEVEL_CHARACTERISTIC_UUID -> {
                        val stressLevel = data[0].toInt() and 0xFF
                        updateBiometricData { it.copy(stressLevel = stressLevel) }
                    }
                    RESPIRATION_RATE_CHARACTERISTIC_UUID -> {
                        val respirationRate = (data[0].toInt() and 0xFF).toFloat()
                        updateBiometricData { it.copy(respirationRate = respirationRate) }
                    }
                    BODY_TEMPERATURE_CHARACTERISTIC_UUID -> {
                        val temperature = parseTemperature(data)
                        updateBiometricData { it.copy(bodyTemperature = temperature) }
                    }
                    GSR_CHARACTERISTIC_UUID -> {
                        val gsr = data[0].toInt() and 0xFF
                        updateBiometricData { it.copy(gsrValue = gsr) }
                    }
                }
            }
        }
        
        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                characteristic?.value?.let { data ->
                    when (characteristic.uuid) {
                        BATTERY_LEVEL_CHARACTERISTIC_UUID -> {
                            val battery = data[0].toInt() and 0xFF
                            _batteryLevel.value = battery
                        }
                    }
                }
            }
        }
    }
    
    private fun parseHeartRate(data: ByteArray): Int {
        return if (data.size >= 2 && (data[0].toInt() and 0x01) == 0) {
            // 8-bit heart rate
            data[1].toInt() and 0xFF
        } else if (data.size >= 3) {
            // 16-bit heart rate
            (data[1].toInt() and 0xFF) or ((data[2].toInt() and 0xFF) shl 8)
        } else {
            0
        }
    }
    
    private fun parseTemperature(data: ByteArray): Float {
        return if (data.size >= 2) {
            val tempRaw = (data[0].toInt() and 0xFF) or ((data[1].toInt() and 0xFF) shl 8)
            tempRaw / 100.0f
        } else {
            0.0f
        }
    }
    
    private fun updateBiometricData(update: (BiometricData) -> BiometricData) {
        _biometricData.value = update(_biometricData.value ?: BiometricData())
    }
    
    @SuppressLint("MissingPermission")
    private fun enableNotifications(gatt: BluetoothGatt?) {
        val service = gatt?.getService(SERVICE_UUID) ?: return
        
        // Enable notifications for all characteristics
        listOf(
            HEART_RATE_CHARACTERISTIC_UUID,
            STRESS_LEVEL_CHARACTERISTIC_UUID,
            RESPIRATION_RATE_CHARACTERISTIC_UUID,
            BODY_TEMPERATURE_CHARACTERISTIC_UUID,
            GSR_CHARACTERISTIC_UUID
        ).forEach { uuid ->
            val characteristic = service.getCharacteristic(uuid)
            characteristic?.let {
                gatt.setCharacteristicNotification(it, true)
                val descriptor = it.getDescriptor(CCCD_UUID)
                descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                gatt.writeDescriptor(descriptor)
            }
        }
        
        // Read battery level
        val batteryCharacteristic = service.getCharacteristic(BATTERY_LEVEL_CHARACTERISTIC_UUID)
        batteryCharacteristic?.let {
            gatt.readCharacteristic(it)
        }
    }
    
    @SuppressLint("MissingPermission")
    fun connect(device: BluetoothDevice) {
        _connectionState.value = ConnectionState.Connecting
        connect(device).enqueue()
    }
    
    fun disconnect() {
        disconnect().enqueue()
        _connectionState.value = ConnectionState.Disconnected
    }
    
    @SuppressLint("MissingPermission")
    fun sendVibrationCommand(intensity: Int) {
        val service = bluetoothGatt?.getService(SERVICE_UUID) ?: return
        val characteristic = service.getCharacteristic(CONTROL_POINT_CHARACTERISTIC_UUID) ?: return
        
        val command = byteArrayOf(0x01, intensity.toByte())
        characteristic.value = command
        bluetoothGatt?.writeCharacteristic(characteristic)
    }
    
    override fun getGattCallback(): BluetoothGattCallback {
        return gattCallback
    }
    
    override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
        val service = gatt.getService(SERVICE_UUID)
        return service != null
    }
    
    override fun initialize() {
        // Additional initialization if needed
    }
    
    override fun onServicesInvalidated() {
        _connectionState.value = ConnectionState.Disconnected
    }
}

sealed class ConnectionState {
    object Disconnected : ConnectionState()
    object Connecting : ConnectionState()
    object Connected : ConnectionState()
    data class Error(val message: String) : ConnectionState()
}

data class BiometricData(
    val heartRate: Int = 0,
    val stressLevel: Int = 0,
    val respirationRate: Float = 0f,
    val bodyTemperature: Float = 0f,
    val gsrValue: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
