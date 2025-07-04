#!/bin/bash

# Alumni System - Quick Deploy Script
# This script helps you get started with deploying your Android app

set -e

echo "🚀 Alumni System - Quick Deploy Setup"
echo "======================================"

# Check if we're in the right directory
if [ ! -f "app/build.gradle.kts" ]; then
    echo "❌ Error: Please run this script from the project root directory"
    exit 1
fi

echo "✅ Project directory verified"

# Make gradlew executable
chmod +x gradlew
echo "✅ Made gradlew executable"

# Clean and test build
echo "🧹 Cleaning project..."
./gradlew clean

echo "🧪 Running tests..."
./gradlew test

echo "🔨 Building debug APK..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo "✅ Debug build successful!"
    echo "📱 APK location: app/build/outputs/apk/debug/app-debug.apk"
else
    echo "❌ Build failed. Please check the errors above."
    exit 1
fi

echo ""
echo "🎉 Your app is ready for development!"
echo ""
echo "Next steps to go live:"
echo "1. 🔥 Set up Firebase (see DEPLOYMENT_GUIDE.md)"
echo "2. 🔑 Create signing keystore for release builds"
echo "3. 📱 Test on physical device or emulator"
echo "4. 🏪 Upload to Google Play Store"
echo ""
echo "📖 For detailed instructions, see: DEPLOYMENT_GUIDE.md"

# Check if Android SDK is available
if command -v adb &> /dev/null; then
    echo ""
    echo "📱 Connected Android devices:"
    adb devices -l
    echo ""
    echo "💡 To install on connected device, run:"
    echo "   adb install app/build/outputs/apk/debug/app-debug.apk"
fi