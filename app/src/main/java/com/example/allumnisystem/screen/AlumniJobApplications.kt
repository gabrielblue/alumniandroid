package com.example.allumnisystem.screen

import android.annotation.SuppressLint
import android.util.Log
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
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlumniApplicationScreen(navController: NavController, userID: String) {
    var jobApplication by remember { mutableStateOf<JobApplicationData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val db = FirebaseFirestore.getInstance()

    // Fetch the current user's application details from Firestore
    LaunchedEffect(Unit) {
        Log.d("AlumniApplicationScreen", "UserID: $userID")
        // Use the provided userID
        if (userID.isNotEmpty()) {
            // Query job applications by userId
            db.collection("jobApplications")
                .whereEqualTo("userId", userID)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val document = documents.documents[0] // Assuming the user has only one application
                        jobApplication = document.toObject(JobApplicationData::class.java)?.copy(id = document.id)
                    } else {
                        errorMessage = "No application found"
                    }
                    isLoading = false
                }
                .addOnFailureListener { e ->
                    errorMessage = "Error fetching application: ${e.message}"
                    isLoading = false
                }
        } else {
            errorMessage = "User ID is empty"
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Application Status") })
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when {
                    isLoading -> {
                        Text("Loading...")
                    }
                    errorMessage != null -> {
                        Text("Error: $errorMessage")
                    }
                    jobApplication != null -> {
                        Text("Applicant Name: ${jobApplication?.applicantName}")
                        Text("Applicant Email: ${jobApplication?.applicantEmail}")
                        Text("Cover Letter: ${jobApplication?.coverLetter}")
                        Text("CV URL: ${jobApplication?.cvUrl}")

                        Spacer(modifier = Modifier.height(16.dp))

                        // Display the application status (Pending, Approved, or Rejected)
                        Text("Application Status: ${jobApplication?.status}")

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

