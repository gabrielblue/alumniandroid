package com.example.allumnisystem.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Profile data state
    private val _profileData = MutableStateFlow(ProfileData())
    val profileData: StateFlow<ProfileData> = _profileData

    // Function to load profile data from Firestore
    fun loadProfileData() {
        val user = auth.currentUser
        user?.let {
            db.collection("profiles").document(it.uid).get().addOnSuccessListener { document ->
                val profile = document.toObject(ProfileData::class.java)
                if (profile != null) {
                    // Assign the userID to the profile data for consistency
                    _profileData.value = profile.copy(userID = it.uid)
                }
            }
        }
    }

    // Function to save profile data to Firestore
    fun saveProfileData(profileData: ProfileData) {
        val user = auth.currentUser
        user?.let {
            val profileWithUserId = profileData.copy(userID = it.uid)  // Ensure userID is included
            db.collection("profiles").document(it.uid).set(profileWithUserId)
        }
    }

    // Function to update individual profile fields
    fun updateProfileField(field: (ProfileData) -> ProfileData) {
        viewModelScope.launch {
            _profileData.value = field(_profileData.value)
        }
    }
}
