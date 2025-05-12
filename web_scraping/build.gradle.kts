plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.uriolus.web_scraping"
    compileSdk = 35

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    testOptions {
        unitTests.isReturnDefaultValues = true // Recommended for Android unit tests
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.jsoup) // Added Jsoup dependency

    // JUnit 5 dependencies for local unit tests
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params) // For parameterized tests, if needed
    testRuntimeOnly(libs.junit.jupiter.engine)

}

// Configure the test task to use JUnit Platform for JUnit 5
android.testOptions {
    unitTests.all {
        it.useJUnitPlatform()
    }
}