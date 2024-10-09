package com.example.allumnisystem.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class JobViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // StateFlow to hold the list of jobs
    private val _jobList = MutableStateFlow<List<JobData>>(emptyList())
    val jobList: StateFlow<List<JobData>> = _jobList

    // StateFlow for job input fields
    var jobTitle = MutableStateFlow("")
    var jobDescription = MutableStateFlow("")
    var jobSalary = MutableStateFlow("")
    var jobDuration = MutableStateFlow("")

    init {
        fetchJobs()
    }

    // Fetch jobs from Firestore
    private fun fetchJobs() {
        viewModelScope.launch {
            db.collection("jobs")
                .get()
                .addOnSuccessListener { documents ->
                    val currentTime = System.currentTimeMillis()
                    val jobs = documents.mapNotNull { doc ->
                        val expirationTime = doc.getLong("expirationTime")
                        if (expirationTime != null && expirationTime > currentTime) {
                            JobData(
                                id = doc.id,
                                title = doc.getString("title"),
                                description = doc.getString("description"),
                                salary = doc.getString("salary"),
                                postedDate = doc.getString("postedDate"),
                                expirationTime = expirationTime
                            )
                        } else {
                            deleteJob(doc.id)
                            null
                        }
                    }
                    _jobList.value = jobs
                }
        }
    }

    // Add or edit a job
    fun addOrEditJob(jobId: String? = null) {
        val currentTime = System.currentTimeMillis()
        val expirationTime = currentTime + jobDuration.value.toLong() * 24 * 60 * 60 * 1000
        val postedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(currentTime))

        val jobData = hashMapOf(
            "title" to jobTitle.value,
            "description" to jobDescription.value,
            "salary" to jobSalary.value,
            "postedDate" to postedDate,
            "expirationTime" to expirationTime
        )

        if (jobId == null) {
            db.collection("jobs").add(jobData)
        } else {
            db.collection("jobs").document(jobId).update(jobData as Map<String, Any>)
        }
    }

    // Delete a job from Firestore
    private fun deleteJob(jobId: String) {
        db.collection("jobs").document(jobId).delete()
    }
}
