import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

val supabaseUrl = localProperties.getProperty("supabase.url") ?: ""
val supabaseKey = localProperties.getProperty("supabase.key") ?: ""
// Google OAuth *Web* client ID used for Single Sign-On (Google) via Supabase
val googleWebClientId = localProperties.getProperty("google.web.client.id") ?: ""

android {
    namespace = "study_hub"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "study_hub"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "SUPABASE_URL",
            "\"$supabaseUrl\""
        )

        buildConfigField(
            "String",
            "SUPABASE_KEY",
            "\"$supabaseKey\""
        )

        buildConfigField(
            "String",
            "GOOGLE_WEB_CLIENT_ID",
            "\"$googleWebClientId\""
        )
    }

    // Release signing (Play Store). Values come from local.properties / CI secrets, never from source.
    signingConfigs {
        create("release") {
            val storePath = localProperties.getProperty("release.store.file")
            if (storePath != null) {
                storeFile = file(storePath)
                storePassword = localProperties.getProperty("release.store.password")
                keyAlias = localProperties.getProperty("release.key.alias")
                keyPassword = localProperties.getProperty("release.key.password")
            }
        }
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
            if (localProperties.getProperty("release.store.file") != null) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

    testImplementation(libs.junit)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

// Supabase
    implementation(platform("io.github.jan-tennert.supabase:bom:3.5.0"))
    implementation("io.github.jan-tennert.supabase:auth-kt")
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.ktor:ktor-client-android:3.0.3")

// Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    // Room (Offline mode) - annotation processing via KSP
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Google Single Sign-On (Credential Manager + Google ID token -> Supabase)
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    // WorkManager (background offline -> online synchronisation)
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // Firebase Messaging (real-time push notifications)
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-messaging-ktx")

}