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
import com.example.allumnisystem.utils.JobApplicationData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobApplicationScreen(navController: NavController, jobId: String) {
    var applicantName by remember { mutableStateOf("") }
    var applicantEmail by remember { mutableStateOf("") }
    var coverLetter by remember { mutableStateOf("") }

    val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser

    fun submitApplication() {
        val jobApplication = JobApplicationData(
            jobId = jobId,
            applicantName = applicantName,
            applicantEmail = applicantEmail,
            coverLetter = coverLetter
        )

        user?.let {
            db.collection("users").document(user.uid).collection("jobApplications")
                .add(jobApplication)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Apply for Job") })
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
                    value = applicantName,
                    onValueChange = { applicantName = it },
                    label = { Text("Applicant Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = applicantEmail,
                    onValueChange = { applicantEmail = it },
                    label = { Text("Applicant Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = coverLetter,
                    onValueChange = { coverLetter = it },
                    label = { Text("Cover Letter") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { submitApplication() }) {
                    Text("Submit Application")
                }
            }
        }
    )
}
