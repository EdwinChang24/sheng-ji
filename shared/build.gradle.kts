import java.util.Properties
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeJB)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
}

kotlin {
    androidTarget()
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "shengjidisplay-wasm"
        browser {
            commonWebpackConfig {
                outputFileName = "shengjidisplay-wasm.js"
                devServer =
                    (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                        static =
                            (static ?: mutableListOf()).apply {
                                add(project.rootDir.path + "/shared/")
                            }
                    }
            }
        }
        binaries.executable()
    }
    js {
        moduleName = "shengjidisplay-js"
        browser {
            commonWebpackConfig {
                outputFileName = "shengjidisplay-js.js"
                devServer =
                    (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                        static =
                            (static ?: mutableListOf()).apply {
                                add(project.rootDir.path + "/shared/")
                            }
                    }
            }
        }
        binaries.executable()
    }
    sourceSets {
        all {
            languageSettings { optIn("org.jetbrains.compose.resources.ExperimentalResourceApi") }
        }
        val commonMain by getting {
            kotlin.srcDirs(
                "build/generated/ksp/metadata/commonMain/kotlin",
                "build/generated/version/commonMain"
            )
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
                implementation(libs.reorderable)
            }
        }
        androidMain.dependencies {
            implementation(libs.core.ktx)
            implementation(libs.activity.compose)
            implementation(libs.window)
        }
        val webMain by creating { dependsOn(commonMain) }
        val wasmJsMain by getting { dependsOn(webMain) }
        val jsMain by getting { dependsOn(webMain) }
    }
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions { freeCompilerArgs.add("-Xexpect-actual-classes") }
}

dependencies { kspCommonMainMetadata(libs.arrow.optics.ksp) }

abstract class GenerateVersionNumberTask : DefaultTask() {
    @OutputFile
    val output = project.layout.buildDirectory.file("generated/version/commonMain/Version.kt")

    @TaskAction
    fun generate() {
        val file = File(project.rootDir, "version.properties")
        val properties =
            if (file.exists()) Properties().apply { load(file.inputStream()) } else null
        val version = properties?.getProperty("version", "?") ?: "?"
        output.get().asFile.writeText("val appVersion = \"$version\"")
    }
}

val generateVersionNumber =
    tasks.register<GenerateVersionNumberTask>("generateVersionNumber") { group = "custom" }

tasks.withType(KotlinCompilationTask::class) {
    val kspTaskName = "kspCommonMainKotlinMetadata"
    if (name != kspTaskName) dependsOn(kspTaskName)
    dependsOn(generateVersionNumber)
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

val buildWebApp by tasks.creating(Copy::class) {
    val wasm = "wasmJsBrowserDistribution"
    val js = "jsBrowserDistribution"
    dependsOn(wasm, js)
    from(tasks.named(wasm).get().outputs.files)
    from(tasks.named(js).get().outputs.files)
    into(layout.buildDirectory.file("webApp"))
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
