import com.diffplug.gradle.spotless.SpotlessExtension

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.composeJB) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ksp) apply false
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

val mergeWeb: Task by
    tasks.creating {
        group = "custom"
        dependsOn(":display:buildWebApp")
        doLast {
            delete { delete("build/web") }
            copy {
                from(project.file("website"))
                exclude("node_modules", "dist")
                into(layout.buildDirectory.dir("web"))
            }
            copy {
                from(project("display").layout.buildDirectory.file("webApp/index.html"))
                rename { "display.html" }
                into(layout.buildDirectory.dir("web/src/pages"))
                duplicatesStrategy = DuplicatesStrategy.INCLUDE
            }
            copy {
                from(project("display").layout.buildDirectory.file("webApp"))
                exclude("index.html")
                into(layout.buildDirectory.dir("web/public"))
                duplicatesStrategy = DuplicatesStrategy.INCLUDE
            }
        }
    }

true
