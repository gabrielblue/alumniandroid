package com.example.allumnisystem.utils

data class JobData(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val salary: String = "",
    val postedDate: String = "",
    val expirationTime: Long = 0L
)

data class JobApplicationData(
    val id: String = "",
    val jobId: String = "",
    val applicantName: String = "",
    val applicantEmail: String = "",
    val coverLetter: String = ""
)
