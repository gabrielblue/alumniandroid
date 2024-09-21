package com.example.allumnisystem.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.allumnisystem.screen.LoginScreen
import com.example.allumnisystem.screen.RegisterScreen
import com.example.allumnisystem.screen.ForgotPasswordScreen
import com.example.allumnisystem.screen.DashboardScreen
import com.example.allumnisystem.screen.ProfileCreationScreen
import com.example.allumnisystem.screen.JobManagementScreen
import com.example.allumnisystem.screen.ProfileScreen  // Import ProfileScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screens.LoginScreen.route
    ) {
        // Login screen
        composable(
            route = Screens.LoginScreen.route
        ) {
            LoginScreen(navController = navController)
        }

        // Register screen
        composable(
            route = Screens.RegisterScreen.route
        ) {
            RegisterScreen(navController = navController)
        }

        // Forgot Password screen
        composable(
            route = Screens.ForgotPasswordScreen.route
        ) {
            ForgotPasswordScreen(navController = navController)
        }

        // Dashboard screen
        composable(
            route = Screens.DashboardScreen.route
        ) {
            DashboardScreen(navController = navController)
        }

        // Profile Creation screen
        composable(
            route = Screens.ProfileCreationScreen.route
        ) {
            ProfileCreationScreen(navController = navController)
        }

        // Job Management screen
        composable(
            route = Screens.JobsScreen.route
        ) {
            JobManagementScreen(navController = navController)
        }

        // Profile screen
        composable(
            route = Screens.ProfileScreen.route
        ) {
            ProfileScreen(navController = navController)  // Use ProfileScreen here
        }
    }
}
