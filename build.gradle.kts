buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        //classpath("com.android.tools.build:gradle:4.0.0")
        classpath("com.google.gms:google-services:4.4.0")
        //classpath("com.github.bumptech.glide:glide:4.14.2")
        // Skip this if you don't want to use integration libraries or configure Glide.
        //annotationProcessor 'com.github.bumptech.glide:compiler:4.14.2'
    }
}

plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    // Add the dependency for the Google services Gradle plugin
    id("com.google.gms.google-services") version "4.4.0"
}