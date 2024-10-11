package com.example.allumnisystem.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.allumnisystem.utils.JobViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobsScreen(navController: NavController, jobViewModel: JobViewModel = viewModel()) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    var isAdmin by remember { mutableStateOf(false) }
    var isFormVisible by remember { mutableStateOf(false) }

    // Use LaunchedEffect to fetch user role
    LaunchedEffect(currentUser) {
        currentUser?.let {
            val userId = it.uid
            val firestore = FirebaseFirestore.getInstance()
            try {
                val documentSnapshot = firestore.collection("users").document(userId).get().await()
                val role = documentSnapshot.getString("role")
                isAdmin = (role == "admin") // Set isAdmin based on Firestore data
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val jobList by jobViewModel.jobList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Job Management", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        bottomBar = {
            BottomNav(navController = navController)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Show "Create New Job" button only for admin
                if (isAdmin) {
                    Button(
                        onClick = { isFormVisible = !isFormVisible },
                        modifier = Modifier.fillMaxWidth(0.9f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            if (isFormVisible) "Cancel Job Creation" else "Create New Job",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                if (isFormVisible) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Job creation form
                    TextField(
                        value = jobViewModel.jobTitle.collectAsState().value,
                        onValueChange = { jobViewModel.jobTitle.value = it },
                        label = { Text("Job Title", style = MaterialTheme.typography.bodyLarge) },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(8.dp),
                        colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = MaterialTheme.colorScheme.primary)
                    )

                    TextField(
                        value = jobViewModel.jobDescription.collectAsState().value,
                        onValueChange = { jobViewModel.jobDescription.value = it },
                        label = { Text("Job Description", style = MaterialTheme.typography.bodyLarge) },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(8.dp),
                        colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = MaterialTheme.colorScheme.primary)
                    )

                    TextField(
                        value = jobViewModel.jobSalary.collectAsState().value,
                        onValueChange = { jobViewModel.jobSalary.value = it },
                        label = { Text("Job Salary", style = MaterialTheme.typography.bodyLarge) },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(8.dp),
                        colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = MaterialTheme.colorScheme.primary)
                    )

                    TextField(
                        value = jobViewModel.jobDuration.collectAsState().value,
                        onValueChange = { jobViewModel.jobDuration.value = it },
                        label = { Text("Job Duration (days)", style = MaterialTheme.typography.bodyLarge) },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(8.dp),
                        colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = MaterialTheme.colorScheme.primary)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { jobViewModel.addOrEditJob() },
                        modifier = Modifier.fillMaxWidth(0.9f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Submit Job", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text("Job List", style = MaterialTheme.typography.titleLarge)

                jobList.forEach { job ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(8.dp)
                            .clickable { navController.navigate("job_details_screen/${job.id}") },
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text("Title: ${job.title}", style = MaterialTheme.typography.bodyLarge)
                            Text("Description: ${job.description}", style = MaterialTheme.typography.bodyMedium)
                            Text("Salary: ${job.salary}", style = MaterialTheme.typography.bodyMedium)
                            Text("Posted: ${job.postedDate}", style = MaterialTheme.typography.bodyMedium)

                            Spacer(modifier = Modifier.height(16.dp))

                            // Show apply button only for alumni
                            if (!isAdmin) {
                                Button(
                                    onClick = { navController.navigate("job_application_screen/${job.id}") },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Apply", color = MaterialTheme.colorScheme.onSecondary)
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
