package com.example.allumnisystem.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
//import com.example.systemically.data.JobData
import com.example.allumnisystem.utils.JobData

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailsScreen(navController: NavController, jobId: String) {
    var job by remember { mutableStateOf<JobData?>(null) }
    val db = FirebaseFirestore.getInstance()

    // Fetch job details
    LaunchedEffect(jobId) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            db.collection("users").document(user.uid).collection("jobs")
                .document(jobId)
                .get()
                .addOnSuccessListener { document ->
                    job = document.toObject(JobData::class.java)?.copy(id = document.id)
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Job Details") })
        },
        content = {
            job?.let { jobData ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Title: ${jobData.title}")
                    Text("Description: ${jobData.description}")
                    Text("Salary: ${jobData.salary}")
                    Text("Posted: ${jobData.postedDate}")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { navController.popBackStack() }) {
                        Text("Back")
                    }
                }
            } ?: Text("Loading...")
        }
    )
}
