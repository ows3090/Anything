import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.ows.gemini.anything"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ows.gemini.anything"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "GEMINI_KEY",
            gradleLocalProperties(rootDir, providers).getProperty("GEMINI_KEY"),
        )

        buildConfigField(
            "String",
            "DALLE_KEY",
            gradleLocalProperties(rootDir, providers).getProperty("DALLE_KEY"),
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.datastore)
    implementation(libs.datastore.rxjava)
    implementation(libs.viewModel.ktx)
    implementation(libs.generativeai)
    implementation(libs.glide)
    implementation(libs.circleindicator)
    implementation(libs.splash.screen)
    implementation(libs.rxkotlin)
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.timber)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
