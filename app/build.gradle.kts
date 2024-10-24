import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.starkindustries.radientdermat"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.starkindustries.radientdermat"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("string","API_KEY","")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        android {
            buildTypes {
                debug {
                    buildConfigField("String", "API_KEY", "\"AIzaSyBZ0hfk9jCKq6_gV7NJhmfbZIJs9lUDEno\"")
                }
                release {
                    isMinifyEnabled = true
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                    buildConfigField("String", "API_KEY", "\"AIzaSyBZ0hfk9jCKq6_gV7NJhmfbZIJs9lUDEno\"")
                }
            }
        }


    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        dataBinding=true
    }
}

dependencies {
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.google.android.material:compose-theme-adapter:1.2.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}