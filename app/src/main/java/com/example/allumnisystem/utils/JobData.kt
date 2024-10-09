package com.example.allumnisystem.utils

// JobData model for job posts
data class JobData(
    val id: String = "",  // Document ID
    val title: String? = null,  // Nullable for Firebase compatibility
    val description: String? = null,
    val salary: String? = null,
    val postedDate: String? = null,
    val expirationTime: Long = 0L  // Expiration time, non-nullable
)