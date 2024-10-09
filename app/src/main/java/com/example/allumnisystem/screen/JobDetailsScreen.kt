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
import com.example.allumnisystem.utils.JobData
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailsScreen(navController: NavController, jobId: String) {
    var job by remember { mutableStateOf<JobData?>(null) }
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(jobId) {
        db.collection("jobs").document(jobId)
            .get()
            .addOnSuccessListener { document ->
                job = document.toObject(JobData::class.java)?.copy(id = document.id)
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
