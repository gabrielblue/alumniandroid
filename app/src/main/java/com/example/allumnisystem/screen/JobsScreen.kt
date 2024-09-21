package com.example.allumnisystem.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobManagementScreen(navController: NavController) {
    var jobTitle by remember { mutableStateOf("") }
    var jobDescription by remember { mutableStateOf("") }
    var jobDuration by remember { mutableStateOf("") }
    var jobList by remember { mutableStateOf<List<Job>>(emptyList()) }

    val db = FirebaseFirestore.getInstance()

    // Function to add or edit a job
    fun addOrEditJob(jobId: String? = null) {
        val user = FirebaseAuth.getInstance().currentUser
        val currentTime = System.currentTimeMillis()
        val expirationTime = currentTime + jobDuration.toLong() * 24 * 60 * 60 * 1000

        val jobData = hashMapOf(
            "title" to jobTitle,
            "description" to jobDescription,
            "expirationTime" to expirationTime
        )

        user?.let {
            if (jobId == null) {
                // Add a new job
                db.collection("users").document(user.uid).collection("jobs")
                    .add(jobData)
            } else {
                // Edit existing job
                db.collection("users").document(user.uid).collection("jobs")
                    .document(jobId).update(jobData as Map<String, Any>)
            }
        }
    }

    // Function to delete a job
    fun deleteJob(jobId: String) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            db.collection("users").document(user.uid).collection("jobs")
                .document(jobId).delete()
        }
    }

    // Function to fetch and filter expired jobs
    fun fetchJobs() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            db.collection("users").document(user.uid).collection("jobs")
                .get()
                .addOnSuccessListener { documents ->
                    val currentTime = System.currentTimeMillis()
                    val activeJobs = documents.mapNotNull { doc ->
                        val expirationTime = doc.getLong("expirationTime")
                        if (expirationTime != null && expirationTime > currentTime) {
                            Job(
                                id = doc.id,
                                title = doc.getString("title") ?: "",
                                description = doc.getString("description") ?: "",
                                expirationTime = expirationTime
                            )
                        } else {
                            // Automatically delete expired job
                            deleteJob(doc.id)
                            null
                        }
                    }
                    jobList = activeJobs
                }
        }
    }

    // Fetch jobs on first load
    LaunchedEffect(Unit) {
        fetchJobs()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Job Management", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
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

                // Form to add/edit a job
                OutlinedTextField(
                    value = jobTitle,
                    onValueChange = { jobTitle = it },
                    label = { Text("Job Title", style = MaterialTheme.typography.bodyLarge) },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )

                OutlinedTextField(
                    value = jobDescription,
                    onValueChange = { jobDescription = it },
                    label = { Text("Job Description", style = MaterialTheme.typography.bodyLarge) },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )

                OutlinedTextField(
                    value = jobDuration,
                    onValueChange = { jobDuration = it },
                    label = { Text("Job Duration (days)", style = MaterialTheme.typography.bodyLarge) },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { addOrEditJob() },
                    modifier = Modifier.fillMaxWidth(0.9f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Add/Update Job", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onPrimary)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // List of Jobs
                Text("Job List", style = MaterialTheme.typography.titleLarge)

                jobList.forEach { job ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(8.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Title: ${job.title}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Description: ${job.description}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Row {
                                Button(
                                    onClick = { addOrEditJob(job.id) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary
                                    )
                                ) {
                                    Text("Edit", color = MaterialTheme.colorScheme.onSecondary)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { deleteJob(job.id) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Text("Delete", color = MaterialTheme.colorScheme.onError)
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

// Job data model
data class Job(
    val id: String,
    val title: String,
    val description: String,
    val expirationTime: Long
)
