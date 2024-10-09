package com.example.allumnisystem.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.allumnisystem.utils.JobApplicationData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobApplicationDetailsScreen(navController: NavController, applicationId: String) {
    var jobApplication by remember { mutableStateOf<JobApplicationData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val db = FirebaseFirestore.getInstance()

    // Fetch job application details from Firestore
    LaunchedEffect(applicationId) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            db.collection("jobApplications") // Assuming the collection is global
                .document(applicationId)
                .get()
                .addOnSuccessListener { document ->
                    jobApplication = document.toObject(JobApplicationData::class.java)?.copy(id = document.id)
                    isLoading = false
                }
                .addOnFailureListener { e ->
                    errorMessage = e.message
                    isLoading = false
                }
        } else {
            errorMessage = "User not authenticated"
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Application Details") })
        },
        content = {
            if (isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Loading...")
                }
            } else if (errorMessage != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Error: $errorMessage")
                }
            } else {
                jobApplication?.let { application ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Applicant Name: ${application.applicantName}")
                        Text("Applicant Email: ${application.applicantEmail}")
                        Text("Cover Letter: ${application.coverLetter}")
                        Text("CV URL: ${application.cvUrl}")  // Show the CV URL

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = { navController.popBackStack() }) {
                            Text("Back")
                        }
                    }
                }
            }
        }
    )
}
