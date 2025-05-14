plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id(libs.plugins.kotlin.compose.get().pluginId)
}

android {
    namespace = "com.uriolus.recipies"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.uriolus.recipies"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx.v290)
    implementation(libs.androidx.activity.compose)
    
    // Compose
    implementation(platform(libs.androidx.compose.bom.v20240900))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    
    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.core.v180)
    implementation(libs.kotlinx.coroutines.android.v180)
    
    // Ktor
    implementation(libs.ktor.client.core.v239)
    implementation(libs.ktor.client.android.v239)
    implementation(libs.ktor.client.content.negotiation.v239)
    implementation(libs.ktor.serialization.kotlinx.json.v239)
    implementation(libs.ktor.client.logging.v239)
    
    // Hilt (Dagger)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    
    // Navigation
    implementation(libs.androidx.navigation.compose.v277)
    
    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose.v290)
    
    // Image loading
    implementation(libs.coil.compose)
    
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom.v20250500))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}