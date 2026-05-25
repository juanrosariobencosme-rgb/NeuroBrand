package com.neuroband.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.neuroband.app.ui.theme.SmokeGray
import com.neuroband.app.ui.theme.SoftBlue

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun NeuroBandBottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem("dashboard", Icons.Default.Home, "Home"),
        BottomNavItem("trends", Icons.Default.ShowChart, "Trends"),
        BottomNavItem("insights", Icons.Default.Psychology, "IA Insights"),
        BottomNavItem("settings", Icons.Default.Settings, "Settings")
    )
    
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Color.White.copy(alpha = 0.9f),
                RoundedCornerShape(24.dp)
            ),
        containerColor = Color.White.copy(alpha = 0.9f),
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    onClick = { onNavigate(item.route) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (currentRoute == item.route) {
                                SoftBlue
                            } else {
                                SmokeGray
                            }
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (currentRoute == item.route) {
                                SoftBlue
                            } else {
                                SmokeGray
                            }
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIndicatorColor = SoftBlue.copy(alpha = 0.2f),
                        unselectedIndicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}
