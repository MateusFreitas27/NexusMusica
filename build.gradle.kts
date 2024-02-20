// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

buildscript{
    dependencies{
        classpath("androidx.navigation.safeargs.kotlin:androidx.navigation.safeargs.kotlin.gradle.plugin:2.5.3")
    }
}