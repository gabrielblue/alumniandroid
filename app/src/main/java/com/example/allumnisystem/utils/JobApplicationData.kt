package com.example.allumnisystem.utils

data class JobApplicationData(
    val id: String? = null,  // Firestore document ID
    val jobId: String = "",
    val applicantName: String = "",
    val applicantEmail: String = "",
    val coverLetter: String = "",
    val cvUrl: String = "",  // CV URL
    val status: String = "Pending"  // New field to track approval/rejection status
)
