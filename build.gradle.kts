// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
}

// Настройки для решения проблем с кэшем
allprojects {
    tasks.withType<org.gradle.api.tasks.Delete> {
        doLast {
            delete(fileTree("${project.buildDir}") {
                include("**/*.bin")
            })
        }
    }
}