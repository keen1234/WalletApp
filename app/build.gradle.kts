buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.google.devtools.ksp:symbol-processing-gradle-plugin:2.0.21-1.0.25")
    }
}

plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

// Apply KSP via buildscript classpath (declared in root build.gradle.kts or here)
apply(plugin = "com.google.devtools.ksp")


android {
    namespace = "com.example.walletapp"
    compileSdk = 36


    defaultConfig {
        applicationId = "com.example.walletapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "0.1"
    }


    buildFeatures {
        compose = true
    }



    kotlinOptions {
        jvmTarget = "17"
    }


    // Set Java toolchain to 17 for all Java compilation
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}


// Set JVM toolchain for Kotlin
kotlin {
    jvmToolchain(17)
}


dependencies {
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
    implementation("androidx.activity:activity-compose:1.11.0")


// Compose
    implementation("androidx.compose.ui:ui:1.9.2")
    implementation("androidx.compose.material:material:1.9.2")
    implementation("androidx.compose.material3:material3:1.4.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.9.2")
    implementation(libs.firebase.crashlytics)
    debugImplementation("androidx.compose.ui:ui-tooling:1.9.2")
    implementation("androidx.navigation:navigation-compose:2.9.5")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")


// Navigation
    implementation("androidx.navigation:navigation-compose:2.9.5")


// Room
    implementation("androidx.room:room-runtime:2.8.1")
    implementation("androidx.room:room-ktx:2.8.1")
    add("ksp", "androidx.room:room-compiler:2.8.1")


// Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")


// ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}


// Configure KSP for Room (configure extension by type to avoid 'ksp { }' accessor)
extensions.configure<com.google.devtools.ksp.gradle.KspExtension> {
    arg("room.schemaLocation", file("schemas").also { it.mkdirs() }.absolutePath)
    arg("room.incremental", "true")
}
