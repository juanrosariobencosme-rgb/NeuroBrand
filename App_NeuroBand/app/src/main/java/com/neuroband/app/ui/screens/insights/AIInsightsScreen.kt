package com.neuroband.app.ui.screens.insights

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.neuroband.app.ui.theme.DeepBlack
import com.neuroband.app.ui.theme.MintGreen
import com.neuroband.app.ui.theme.SoftBlue
import com.neuroband.app.ui.theme.StressHigh
import com.neuroband.app.ui.theme.WarmWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIInsightsScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "IA Insights",
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
            // AI Status Card
            AIStatusCard()
            
            // Biometric Alert Card
            BiometricAlertCard()
            
            // Emotional Projection Card
            EmotionalProjectionCard()
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AIStatusCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = SoftBlue.copy(alpha = 0.15f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Animated AI icon
                val infiniteTransition = rememberInfiniteTransition(label = "ai")
                val scale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2000, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "scale"
                )
                
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .scale(scale)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    SoftBlue.copy(alpha = 0.4f),
                                    Color.Transparent
                                )
                            ),
                            RoundedCornerShape(32.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "AI",
                        tint = SoftBlue,
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                Column {
                    Text(
                        text = "Síntesis Neuronal Activa",
                        style = MaterialTheme.typography.titleMedium,
                        color = DeepBlack,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Monitoreo biométrico en tiempo real",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DeepBlack.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
fun BiometricAlertCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Alerta de Biometría",
                style = MaterialTheme.typography.titleMedium,
                color = DeepBlack,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Detectamos un pico de estrés sostenido en los últimos 15 minutos basado en la variabilidad de su frecuencia cardíaca.",
                style = MaterialTheme.typography.bodyMedium,
                color = DeepBlack.copy(alpha = 0.8f)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Sugerencia: 5 min de respiración coherente para restablecer el equilibrio.",
                style = MaterialTheme.typography.bodySmall,
                color = DeepBlack.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { /* TODO: Start meditation */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SoftBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "INICIAR MEDITACIÓN",
                        style = MaterialTheme.typography.labelMedium,
                        color = DeepBlack
                    )
                }
                
                Button(
                    onClick = { /* TODO: Activate haptic calm */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MintGreen
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "ACTIVAR HAPTIC CALM",
                        style = MaterialTheme.typography.labelMedium,
                        color = DeepBlack
                    )
                }
            }
        }
    }
}

@Composable
fun EmotionalProjectionCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Proyección Emocional",
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepBlack,
                    fontWeight = FontWeight.SemiBold
                )
                
                Text(
                    text = "(Próximas 2h)",
                    style = MaterialTheme.typography.bodySmall,
                    color = DeepBlack.copy(alpha = 0.6f)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Modelo IA v3.2",
                style = MaterialTheme.typography.labelSmall,
                color = SoftBlue
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Chart placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(
                        SoftBlue.copy(alpha = 0.1f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Gráfico de Proyección",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DeepBlack.copy(alpha = 0.5f)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ProjectionPoint(label = "Ahora", value = "Medio")
                        ProjectionPoint(label = "+30m", value = "Alto")
                        ProjectionPoint(label = "+1h", value = "Alto")
                        ProjectionPoint(label = "+1.5h", value = "Medio")
                        ProjectionPoint(label = "+2h", value = "Bajo")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(StressHigh, RoundedCornerShape(4.dp))
                )
                Text(
                    text = "Pico Proyectado",
                    style = MaterialTheme.typography.bodySmall,
                    color = DeepBlack.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun ProjectionPoint(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val color = when (value) {
            "Alto" -> StressHigh
            "Medio" -> SoftBlue
            "Bajo" -> MintGreen
            else -> SoftBlue
        }
        
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, RoundedCornerShape(6.dp))
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = DeepBlack.copy(alpha = 0.6f)
        )
    }
}
