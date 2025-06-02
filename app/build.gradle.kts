plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.kotlin.compose.compiler)
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.uriolus.recipes"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.uriolus.recipies"
        minSdk = 28
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    
    // Configure JUnit 5 test runner
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}


configurations.all {
    resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
    resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.22")
    resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.22")
}


dependencies {
    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    
    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    
    // Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    
    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.androidx.room.external.antlr)
    ksp(libs.room.compiler)
    
    // Arrow
    implementation(libs.arrow.core)
    implementation(libs.arrow.fx.coroutines)
    
    // Hilt (Dagger)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    
    // Navigation
    implementation(libs.androidx.navigation.compose)
    
    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Tink (for encryption with DataStore)
    implementation(libs.google.tink.android)
    
    // Image loading
    implementation(libs.coil.compose)
    
    // Web scraping module
    implementation(project(":web_scraping"))
    
    // Serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.protobuf)
    
    // Testing
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
    
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
}