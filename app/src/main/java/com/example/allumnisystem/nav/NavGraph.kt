package com.example.allumnisystem.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.allumnisystem.screen.DashboardScreen
import com.example.allumnisystem.screen.ForgotPasswordScreen
import com.example.allumnisystem.screen.JobApplicationDetailsScreen
import com.example.allumnisystem.screen.JobApplicationScreen
import com.example.allumnisystem.screen.JobDetailsScreen
import com.example.allumnisystem.screen.JobManagementScreen
import com.example.allumnisystem.screen.JobPostingScreen
import com.example.allumnisystem.screen.LoginScreen
import com.example.allumnisystem.screen.ProfileCreationScreen
import com.example.allumnisystem.screen.ProfileScreen
import com.example.allumnisystem.screen.RegisterScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.LoginScreen.route // Start at LoginScreen
    ) {
        // Login screen
        composable(Screens.LoginScreen.route) {
            LoginScreen(navController = navController)
        }

        // Register screen
        composable(Screens.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }

        // Forgot Password screen
        composable(Screens.ForgotPasswordScreen.route) {
            ForgotPasswordScreen(navController = navController)
        }

        // Dashboard screen
        composable(Screens.DashboardScreen.route) {
            DashboardScreen(navController = navController)
        }

        // Profile Creation screen
        composable(Screens.ProfileCreationScreen.route) {
            ProfileCreationScreen(navController = navController)
        }

        // Job Management screen
        composable(Screens.JobsScreen.route) {
            JobManagementScreen(navController = navController)
        }

        // Profile screen
        composable(Screens.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }

        // Job Posting screen
        composable(Screens.JobPostingScreen.route) {
            JobPostingScreen(navController = navController)
        }

        // Job Details screen with a jobId parameter
        composable(Screens.JobDetailsScreen.route + "/{jobId}") { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: return@composable
            JobDetailsScreen(navController = navController, jobId = jobId)
        }

        // Job Application screen with a jobId parameter
        composable(Screens.JobApplicationScreen.route + "/{jobId}") { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: return@composable
            JobApplicationScreen(navController = navController, jobId = jobId)
        }

        // Job Application Details screen with an applicationId parameter
        composable(Screens.JobApplicationDetailsScreen.route + "/{applicationId}") { backStackEntry ->
            val applicationId = backStackEntry.arguments?.getString("applicationId") ?: return@composable
            JobApplicationDetailsScreen(navController = navController, applicationId = applicationId)
        }
    }
}
