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
import com.example.allumnisystem.utils.JobData
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobPostingScreen(navController: NavController) {
    var jobTitle by remember { mutableStateOf("") }
    var jobDescription by remember { mutableStateOf("") }
    var jobSalary by remember { mutableStateOf("") }
    var jobDuration by remember { mutableStateOf("") }

    val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser

    // Function to post a job
    fun postJob() {
        val currentTime = System.currentTimeMillis()
        val expirationTime = currentTime + jobDuration.toLong() * 24 * 60 * 60 * 1000
        val postedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(currentTime))

        val jobData = JobData(
            title = jobTitle,
            description = jobDescription,
            salary = jobSalary,
            postedDate = postedDate,
            expirationTime = expirationTime
        )

        user?.let {
            db.collection("users").document(user.uid).collection("jobs")
                .add(jobData)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Post a Job") })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                OutlinedTextField(
                    value = jobTitle,
                    onValueChange = { jobTitle = it },
                    label = { Text("Job Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = jobDescription,
                    onValueChange = { jobDescription = it },
                    label = { Text("Job Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = jobSalary,
                    onValueChange = { jobSalary = it },
                    label = { Text("Job Salary") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = jobDuration,
                    onValueChange = { jobDuration = it },
                    label = { Text("Job Duration (days)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { postJob() }) {
                    Text("Submit")
                }
            }
        }
    )
}
