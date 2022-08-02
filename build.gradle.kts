import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    //id("java-platform")
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    shadow(project(":chatregulator-api"))
    shadow(project(":chatregulator-common"))
    shadow(project(":chatregulator-velocity"))
    shadow(project(":chatregulator-paper"))
    shadow(project(":chatregulator-krypton"))
}

tasks {
    shadowJar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveFileName.set("ChatRegulator.jar")
        configurations = listOf(project.configurations.shadow.get())
    }
}
