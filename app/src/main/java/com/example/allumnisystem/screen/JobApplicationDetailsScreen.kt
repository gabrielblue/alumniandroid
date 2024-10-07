package com.example.allumnisystem.screen

import android.annotation.SuppressLint
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
    val db = FirebaseFirestore.getInstance()

    // Fetch job application details
    LaunchedEffect(applicationId) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            db.collection("users").document(user.uid).collection("jobApplications")
                .document(applicationId)
                .get()
                .addOnSuccessListener { document ->
                    jobApplication = document.toObject(JobApplicationData::class.java)?.copy(id = document.id)
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Application Details") })
        },
        content = {
            jobApplication?.let { application ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Applicant Name: ${application.applicantName}")
                    Text("Applicant Email: ${application.applicantEmail}")
                    Text("Cover Letter: ${application.coverLetter}")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { navController.popBackStack() }) {
                        Text("Back")
                    }
                }
            } ?: Text("Loading...")
        }
    )
}
