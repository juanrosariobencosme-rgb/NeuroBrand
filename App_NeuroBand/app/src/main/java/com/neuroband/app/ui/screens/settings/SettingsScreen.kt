package com.neuroband.app.ui.screens.settings

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.neuroband.app.ui.theme.DeepBlack
import com.neuroband.app.ui.theme.SoftBlue
import com.neuroband.app.ui.theme.WarmWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Configuración",
                        style = MaterialTheme.typography.titleLarge,
                        color = DeepBlack,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = DeepBlack
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WarmWhite
                )
            )
        },
        containerColor = WarmWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Device Settings
            SettingsSection(title = "Dispositivo") {
                var vibrationEnabled by remember { mutableStateOf(true) }
                var hapticFeedback by remember { mutableStateOf(true) }
                
                SettingItem(
                    icon = Icons.Default.Vibration,
                    title = "Intensidad de Vibración",
                    description = "Ajustar la fuerza de las vibraciones",
                    showSwitch = true,
                    switchChecked = vibrationEnabled,
                    onSwitchChange = { vibrationEnabled = it }
                )
                
                SettingItem(
                    icon = Icons.Default.Sensors,
                    title = "Feedback Háptico",
                    description = "Respuestas táctiles de la pulsera",
                    showSwitch = true,
                    switchChecked = hapticFeedback,
                    onSwitchChange = { hapticFeedback = it }
                )
            }
            
            // App Settings
            SettingsSection(title = "Aplicación") {
                var notifications by remember { mutableStateOf(true) }
                var darkTheme by remember { mutableStateOf(true) }
                
                SettingItem(
                    icon = Icons.Default.Notifications,
                    title = "Notificaciones",
                    description = "Alertas y recordatorios inteligentes",
                    showSwitch = true,
                    switchChecked = notifications,
                    onSwitchChange = { notifications = it }
                )
                
                SettingItem(
                    icon = Icons.Default.Palette,
                    title = "Tema Oscuro",
                    description = "Modo de visualización de la app",
                    showSwitch = true,
                    switchChecked = darkTheme,
                    onSwitchChange = { darkTheme = it }
                )
            }
            
            // Privacy Settings
            SettingsSection(title = "Privacidad") {
                var dataSharing by remember { mutableStateOf(false) }
                var analytics by remember { mutableStateOf(false) }
                
                SettingItem(
                    icon = Icons.Default.Lock,
                    title = "Compartir Datos",
                    description = "Permitir uso de datos anónimos",
                    showSwitch = true,
                    switchChecked = dataSharing,
                    onSwitchChange = { dataSharing = it }
                )
                
                SettingItem(
                    icon = Icons.Default.Lock,
                    title = "Analíticas",
                    description = "Mejorar la app con datos de uso",
                    showSwitch = true,
                    switchChecked = analytics,
                    onSwitchChange = { analytics = it }
                )
            }
            
            // Privacy Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SoftBlue.copy(alpha = 0.15f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Tu Privacidad",
                        style = MaterialTheme.typography.titleMedium,
                        color = DeepBlack,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = "Tus emociones son privadas. Tus datos nunca serán vendidos. Tienes control total sobre tu información.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DeepBlack.copy(alpha = 0.8f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = DeepBlack.copy(alpha = 0.6f),
            fontWeight = FontWeight.SemiBold
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun SettingItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    showSwitch: Boolean = false,
    switchChecked: Boolean = false,
    onSwitchChange: (Boolean) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        SoftBlue.copy(alpha = 0.15f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = SoftBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DeepBlack,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = DeepBlack.copy(alpha = 0.6f)
                )
            }
        }
        
        if (showSwitch) {
            Switch(
                checked = switchChecked,
                onCheckedChange = onSwitchChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = SoftBlue,
                    checkedTrackColor = SoftBlue.copy(alpha = 0.5f),
                    uncheckedThumbColor = DeepBlack.copy(alpha = 0.5f),
                    uncheckedTrackColor = DeepBlack.copy(alpha = 0.2f)
                )
            )
        }
    }
}
