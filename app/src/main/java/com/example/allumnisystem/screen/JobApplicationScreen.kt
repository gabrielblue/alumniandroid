package com.example.allumnisystem.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.allumnisystem.utils.JobApplicationData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobApplicationScreen(navController: NavController, jobId: String) {
    val auth = FirebaseAuth.getInstance()
    var applicantName by remember { mutableStateOf("") }
    var applicantEmail by remember { mutableStateOf("") }
    var coverLetter by remember { mutableStateOf("") }
    var cvUrl by remember { mutableStateOf("") }  // New field for CV URL
    val coroutineScope = rememberCoroutineScope()
    val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser
    val userID = currentUser?.uid
    val user = FirebaseAuth.getInstance().currentUser

    fun submitApplication() {
        coroutineScope.launch {
            val jobApplication = userID?.let {
                JobApplicationData(
                    jobId = jobId,
                    userId = it,
                    applicantName = applicantName,
                    applicantEmail = applicantEmail,
                    coverLetter = coverLetter,
                    cvUrl = cvUrl  // Include CV URL
                )
            }

            user?.let {
                if (jobApplication != null) {
                    db.collection("jobApplications")
                        .add(jobApplication)
                        .addOnSuccessListener {
                            navController.navigateUp()  // Go back after successful submission
                        }
                        .addOnFailureListener { e ->
                            e.printStackTrace()
                        }
                }
            }
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

                // New CV field
                OutlinedTextField(
                    value = cvUrl,
                    onValueChange = { cvUrl = it },
                    label = { Text("CV URL") },
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
