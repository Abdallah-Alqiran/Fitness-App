plugins {
    alias(libs.plugins.android.application)
    //firebase
    id("com.google.gms.google-services")
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    // Serialization
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.fitnessapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.fitnessapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.fitnessapp.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        manifestPlaceholders["healthConnectPermission"] = "android.permission.ACCESS_HEALTH_DATA"
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

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.benchmark.macro)
    implementation(libs.androidx.runtime.android)
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)

    // For Testing
    testImplementation(libs.truth)
    androidTestImplementation(libs.truth)

    // testing
    testImplementation(kotlin("test"))

    // JUnit 5
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)

    // Mockito (Java mocking framework)
    testImplementation(libs.mockito.core)

    // Kotlinx Coroutines testing
    testImplementation(libs.kotlinx.coroutines.test)


    androidTestImplementation(libs.androidx.core)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.rules)

    testImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.androidx.core.testing)

    // testing hilt
    androidTestImplementation(libs.hilt.android.testing)

    //coil
    implementation(libs.coil)
    implementation(libs.coil.compose)

    // lottie
    implementation(libs.lottie)
    implementation(libs.lottie.compose)

    // viewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose.android)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // coroutines
    implementation(libs.kotlinx.coroutines.android)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.gson)
    implementation(libs.converter.gson)

    // dataStore
    implementation(libs.androidx.datastore.preferences)

    // room
    implementation(libs.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // dependency injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)


    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.database.ktx)


    // work
    implementation(libs.androidx.work.runtime.ktx)

    // Health Connect dependency
    implementation(libs.androidx.connect.client)


    // Optional icons (for NavigationBar icons like Security, Sync, etc.)
    implementation(libs.androidx.material)
    implementation(libs.androidx.material.icons.extended)


    // Serialization
    implementation(libs.kotlinx.serialization.json)


    // Graphs
    implementation(libs.androidx.foundation)
    implementation(libs.ui)
}