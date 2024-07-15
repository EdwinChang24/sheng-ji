import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeJB)
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    androidTarget()
    sourceSets {
        val androidMain by getting { dependencies { implementation(project(":shared")) } }
    }
}

android {
    namespace = "io.github.edwinchang24.shengjidisplay"
    compileSdk = 34
    defaultConfig {
        applicationId = "io.github.edwinchang24.shengjidisplay"
        minSdk = 24
        targetSdk = 34
        versionCode = 1

        val file = File("./version.properties")
        val properties =
            if (file.exists()) Properties().apply { load(file.inputStream()) } else null
        versionName = properties?.getProperty("version", "?") ?: "?"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin { jvmToolchain(17) }
    buildFeatures { buildConfig = true }
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.activity.compose)
    implementation(compose.ui)
    implementation(compose.preview)
    implementation(compose.material3)
    implementation(libs.core.splashscreen)
    implementation(libs.kotlinx.serialization)
    implementation(libs.arrow.optics)
    debugImplementation(compose.uiTooling)
}
