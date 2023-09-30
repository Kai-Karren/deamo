import java.net.URI

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "deamo"

sourceControl {
    gitRepository(URI.create("https://github.com/Kai-Karren/conversations.git")) {
        producesModule("de.kaikarren:conversations")
    }
}