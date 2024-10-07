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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.allumnisystem.utils.JobData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobManagementScreen(navController: NavController) {
    var jobList by remember { mutableStateOf<List<JobData>>(emptyList()) }

    val db = FirebaseFirestore.getInstance()

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
                            JobData(
                                id = doc.id,
                                title = doc.getString("title") ?: "",
                                description = doc.getString("description") ?: "",
                                salary = doc.getString("salary") ?: "",
                                postedDate = doc.getString("postedDate") ?: "",
                                expirationTime = expirationTime
                            )
                        } else {
                            null
                        }
                    }
                    jobList = activeJobs
                }
        }
    }

    LaunchedEffect(Unit) {
        fetchJobs()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Job Management") })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Text("Job List", style = MaterialTheme.typography.titleLarge)

                jobList.forEach { job ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(8.dp)
                            .clickable { navController.navigate("job_details_screen/${job.id}") },
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            Text("Title: ${job.title}")
                            Text("Description: ${job.description}")
                            Text("Salary: ${job.salary}")
                            Text("Posted: ${job.postedDate}")

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { navController.navigate("job_application_screen/${job.id}") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Apply")
                            }
                        }
                    }
                }
            }
        }
    )
}
