package com.example.allumnisystem.nav

sealed class Screens(val route: String) {
    // Auth related screens
    data object RegisterScreen : Screens(route = "register_screen")
    data object LoginScreen : Screens(route = "login_screen")
    data object ForgotPasswordScreen : Screens(route = "forgot_password_screen")

    // General screens
    data object DashboardScreen : Screens(route = "dashboard_screen")
    data object ProfileCreationScreen : Screens(route = "profile_creation_screen")
    data object ProfileScreen : Screens(route = "profile_screen")

    // Job related screens
    data object JobsScreen : Screens(route = "jobs_screen")
    data object JobPostingScreen : Screens(route = "job_posting_screen")
    data object JobDetailsScreen : Screens(route = "job_details_screen")
    data object JobApplicationScreen : Screens(route = "job_application_screen/{jobId}")
    data object JobApplicationDetailsScreen : Screens(route = "job_application_details_screen")

    // View Job Applications screen
    data object ViewJobApplicationsScreen : Screens(route = "view_job_applications_screen")
    data object AlumniApplicationScreen : Screens(route = "alumni_application/{userId}")

    // Add screen
    data object AddScreen : Screens(route = "add_screen")

    // Other screens
    data object SettingsScreen : Screens(route = "settings_screen")
}
