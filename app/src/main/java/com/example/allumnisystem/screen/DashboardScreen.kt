package com.example.allumnisystem.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.allumnisystem.nav.Screens
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navbar(navController: NavController) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "App Logo",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        "Alumni System",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                IconButton(onClick = { /* navigation menu click handler */ }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Navigation Menu",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNav(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedItem == 0,
            onClick = {
                selectedItem = 0
                navController.navigate(Screens.HomeScreen.route)
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = selectedItem == 1,
            onClick = {
                selectedItem = 1
                navController.navigate(Screens.ProfileScreen.route)
            }
        )

        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = "Add") },
            label = { Text("Add") },
            selected = selectedItem == 2,
            onClick = {
                selectedItem = 2
                navController.navigate(Screens.AddScreen.route)
            }
        )

        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.Menu, contentDescription = "Jobs") }, // Updated icon
            label = { Text("Jobs") },
            selected = selectedItem == 3,
            onClick = {
                selectedItem = 3
                navController.navigate(Screens.JobsScreen.route)
            }
        )

        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = selectedItem == 4,
            onClick = {
                selectedItem = 4
                navController.navigate(Screens.SettingsScreen.route)
            }
        )
    }
}

@Composable
fun DashboardScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    Scaffold(
        topBar = { Navbar(navController = navController) },
        bottomBar = { BottomNav(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Dashboard",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            currentUser?.let {
                Text(
                    text = "Welcome, ${it.email}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    auth.signOut()
                    navController.navigate(Screens.LoginScreen.route) {
                        popUpTo(Screens.DashboardScreen.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Log Out", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
