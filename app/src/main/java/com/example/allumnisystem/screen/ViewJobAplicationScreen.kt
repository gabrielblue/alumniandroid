package com.example.allumnisystem.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.allumnisystem.utils.JobApplicationData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewJobApplicationsScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var jobApplications by remember { mutableStateOf<List<JobApplicationData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Fetch job applications
    LaunchedEffect(Unit) {
        db.collection("jobApplications")
            .get()
            .addOnSuccessListener { documents ->
                val applications = documents.map { document ->
                    document.toObject(JobApplicationData::class.java).copy(id = document.id)
                }
                jobApplications = applications
                isLoading = false
            }
            .addOnFailureListener { exception ->
                errorMessage = exception.message
                isLoading = false
            }
    }

    // Approve or Reject an application
    fun handleApplicationAction(applicationId: String, status: String) {
        coroutineScope.launch {
            db.collection("jobApplications")
                .document(applicationId)
                .update("status", status)
                .addOnSuccessListener {
                    // Handle success (e.g., navigate or show a message)
                    jobApplications = jobApplications.filter { it.id != applicationId }  // Remove from the list
                }
                .addOnFailureListener { exception ->
                    errorMessage = exception.message
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("View Job Applications") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        content = { paddingValues ->
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else if (errorMessage != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: $errorMessage", color = Color.Red)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    items(jobApplications) { application ->
                        JobApplicationCard(
                            application = application,
                            onApprove = { handleApplicationAction(application.id!!, "Approved") },
                            onReject = { handleApplicationAction(application.id!!, "Rejected") }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobApplicationCard(
    application: JobApplicationData,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Applicant Name: ${application.applicantName}", style = MaterialTheme.typography.bodyLarge)
            Text("Email: ${application.applicantEmail}", style = MaterialTheme.typography.bodyMedium)
            Text("Cover Letter: ${application.coverLetter}", style = MaterialTheme.typography.bodySmall)
            Text("CV URL: ${application.cvUrl}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onApprove,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Approve", color = MaterialTheme.colorScheme.onPrimary)
                }
                Button(
                    onClick = onReject,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Reject", color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        }
    }
}
