import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.util.Properties
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeJB)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
}

kotlin {
    androidTarget()
    @OptIn(ExperimentalWasmDsl::class) wasmJs { browser() }
    sourceSets {
        all {
            languageSettings { optIn("org.jetbrains.compose.resources.ExperimentalResourceApi") }
        }
        commonMain {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            dependencies {
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.components.resources)
                implementation(libs.lifecycle.runtime.compose)
                implementation(libs.lifecycle.viewmodel.compose)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization)
                implementation(libs.uuid)
                implementation(libs.arrow.optics)
            }
        }
        androidMain.dependencies {
            implementation(libs.core.ktx)
            implementation(libs.activity.compose)
            implementation(libs.window)
        }
    }
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions { freeCompilerArgs.add("-Xexpect-actual-classes") }
}

dependencies { kspCommonMainMetadata(libs.arrow.optics.ksp) }

tasks.withType(KotlinCompilationTask::class) {
    val kspTaskName = "kspCommonMainKotlinMetadata"
    if (name != kspTaskName) dependsOn(kspTaskName)
}

compose.resources { packageOfResClass = "resources" }

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
    packageName = "versionconfig"
    exposeObjectWithName = "VersionConfig"
    defaultConfigs {
        val file = File(project.rootDir, "version.properties")
        val properties =
            if (file.exists()) Properties().apply { load(file.inputStream()) } else null
        buildConfigField(STRING, "version", properties?.getProperty("version", "?") ?: "?")
    }
}
