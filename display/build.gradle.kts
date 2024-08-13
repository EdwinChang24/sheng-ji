import java.util.Properties
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
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
                                add(project.rootDir.path + "/display/")
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
                                add(project.rootDir.path + "/display/")
                            }
                    }
            }
        }
        binaries.executable()
    }
    jvm("desktop")
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
            implementation(compose.preview)
            implementation(compose.uiTooling)
            implementation(libs.window)
            implementation(libs.core.splashscreen)
            implementation(libs.qrcode.kotlin)
        }
        val webMain by creating { dependsOn(commonMain) }
        val wasmJsMain by getting {
            dependsOn(webMain)
            dependencies { implementation(npm("uqr", libs.versions.uqr.get())) }
        }
        val jsMain by getting {
            dependsOn(webMain)
            dependencies { implementation(npm("uqr", libs.versions.uqr.get())) }
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.qrcode.kotlin)
            }
        }
    }
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions { freeCompilerArgs.add("-Xexpect-actual-classes") }
}

dependencies { kspCommonMainMetadata(libs.arrow.optics.ksp) }

fun getAppVersion(): String {
    val versionFile = File(project.rootDir, "version.properties")
    val versionProperties =
        if (versionFile.exists()) Properties().apply { load(versionFile.inputStream()) } else null
    return versionProperties?.getProperty("version", "?") ?: "?"
}

abstract class GenerateVersionNumberTask : DefaultTask() {
    @OutputFile
    val output = project.layout.buildDirectory.file("generated/version/commonMain/Version.kt")

    @TaskAction
    fun generate() {
        val versionFile = File(project.rootDir, "version.properties")
        val versionProperties =
            if (versionFile.exists()) Properties().apply { load(versionFile.inputStream()) }
            else null
        val appVersion = versionProperties?.getProperty("version", "?") ?: "?"
        output.get().asFile.writeText("const val appVersion = \"$appVersion\"")
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
    namespace = "dev.edwinchang.shengjidisplay"
    defaultConfig {
        applicationId = "dev.edwinchang.shengjidisplay"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = getAppVersion()
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
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
}

val buildWebApp by
    tasks.creating(Copy::class) {
        val wasm = "wasmJsBrowserDistribution"
        val js = "jsBrowserDistribution"
        dependsOn(wasm, js)
        from(tasks.named(wasm).get().outputs.files)
        from(tasks.named(js).get().outputs.files)
        into(layout.buildDirectory.file("webApp"))
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

compose.desktop {
    application {
        mainClass = "MainKt"
        buildTypes.release.proguard {
            obfuscate.set(true)
            optimize.set(true)
            joinOutputJars.set(true)
        }
        nativeDistributions {
            packageName = "Sheng Ji Display"
            packageVersion = getAppVersion()
            licenseFile.set(rootProject.file("LICENSE"))
            targetFormats(TargetFormat.Exe, TargetFormat.Deb)
        }
    }
}
