package com.example.allumnisystem.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.allumnisystem.screen.AddScreen
import com.example.allumnisystem.screen.AlumniApplicationScreen
import com.example.allumnisystem.screen.DashboardScreen
import com.example.allumnisystem.screen.ForgotPasswordScreen
import com.example.allumnisystem.screen.JobApplicationScreen
import com.example.allumnisystem.screen.JobDetailsScreen
import com.example.allumnisystem.screen.JobPostingScreen
import com.example.allumnisystem.screen.JobsScreen
import com.example.allumnisystem.screen.LoginScreen
import com.example.allumnisystem.screen.ProfileCreationScreen
import com.example.allumnisystem.screen.ProfileScreen
import com.example.allumnisystem.screen.RegisterScreen
import com.example.allumnisystem.screen.ViewJobApplicationsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.DashboardScreen.route
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
            JobsScreen(navController = navController)
        }

        // Job Details screen with a jobId parameter
        composable(
            route = Screens.JobDetailsScreen.route + "/{jobId}",
            arguments = listOf(navArgument("jobId") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId")
            jobId?.let {
                JobDetailsScreen(navController = navController, jobId = it)
            }
        }

        // Job Application screen with a jobId parameter
        composable(
            route = Screens.JobApplicationScreen.route,
            arguments = listOf(navArgument("jobId") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId")
            jobId?.let {
                JobApplicationScreen(navController = navController, jobId = it)
            }
        }

        // View Job Applications screen
        composable(Screens.ViewJobApplicationsScreen.route) {  // Added
            ViewJobApplicationsScreen(navController = navController)
        }

        // Profile screen
        composable(Screens.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }

        // Job Posting screen
        composable(Screens.JobPostingScreen.route) {
            JobPostingScreen(navController = navController)
        }

        // Add Screen
        composable(Screens.AddScreen.route) {
            AddScreen(navController = navController)
        }
        
        // Alumni my Application
        composable(
            route = Screens.AlumniApplicationScreen.route,
            arguments = listOf(navArgument("userId") {type = NavType.StringType })
        ) { backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userId") ?: ""
            
            AlumniApplicationScreen(navController = navController, userID = userID)
        }
    }
}
