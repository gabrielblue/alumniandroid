package com.example.allumnisystem.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.allumnisystem.utils.ProfileData
import com.example.allumnisystem.nav.Screens
import com.example.allumnisystem.utils.uriToBitmap
import com.example.allumnisystem.utils.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCreationScreen(navController: NavController, viewModel: ProfileViewModel = viewModel()) {
    var currentStep by remember { mutableStateOf(1) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Load profile data initially
    LaunchedEffect(Unit) {
        viewModel.loadProfileData()
    }

    val profileData by viewModel.profileData.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile Creation: Step $currentStep") }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (currentStep) {
                    1 -> Step1(
                        profileData = profileData,
                        onFieldUpdate = { updatedProfile ->
                            viewModel.updateProfileField { it.copy(name = updatedProfile.name, birthDate = updatedProfile.birthDate, email = updatedProfile.email) }
                        },
                        onNext = { currentStep++ }
                    )
                    2 -> Step2Location(
                        profileData = profileData,
                        onFieldUpdate = { updatedProfile ->
                            viewModel.updateProfileField { it.copy(city = updatedProfile.city, country = updatedProfile.country) }
                        },
                        onNext = { currentStep++ }, onPrevious = { currentStep-- }
                    )
                    3 -> Step3Education(
                        profileData = profileData,
                        onFieldUpdate = { updatedProfile ->
                            viewModel.updateProfileField { it.copy(degree = updatedProfile.degree, school = updatedProfile.school) }
                        },
                        onNext = { currentStep++ }, onPrevious = { currentStep-- }
                    )
                    4 -> Step4ProfilePicture(
                        imageUri = imageUri,
                        onImageSelected = { imageUri = it },
                        onNext = { currentStep++ },
                        onSkip = { currentStep++ },
                        onPrevious = { currentStep-- }
                    )
                    5 -> Step5VerifyEmail(onNext = { currentStep++ }, onPrevious = { currentStep-- })
                    6 -> Step6WorkspaceSkills(
                        profileData = profileData,
                        onFieldUpdate = { updatedProfile ->
                            viewModel.updateProfileField { it.copy(occupation = updatedProfile.occupation, skills = updatedProfile.skills) }
                        },
                        onNext = {
                            viewModel.saveProfileData(profileData)
                            currentStep++
                        },
                        onPrevious = { currentStep-- }
                    )
                    7 -> Step7FinalMessage(navController = navController, onPrevious = { currentStep-- })
                }
            }
        }
    )
}

@Composable
fun Step1(profileData: ProfileData, onFieldUpdate: (ProfileData) -> Unit, onNext: () -> Unit) {
    var name by remember { mutableStateOf(TextFieldValue(profileData.name)) }
    var birthDate by remember { mutableStateOf(TextFieldValue(profileData.birthDate)) }
    var email by remember { mutableStateOf(TextFieldValue(profileData.email)) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Step 1: Personal Information", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                onFieldUpdate(profileData.copy(name = it.text))
            },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = birthDate,
            onValueChange = {
                birthDate = it
                onFieldUpdate(profileData.copy(birthDate = it.text))
            },
            label = { Text("Birth Date") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                onFieldUpdate(profileData.copy(email = it.text))
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNext) {
            Text("Next")
        }
    }
}

@Composable
fun Step2Location(profileData: ProfileData, onFieldUpdate: (ProfileData) -> Unit, onNext: () -> Unit, onPrevious: () -> Unit) {
    var city by remember { mutableStateOf(TextFieldValue(profileData.city)) }
    var country by remember { mutableStateOf(TextFieldValue(profileData.country)) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Step 2: Location", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = city,
            onValueChange = {
                city = it
                onFieldUpdate(profileData.copy(city = it.text))
            },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = country,
            onValueChange = {
                country = it
                onFieldUpdate(profileData.copy(country = it.text))
            },
            label = { Text("Country") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = onPrevious, modifier = Modifier.weight(1f)) {
                Text("Previous")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onNext, modifier = Modifier.weight(1f)) {
                Text("Next")
            }
        }
    }
}

@Composable
fun Step3Education(profileData: ProfileData, onFieldUpdate: (ProfileData) -> Unit, onNext: () -> Unit, onPrevious: () -> Unit) {
    var degree by remember { mutableStateOf(TextFieldValue(profileData.degree)) }
    var school by remember { mutableStateOf(TextFieldValue(profileData.school)) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Step 3: Education", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = degree,
            onValueChange = {
                degree = it
                onFieldUpdate(profileData.copy(degree = it.text))
            },
            label = { Text("Degree") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = school,
            onValueChange = {
                school = it
                onFieldUpdate(profileData.copy(school = it.text))
            },
            label = { Text("School") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = onPrevious, modifier = Modifier.weight(1f)) {
                Text("Previous")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onNext, modifier = Modifier.weight(1f)) {
                Text("Next")
            }
        }
    }
}

@Composable
fun Step4ProfilePicture(
    imageUri: Uri?,
    onImageSelected: (Uri?) -> Unit,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    onPrevious: () -> Unit
) {
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Step 4: Profile Picture", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            onImageSelected(uri)
        }

        imageUri?.let {
            val bitmap = uriToBitmap(context, it)
            if (bitmap != null) {
                Image(bitmap = bitmap, contentDescription = null, modifier = Modifier.size(128.dp))
            }
        } ?: Text("No image selected")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Upload Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = onPrevious, modifier = Modifier.weight(1f)) {
                Text("Previous")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onSkip, modifier = Modifier.weight(1f)) {
                Text("Skip")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onNext, modifier = Modifier.weight(1f)) {
                Text("Next")
            }
        }
    }
}

@Composable
fun Step5VerifyEmail(onNext: () -> Unit, onPrevious: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Step 5: Verify Email", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Please verify your email address before proceeding.")
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = onPrevious, modifier = Modifier.weight(1f)) {
                Text("Previous")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onNext, modifier = Modifier.weight(1f)) {
                Text("Next")
            }
        }
    }
}

@Composable
fun Step6WorkspaceSkills(profileData: ProfileData, onFieldUpdate: (ProfileData) -> Unit, onNext: () -> Unit, onPrevious: () -> Unit) {
    var occupation by remember { mutableStateOf(TextFieldValue(profileData.occupation)) }
    var skills by remember { mutableStateOf(TextFieldValue(profileData.skills)) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Step 6: Workspace & Skills", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = occupation,
            onValueChange = {
                occupation = it
                onFieldUpdate(profileData.copy(occupation = it.text))
            },
            label = { Text("Occupation") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = skills,
            onValueChange = {
                skills = it
                onFieldUpdate(profileData.copy(skills = it.text))
            },
            label = { Text("Skills") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = onPrevious, modifier = Modifier.weight(1f)) {
                Text("Previous")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onNext, modifier = Modifier.weight(1f)) {
                Text("Next")
            }
        }
    }
}

@Composable
fun Step7FinalMessage(navController: NavController, onPrevious: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Step 7: Profile Completed", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Congratulations! Your profile is now complete.")
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate(Screens.DashboardScreen.route) {
                popUpTo(Screens.ProfileCreationScreen.route) { inclusive = true }
            }
        }) {
            Text("Go to Dashboard")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onPrevious) {
            Text("Previous")
        }
    }
}
