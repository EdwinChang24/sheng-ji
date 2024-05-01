@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
}

android {
    namespace = "io.github.edwinchang24.shengjidisplay"
    compileSdk = 34
    defaultConfig {
        applicationId = "io.github.edwinchang24.shengjidisplay"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.2.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
            signingConfig = signingConfigs.findByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions { jvmTarget = "1.8" }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.13" }
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.core.splashscreen)
    implementation(libs.kotlinx.serialization)
    implementation(libs.compose.destinations)
    ksp(libs.compose.destinations.ksp)
    implementation(libs.kotlinx.datetime)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    testImplementation(libs.junit)
    testImplementation(libs.hilt.testing)
    kspTest(libs.hilt.compiler)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(libs.hilt.testing)
    kspAndroidTest(libs.hilt.compiler)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}
