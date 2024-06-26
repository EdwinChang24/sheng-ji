import com.diffplug.gradle.spotless.SpotlessExtension

// Top-level build file where you can add configuration options common to all subprojects/modules.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.buildkonfig) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.composeJB) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.spotless) apply true
}

configure<SpotlessExtension> {
    kotlin {
        target(
            fileTree(".") {
                include("**/*.kt")
                exclude("**/build/**")
            }
        )
        ktfmt().kotlinlangStyle()
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktfmt().kotlinlangStyle()
    }
}

true
