plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeJB)
}

kotlin {
    androidTarget()
    sourceSets {
        all {
            languageSettings { optIn("org.jetbrains.compose.resources.ExperimentalResourceApi") }
        }
        commonMain.dependencies {
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.components.resources)
            implementation(libs.kotlinx.datetime)
        }
    }
}

compose.resources {
    packageOfResClass = "io.github.edwinchang24.shengjidisplay.resources"
}

android {
    compileSdk = 34
    namespace = "io.github.edwinchang24.shengjidisplay.shared"
    defaultConfig { minSdk = 24 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin { jvmToolchain(17) }
}
