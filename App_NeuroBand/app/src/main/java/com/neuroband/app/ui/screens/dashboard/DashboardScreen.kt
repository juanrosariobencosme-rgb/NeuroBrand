package com.neuroband.app.ui.screens.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.neuroband.app.data.ble.BiometricData
import com.neuroband.app.ui.components.NeuroBandBottomNavigationBar
import com.neuroband.app.ui.theme.DeepBlack
import com.neuroband.app.ui.theme.EnergyHigh
import com.neuroband.app.ui.theme.EnergyLow
import com.neuroband.app.ui.theme.EnergyMedium
import com.neuroband.app.ui.theme.MintGreen
import com.neuroband.app.ui.theme.SoftBlue
import com.neuroband.app.ui.theme.StressHigh
import com.neuroband.app.ui.theme.StressLow
import com.neuroband.app.ui.theme.StressMedium
import com.neuroband.app.ui.theme.WarmWhite

@Composable
fun DashboardScreen(
    onNavigateToTrends: () -> Unit,
    onNavigateToInsights: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val biometricData by viewModel.biometricData.collectAsState()
    val batteryLevel by viewModel.batteryLevel.collectAsState()
    val emotionalState by viewModel.emotionalState.collectAsState()
    var currentRoute by remember { mutableStateOf("dashboard") }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmWhite)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            DashboardHeader(batteryLevel = batteryLevel)
            
            // Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                item {
                    EmotionalStateCard(emotionalState = emotionalState)
                }
                
                item {
                    BiometricCards(biometricData = biometricData)
                }
                
                item {
                    RealTimeMonitoringCard(biometricData = biometricData)
                }
                
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
        
        // Bottom Navigation
        NeuroBandBottomNavigationBar(
            currentRoute = currentRoute,
            onNavigate = { route ->
                currentRoute = route
                when (route) {
                    "trends" -> onNavigateToTrends()
                    "insights" -> onNavigateToInsights()
                    "settings" -> onNavigateToSettings()
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun DashboardHeader(batteryLevel: Int?) {
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
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.BatteryFull,
                    contentDescription = "Battery",
                    tint = SoftBlue,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "${batteryLevel ?: "--"}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = DeepBlack
                )
            }
        }
    }
}

@Composable
fun EmotionalStateCard(emotionalState: EmotionalState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = emotionalState.color.copy(alpha = 0.15f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(emotionalState.color.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "Emotional State",
                            tint = emotionalState.color,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Column {
                        Text(
                            text = emotionalState.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = DeepBlack,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = emotionalState.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = DeepBlack.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BiometricCards(biometricData: BiometricData?) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BiometricCard(
                icon = Icons.Default.Favorite,
                title = "Frecuencia Cardíaca",
                value = "${biometricData?.heartRate ?: 0}",
                unit = "bpm",
                color = SoftBlue,
                modifier = Modifier.weight(1f)
            )
            BiometricCard(
                icon = Icons.Default.Waves,
                title = "Respiración",
                value = String.format("%.1f", biometricData?.respirationRate ?: 0f),
                unit = "rpm",
                color = MintGreen,
                modifier = Modifier.weight(1f)
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BiometricCard(
                icon = Icons.Default.Psychology,
                title = "Nivel de Estrés",
                value = "${biometricData?.stressLevel ?: 0}",
                unit = "%",
                color = getStressColor(biometricData?.stressLevel ?: 0),
                modifier = Modifier.weight(1f)
            )
            BiometricCard(
                icon = Icons.Default.BatteryFull,
                title = "Nivel de Energía",
                value = "${biometricData?.energyLevel ?: 0}",
                unit = "%",
                color = getEnergyColor(biometricData?.energyLevel ?: 0),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun BiometricCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    unit: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = DeepBlack.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$value $unit",
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepBlack,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RealTimeMonitoringCard(biometricData: BiometricData?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Monitoreo en Tiempo Real",
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepBlack,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Animated heart rate visualization
                AnimatedContent(
                    targetState = biometricData?.heartRate ?: 0,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith 
                        fadeOut(animationSpec = tween(300))
                    },
                    label = "heartRate"
                ) { heartRate ->
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val pulseScale by animateFloatAsState(
                                targetValue = if (heartRate > 0) 1.2f else 1f,
                                animationSpec = tween(500),
                                label = "pulse"
                            )
                            
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Heart Rate",
                                tint = SoftBlue,
                                modifier = Modifier.size(32.dp)
                            )
                            
                            Text(
                                text = "$heartRate BPM",
                                style = MaterialTheme.typography.headlineMedium,
                                color = DeepBlack,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Stress level indicator
                        val stressLevel = biometricData?.stressLevel ?: 0
                        val stressColor = getStressColor(stressLevel)
                        
                        Text(
                            text = "Nivel de Estrés",
                            style = MaterialTheme.typography.bodyMedium,
                            color = DeepBlack.copy(alpha = 0.7f)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        LinearProgressIndicator(
                            progress = { stressLevel / 100f },
                            modifier = Modifier.fillMaxWidth(),
                            color = stressColor,
                            trackColor = stressColor.copy(alpha = 0.2f)
                        )
                    }
                }
            }
        }
    }
}

fun getStressColor(level: Int): Color {
    return when {
        level <= 33 -> StressLow
        level <= 66 -> StressMedium
        else -> StressHigh
    }
}

fun getEnergyColor(level: Int): Color {
    return when {
        level <= 33 -> EnergyLow
        level <= 66 -> EnergyMedium
        else -> EnergyHigh
    }
}

data class EmotionalState(
    val title: String,
    val description: String,
    val color: Color
)
