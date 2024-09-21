package com.example.allumnisystem.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val db = FirebaseFirestore.getInstance()

    // Local states to hold the data
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var birthDate by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var city by remember { mutableStateOf(TextFieldValue("")) }
    var country by remember { mutableStateOf(TextFieldValue("")) }
    var degree by remember { mutableStateOf(TextFieldValue("")) }
    var school by remember { mutableStateOf(TextFieldValue("")) }
    var occupation by remember { mutableStateOf(TextFieldValue("")) }
    var skills by remember { mutableStateOf(TextFieldValue("")) }

    // State to track if the profile is being edited
    var isEditing by remember { mutableStateOf(false) }

    // Fetch existing user data
    LaunchedEffect(user) {
        user?.let {
            db.collection("users").document(it.uid).get().addOnSuccessListener { document ->
                name = TextFieldValue(document.getString("name") ?: "")
                birthDate = TextFieldValue(document.getString("birthDate") ?: "")
                email = TextFieldValue(document.getString("email") ?: "")
                city = TextFieldValue(document.getString("city") ?: "")
                country = TextFieldValue(document.getString("country") ?: "")
                degree = TextFieldValue(document.getString("degree") ?: "")
                school = TextFieldValue(document.getString("school") ?: "")
                occupation = TextFieldValue(document.getString("occupation") ?: "")
                skills = TextFieldValue(document.getString("skills") ?: "")
            }
        }
    }

    // UI layout with Material 3 modern styling
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = { isEditing = !isEditing }) {
                        Icon(
                            imageVector = if (isEditing) Icons.Default.AccountCircle else Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
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
                if (isEditing) {
                    EditableProfileFields(
                        name = name,
                        birthDate = birthDate,
                        email = email,
                        city = city,
                        country = country,
                        degree = degree,
                        school = school,
                        occupation = occupation,
                        skills = skills,
                        onNameChange = { name = it },
                        onBirthDateChange = { birthDate = it },
                        onEmailChange = { email = it },
                        onCityChange = { city = it },
                        onCountryChange = { country = it },
                        onDegreeChange = { degree = it },
                        onSchoolChange = { school = it },
                        onOccupationChange = { occupation = it },
                        onSkillsChange = { skills = it }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            user?.let {
                                db.collection("users").document(it.uid)
                                    .update(
                                        mapOf(
                                            "name" to name.text,
                                            "birthDate" to birthDate.text,
                                            "email" to email.text,
                                            "city" to city.text,
                                            "country" to country.text,
                                            "degree" to degree.text,
                                            "school" to school.text,
                                            "occupation" to occupation.text,
                                            "skills" to skills.text
                                        )
                                    ).addOnSuccessListener {
                                        isEditing = false // Exit edit mode after saving
                                    }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Save Changes")
                    }
                } else {
                    ProfileDetailsView(
                        name = name.text,
                        birthDate = birthDate.text,
                        email = email.text,
                        city = city.text,
                        country = country.text,
                        degree = degree.text,
                        school = school.text,
                        occupation = occupation.text,
                        skills = skills.text
                    )
                }
            }
        }
    )
}

@Composable
fun EditableProfileFields(
    name: TextFieldValue,
    birthDate: TextFieldValue,
    email: TextFieldValue,
    city: TextFieldValue,
    country: TextFieldValue,
    degree: TextFieldValue,
    school: TextFieldValue,
    occupation: TextFieldValue,
    skills: TextFieldValue,
    onNameChange: (TextFieldValue) -> Unit,
    onBirthDateChange: (TextFieldValue) -> Unit,
    onEmailChange: (TextFieldValue) -> Unit,
    onCityChange: (TextFieldValue) -> Unit,
    onCountryChange: (TextFieldValue) -> Unit,
    onDegreeChange: (TextFieldValue) -> Unit,
    onSchoolChange: (TextFieldValue) -> Unit,
    onOccupationChange: (TextFieldValue) -> Unit,
    onSkillsChange: (TextFieldValue) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Editable Fields for the user profile
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = birthDate,
            onValueChange = onBirthDateChange,
            label = { Text("Birth Date") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = city,
            onValueChange = onCityChange,
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = country,
            onValueChange = onCountryChange,
            label = { Text("Country") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = degree,
            onValueChange = onDegreeChange,
            label = { Text("Degree") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = school,
            onValueChange = onSchoolChange,
            label = { Text("School") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = occupation,
            onValueChange = onOccupationChange,
            label = { Text("Occupation") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = skills,
            onValueChange = onSkillsChange,
            label = { Text("Skills") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ProfileDetailsView(
    name: String,
    birthDate: String,
    email: String,
    city: String,
    country: String,
    degree: String,
    school: String,
    occupation: String,
    skills: String
) {
    // Show profile details using Cards for better visual hierarchy
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ProfileField(label = "Name", value = name)
        ProfileField(label = "Birth Date", value = birthDate)
        ProfileField(label = "Email", value = email)
        ProfileField(label = "City", value = city)
        ProfileField(label = "Country", value = country)
        ProfileField(label = "Degree", value = degree)
        ProfileField(label = "School", value = school)
        ProfileField(label = "Occupation", value = occupation)
        ProfileField(label = "Skills", value = skills)
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    // Use Card for each profile field to give it a modern, elevated look
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
