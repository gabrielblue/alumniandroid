// Screens.kt
package com.example.allumnisystem.nav

sealed class Screens(val route: String) {
    data object RegisterScreen : Screens(route = "register_screen")
    data object LoginScreen : Screens(route = "login_screen")
    data object ForgotPasswordScreen : Screens(route = "forgot_password_screen")
    data object DashboardScreen : Screens(route = "dashboard_screen")

    // New screens
    data object HomeScreen : Screens(route = "home_screen")
    data object ProfileScreen : Screens(route = "profile_screen")
    data object AddScreen : Screens(route = "add_screen")
    data object JobsScreen : Screens(route = "jobs_screen")
    data object SettingsScreen : Screens(route = "settings_screen")
    data object ProfileCreationScreen : Screens("profile_creation_screen")
}
