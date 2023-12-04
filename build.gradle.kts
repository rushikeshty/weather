// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false

}
buildscript {
    val agp_version by extra("8.1.2")
    repositories {
        google()
    }


    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        val nav_version = "2.5.0"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")

    }
}