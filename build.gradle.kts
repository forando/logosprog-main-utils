import org.gradle.kotlin.dsl.*
import org.junit.platform.gradle.plugin.EnginesExtension
import org.junit.platform.gradle.plugin.FiltersExtension
import org.junit.platform.gradle.plugin.JUnitPlatformExtension

buildscript {
    extensions.add("kotlinVersion", "1.2.10")

    dependencies {
        classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${extensions["kotlinVersion"]}")
    }
}

apply{

}

plugins {
    kotlin("jvm") version "1.2.10"
}

apply {
    plugin("org.junit.platform.gradle.plugin")
}

configure<JUnitPlatformExtension> {
    filters {
        engines {
            include("spek")
        }
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib", extensions["kotlinVersion"].toString()))
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:0.19.3")
    compile("org.slf4j:slf4j-api:1.7.24")
    compile("javax.persistence:persistence-api:1.0.2")
    compile("javax.validation:validation-api:1.1.0.Final")
    compile("joda-time:joda-time:2.9.7")

    testCompile("com.github.stefanbirkner:system-rules:1.16.0")

    testCompile("org.jetbrains.kotlin:kotlin-reflect:${extensions["kotlinVersion"]}")

    testCompile("org.jetbrains.kotlin:kotlin-test:${extensions["kotlinVersion"]}")

    testCompile("org.jetbrains.spek:spek-api:1.1.5"){
        this.exclude(group = "org.jetbrains.kotlin")
    }
    testRuntime("org.jetbrains.spek:spek-junit-platform-engine:1.1.5") {
        this.exclude(group = "org.junit.platform")
        this.exclude(group = "org.jetbrains.kotlin")
    }
}

// extension for configuration
fun JUnitPlatformExtension.filters(setup: FiltersExtension.() -> Unit) {
    when (this) {
        is ExtensionAware -> extensions.getByType(FiltersExtension::class.java).setup()
        else -> throw Exception("${this::class} must be an instance of ExtensionAware")
    }
}
fun FiltersExtension.engines(setup: EnginesExtension.() -> Unit) {
    when (this) {
        is ExtensionAware -> extensions.getByType(EnginesExtension::class.java).setup()
        else -> throw Exception("${this::class} must be an instance of ExtensionAware")
    }
}