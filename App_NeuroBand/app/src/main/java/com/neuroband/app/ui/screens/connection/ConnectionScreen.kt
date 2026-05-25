package com.neuroband.app.ui.screens.connection

import android.bluetooth.BluetoothDevice
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.neuroband.app.data.ble.ConnectionState
import com.neuroband.app.data.ble.ScanStatus
import com.neuroband.app.ui.theme.DeepBlack
import com.neuroband.app.ui.theme.SoftBlue
import com.neuroband.app.ui.theme.WarmWhite
import com.neuroband.app.ui.screens.connection.ConnectionViewModel

@Composable
fun ConnectionScreen(
    onNavigateToDashboard: () -> Unit,
    viewModel: ConnectionViewModel = hiltViewModel()
) {
    val scanStatus by viewModel.scanStatus.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()
    
    LaunchedEffect(connectionState) {
        if (connectionState is ConnectionState.Connected) {
            onNavigateToDashboard()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmWhite)
    ) {
        // Header
        ConnectionHeader()
        
        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (scanStatus) {
                is ScanStatus.Idle -> {
                    IdleContent(onStartScan = { viewModel.startScan() })
                }
                is ScanStatus.Scanning -> {
                    ScanningContent(
                        devices = (scanStatus as ScanStatus.Scanning).devices,
                        onDeviceClick = { device -> viewModel.connectToDevice(device) },
                        onStopScan = { viewModel.stopScan() }
                    )
                }
                is ScanStatus.DevicesFound -> {
                    DevicesFoundContent(
                        devices = (scanStatus as ScanStatus.DevicesFound).devices,
                        onDeviceClick = { device -> viewModel.connectToDevice(device) },
                        onRescan = { viewModel.startScan() }
                    )
                }
            }
        }
    }
}

@Composable
fun ConnectionHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Bluetooth,
                contentDescription = "Bluetooth",
                tint = SoftBlue,
                modifier = Modifier.size(24.dp)
            )
            
            Text(
                text = "NeuroBand",
                style = MaterialTheme.typography.headlineSmall,
                color = DeepBlack,
                fontWeight = FontWeight.Bold
            )
            
            Icon(
                imageVector = Icons.Default.BatteryFull,
                contentDescription = "Battery",
                tint = SoftBlue,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun IdleContent(onStartScan: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Device Image with Glow Effect
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(200.dp)
        ) {
            // Glow circles
            val infiniteTransition = rememberInfiniteTransition(label = "glow")
            val scale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "scale"
            )
            
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                SoftBlue.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
            )
            
            // Device placeholder
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.BluetoothSearching,
                    contentDescription = "NeuroBand Device",
                    tint = SoftBlue,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onStartScan,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SoftBlue
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Escanear Dispositivos",
                style = MaterialTheme.typography.titleMedium,
                color = DeepBlack
            )
        }
    }
}

@Composable
fun ScanningContent(
    devices: List<BluetoothDevice>,
    onDeviceClick: (BluetoothDevice) -> Unit,
    onStopScan: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = SoftBlue,
            modifier = Modifier.size(48.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Escaneando dispositivos...",
            style = MaterialTheme.typography.titleLarge,
            color = DeepBlack
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Asegúrate de que tu NeuroBand esté cerca",
            style = MaterialTheme.typography.bodyMedium,
            color = com.neuroband.app.ui.theme.SmokeGray,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onStopScan,
            colors = ButtonDefaults.buttonColors(
                containerColor = com.neuroband.app.ui.theme.SmokeGray
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Cancelar",
                style = MaterialTheme.typography.titleMedium,
                color = DeepBlack
            )
        }
    }
}

@Composable
fun DevicesFoundContent(
    devices: List<BluetoothDevice>,
    onDeviceClick: (BluetoothDevice) -> Unit,
    onRescan: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${devices.size} dispositivo(s) encontrado(s)",
            style = MaterialTheme.typography.titleLarge,
            color = DeepBlack
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        devices.forEach { device ->
            DeviceCard(
                device = device,
                onClick = { onDeviceClick(device) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onRescan,
            colors = ButtonDefaults.buttonColors(
                containerColor = com.neuroband.app.ui.theme.SmokeGray
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Escanear de nuevo",
                style = MaterialTheme.typography.titleMedium,
                color = DeepBlack
            )
        }
    }
}

@Composable
fun DeviceCard(
    device: BluetoothDevice,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = device.name ?: "Dispositivo desconocido",
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepBlack,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = device.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = com.neuroband.app.ui.theme.SmokeGray
                )
            }
            
            Icon(
                imageVector = Icons.Default.Bluetooth,
                contentDescription = "Connect",
                tint = SoftBlue,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
