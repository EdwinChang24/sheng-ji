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
        ktfmt().kotlinlangStyle().configure { it.setManageTrailingCommas(true) }
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktfmt().kotlinlangStyle().configure { it.setManageTrailingCommas(true) }
    }
}

val mergeWeb: Task by
    tasks.creating {
        group = "custom"
        dependsOn(":display:buildWebApp")
        inputs.file(project("display").file("version.txt"))
        doLast {
            delete { delete("build/web") }
            copy {
                from(project.file("website"))
                exclude("node_modules", "dist")
                into(layout.buildDirectory.dir("web"))
            }
            copy {
                from(project("display").layout.buildDirectory.file("webApp"))
                rename("index.html", "display.html")
                into(layout.buildDirectory.dir("web/public"))
                duplicatesStrategy = DuplicatesStrategy.INCLUDE
            }
            layout.buildDirectory
                .file("web/src/version/version.json")
                .get()
                .asFile
                .writeText(
                    "\"${project("display").file("version.txt").readText().lines().first()}\""
                )
        }
    }

true
