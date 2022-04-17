import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    buildsrc.convention.`kotlin-jvm`
    id("org.jetbrains.kotlinx.knit")
}

dependencies {
    implementation(projects.library)
    implementation(projects.scriptGenerator)
    implementation(projects.wrapperGenerator)

    implementation("org.jetbrains.kotlin:kotlin-scripting-common")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm-host")
    implementation("org.jetbrains.kotlin:kotlin-scripting-dependencies")
    implementation("org.jetbrains.kotlin:kotlin-scripting-dependencies-maven")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

    implementation("org.jetbrains.kotlinx:kotlinx-knit:0.4.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-knit-test:0.4.0")
}

tasks.withType<KotlinCompile> {
    mustRunAfter(tasks.knit)
}

sourceSets.test {
    java.srcDirs(
//        "example",
        "test",
        "util",
    )
}

knit {
    val docsDir = rootProject.layout.projectDirectory.dir("docs")
    rootDir = docsDir.asFile
    files = docsDir.asFileTree.matching {
        exclude("**/code/**")
        include("**/*.md")
    }
}

tasks.withType<Test>().configureEach { dependsOn(tasks.knit) }

tasks.knit {
    doLast("trim leading whitespace") {
        // quick hack:
        // Because knit-include.ftl is empty, and
        // that adds a blank line at the top, but the shebang needs to be the first line...
        project.layout.projectDirectory.dir("example")
            .asFileTree
            .matching {
                include("**/*.kts")
            }
            .filter { it.isFile && it.exists() }
            .forEach { file ->
                val trimmed = file.readText().trimStart()
                file.writeText(trimmed)
            }
    }
}
