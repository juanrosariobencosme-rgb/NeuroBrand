package com.neuroband.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.neuroband.app.ui.screens.connection.ConnectionScreen
import com.neuroband.app.ui.screens.dashboard.DashboardScreen
import com.neuroband.app.ui.screens.insights.AIInsightsScreen
import com.neuroband.app.ui.screens.settings.SettingsScreen
import com.neuroband.app.ui.screens.trends.TrendsScreen

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Connection : Screen("connection")
    object Trends : Screen("trends")
    object Insights : Screen("insights")
    object Settings : Screen("settings")
}

@Composable
fun NeuroBandNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Connection.route
    ) {
        composable(Screen.Connection.route) {
            ConnectionScreen(
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Connection.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToTrends = {
                    navController.navigate(Screen.Trends.route)
                },
                onNavigateToInsights = {
                    navController.navigate(Screen.Insights.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        composable(Screen.Trends.route) {
            TrendsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Insights.route) {
            AIInsightsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
