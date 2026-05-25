package com.neuroband.app.ui.screens.connection

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neuroband.app.data.ble.BleRepository
import com.neuroband.app.data.ble.ConnectionState
import com.neuroband.app.data.ble.ScanStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val bleRepository: BleRepository
) : ViewModel() {
    
    val scanStatus: StateFlow<ScanStatus> = bleRepository.scanStatus
    val connectionState: StateFlow<ConnectionState> = bleRepository.connectionState
    
    fun startScan() {
        viewModelScope.launch {
            bleRepository.startScan()
        }
    }
    
    fun stopScan() {
        viewModelScope.launch {
            bleRepository.stopScan()
        }
    }
    
    fun connectToDevice(device: BluetoothDevice) {
        viewModelScope.launch {
            bleRepository.connectToDevice(device)
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        bleRepository.stopScan()
    }
}
