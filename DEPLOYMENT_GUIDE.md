# Alumni System - Deployment Guide

## 🚀 Making Your Android App Live with Cursor

This guide will walk you through deploying your Alumni System Android application from development to production.

---

## 📋 Prerequisites

### 1. Development Environment Setup

```bash
# Install Android SDK and tools (if not already installed)
# Download Android Studio or ensure you have Android SDK
```

### 2. Required Accounts
- **Google Developer Account** ($25 one-time fee for Play Store)
- **Firebase Account** (free tier available)
- **GitHub Account** (for version control and CI/CD)

---

## 🔧 Step 1: Setup Development Environment in Cursor

### 1.1 Install Required Extensions
```bash
# In Cursor, install these extensions:
# - Android for VS Code
# - Kotlin Language
# - Gradle for Java
```

### 1.2 Verify Android SDK
```bash
# Check if Android SDK is installed
android list targets

# If not installed, download from:
# https://developer.android.com/studio#command-tools
```

### 1.3 Set Environment Variables
```bash
# Add to your ~/.bashrc or ~/.zshrc
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/tools/bin
export PATH=$PATH:$ANDROID_HOME/platform-tools
```

---

## 🔥 Step 2: Configure Firebase

### 2.1 Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Create a project"
3. Name it "Alumni System" or similar
4. Enable Google Analytics (optional)

### 2.2 Add Android App to Firebase
1. Click "Add app" → Android icon
2. Enter your package name: `com.example.allumnisystem`
3. Add app nickname: "Alumni System Android"
4. Download `google-services.json`

### 2.3 Update Firebase Configuration
```bash
# Replace the existing google-services.json with your downloaded file
cp ~/Downloads/google-services.json app/google-services.json
```

### 2.4 Enable Firebase Services
In Firebase Console, enable:
- **Authentication** → Email/Password provider
- **Firestore Database** → Start in test mode
- **Storage** (for profile pictures)

### 2.5 Configure Firestore Security Rules
```javascript
// Firestore Rules
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users can read/write their own profile
    match /profiles/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // All authenticated users can read jobs
    match /jobs/{jobId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && 
        resource.data.createdBy == request.auth.uid;
    }
    
    // Users collection for roles
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Job applications
    match /jobApplications/{applicationId} {
      allow read, write: if request.auth != null;
    }
  }
}
```

---

## 🏗️ Step 3: Build Configuration

### 3.1 Update App-Level build.gradle.kts
```kotlin
// Ensure proper release configuration
android {
    compileSdk 34

    defaultConfig {
        applicationId "com.example.allumnisystem"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    // Add signing config for release
    signingConfigs {
        create("release") {
            // These will be set via environment variables or keystore
        }
    }
}
```

### 3.2 Create Keystore for Signing
```bash
# Generate release keystore
keytool -genkey -v -keystore alumni-system-release.keystore \
        -alias alumni-system -keyalg RSA -keysize 2048 -validity 10000

# Store this keystore file securely - you'll need it for all future releases
```

### 3.3 Configure Signing (create keystore.properties)
```properties
# keystore.properties (DO NOT commit to version control)
storePassword=YOUR_STORE_PASSWORD
keyPassword=YOUR_KEY_PASSWORD
keyAlias=alumni-system
storeFile=../alumni-system-release.keystore
```

---

## 🚀 Step 4: Build and Test

### 4.1 Test Locally
```bash
# Clean and build
./gradlew clean

# Build debug version
./gradlew assembleDebug

# Install on connected device/emulator
./gradlew installDebug

# Run tests
./gradlew test
```

### 4.2 Build Release Version
```bash
# Build signed release APK
./gradlew assembleRelease

# Build App Bundle (recommended for Play Store)
./gradlew bundleRelease
```

---

## 📱 Step 5: Deploy to Google Play Store

### 5.1 Prepare App Store Assets

Create these in a `store-assets/` folder:
- **App Icon**: 512x512px PNG
- **Feature Graphic**: 1024x500px PNG  
- **Screenshots**: Various device sizes
- **App Description**: Clear, compelling description

### 5.2 Google Play Console Setup
1. Go to [Google Play Console](https://play.google.com/console/)
2. Create new application
3. Fill in app details:
   - Title: "Alumni System"
   - Short description: "Connect with your alma mater"
   - Full description: Detailed app description

### 5.3 Upload App Bundle
1. Go to "Release" → "Production"
2. Click "Create new release"
3. Upload your `.aab` file from `app/build/outputs/bundle/release/`
4. Fill in release notes
5. Review and rollout

---

## 🔄 Step 6: Continuous Integration/Deployment

### 6.1 GitHub Actions Setup
```yaml
# .github/workflows/android.yml
name: Android CI/CD

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
      
    - name: Run tests
      run: ./gradlew test
      
    - name: Build debug APK
      run: ./gradlew assembleDebug

  release:
    needs: test
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Build release bundle
      run: ./gradlew bundleRelease
      env:
        SIGNING_KEY_ALIAS: ${{ secrets.SIGNING_KEY_ALIAS }}
        SIGNING_KEY_PASSWORD: ${{ secrets.SIGNING_KEY_PASSWORD }}
        SIGNING_STORE_PASSWORD: ${{ secrets.SIGNING_STORE_PASSWORD }}
```

### 6.2 Environment Secrets
Add these secrets in GitHub repository settings:
- `SIGNING_KEY_ALIAS`
- `SIGNING_KEY_PASSWORD` 
- `SIGNING_STORE_PASSWORD`
- `FIREBASE_CONFIG` (base64 encoded google-services.json)

---

## 🌐 Step 7: Backend Infrastructure (Optional)

### 7.1 Firebase Hosting for Admin Panel
```bash
# Install Firebase CLI
npm install -g firebase-tools

# Initialize hosting
firebase init hosting

# Deploy
firebase deploy
```

### 7.2 Cloud Functions for Backend Logic
```javascript
// functions/index.js
const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

// Auto-delete expired jobs
exports.cleanupExpiredJobs = functions.pubsub.schedule('every 24 hours')
  .onRun(async (context) => {
    const now = Date.now();
    const expiredJobs = await admin.firestore()
      .collection('jobs')
      .where('expirationTime', '<', now)
      .get();
    
    const batch = admin.firestore().batch();
    expiredJobs.docs.forEach(doc => {
      batch.delete(doc.ref);
    });
    
    await batch.commit();
    console.log(`Deleted ${expiredJobs.size} expired jobs`);
  });
```

---

## 📊 Step 8: Monitoring and Analytics

### 8.1 Firebase Analytics
```kotlin
// Add to build.gradle
implementation 'com.google.firebase:firebase-analytics-ktx'

// In your Application class
FirebaseAnalytics.getInstance(this)
```

### 8.2 Crashlytics
```kotlin
// Add crashlytics
implementation 'com.google.firebase:firebase-crashlytics-ktx'

// Log non-fatal errors
FirebaseCrashlytics.getInstance().recordException(exception)
```

---

## ✅ Final Checklist

- [ ] Firebase project configured with Authentication, Firestore, Storage
- [ ] App signed with release keystore
- [ ] Google Play Console account setup
- [ ] App bundle uploaded to Play Store
- [ ] Privacy policy created and linked
- [ ] App store listing optimized
- [ ] CI/CD pipeline configured
- [ ] Analytics and crash reporting enabled
- [ ] Beta testing completed

---

## 🚨 Important Notes

1. **Never commit sensitive files**:
   - `keystore.properties`
   - `*.keystore` files
   - `google-services.json` (use environment variables in CI)

2. **Version management**:
   - Increment `versionCode` for each release
   - Use semantic versioning for `versionName`

3. **Testing**:
   - Test on multiple devices and Android versions
   - Use Firebase Test Lab for automated testing

4. **Compliance**:
   - Create privacy policy (required for Play Store)
   - Ensure GDPR compliance if targeting EU users
   - Follow Google Play policies

---

## 🆘 Troubleshooting

### Common Issues:
1. **Build fails**: Check Android SDK path and Java version
2. **Firebase connection**: Verify google-services.json is correct
3. **Signing issues**: Ensure keystore path and passwords are correct
4. **Play Store rejection**: Review Google Play policies and app content

### Support Resources:
- [Android Developer Documentation](https://developer.android.com)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Google Play Console Help](https://support.google.com/googleplay/android-developer)

---

**Ready to go live!** 🎉

Follow this guide step by step, and your Alumni System will be live on the Google Play Store with proper CI/CD and monitoring in place.