import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.util.Properties
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeJB)
    alias(libs.plugins.serialization)
}

kotlin {
    androidTarget()
    @OptIn(ExperimentalWasmDsl::class) wasmJs { browser() }
    sourceSets {
        all {
            languageSettings { optIn("org.jetbrains.compose.resources.ExperimentalResourceApi") }
        }
        commonMain.dependencies {
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.components.resources)
            implementation(libs.lifecycle.runtime.compose)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization)
            implementation(libs.uuid)
            implementation(libs.window.sc)
        }
        androidMain.dependencies {
            implementation(libs.core.ktx)
            implementation(libs.activity.compose)
        }
    }
}

compose.resources { packageOfResClass = "io.github.edwinchang24.shengjidisplay.resources" }

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

buildkonfig {
    packageName = "io.github.edwinchang24.shengjidisplay"
    exposeObjectWithName = "VersionConfig"
    defaultConfigs {
        val file = File("./version.properties")
        val properties =
            if (file.exists()) Properties().apply { load(file.inputStream()) } else null
        buildConfigField(STRING, "version", properties?.getProperty("version", "?") ?: "?")
    }
}
