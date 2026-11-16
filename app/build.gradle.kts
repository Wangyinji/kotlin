plugins {
    id("com.android.application")
    id("com.google.devtools.ksp")
}

android {
    namespace = "edu.isep.localnest"
    compileSdk = 37

    defaultConfig {
        applicationId = "edu.isep.localnest.up"
        minSdk = 23
        targetSdk = 37
        versionCode = 1
        versionName = "1.0-UP"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.room:room-runtime:2.8.5")
    ksp("androidx.room:room-compiler:2.8.5")
    androidTestImplementation("androidx.test:runner:1.7.0")
    androidTestImplementation("androidx.test:rules:1.7.0")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
}

ksp { arg("room.schemaLocation", "$projectDir/schemas") }
