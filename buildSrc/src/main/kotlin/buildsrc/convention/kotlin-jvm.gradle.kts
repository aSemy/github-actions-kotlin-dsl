package buildsrc.convention

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("buildsrc.convention.subproject")
    kotlin("jvm")
    `java-library`

    kotlin("plugin.serialization")

    // Code quality.
    id("org.jlleitschuh.gradle.ktlint")
}

dependencies {
    implementation(platform("org.jetbrains.kotlinx:kotlinx-serialization-bom:1.3.2"))

    testImplementation(platform("io.kotest:kotest-bom:5.2.3"))
    testImplementation("io.kotest:kotest-runner-junit5")
    testImplementation("io.kotest:kotest-assertions-core")
    testImplementation("io.kotest:kotest-property")
    testImplementation("io.kotest:kotest-framework-datatest")
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        apiVersion = "1.6"
        languageVersion = "1.6"
    }

    kotlinOptions.freeCompilerArgs += listOf(
        "-opt-in=kotlin.RequiresOptIn",
        "-opt-in=kotlin.ExperimentalStdlibApi",
        "-opt-in=kotlin.time.ExperimentalTime",
    )
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
