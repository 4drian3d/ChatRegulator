import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    //id("java-platform")
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    shadow(project(":chatregulator-api"))
    shadow(project(":chatregulator-velocity"))
    shadow(project(":chatregulator-paper"))
    shadow(project(":chatregulator-krypton"))
    shadow(project(":chatregulator-sponge"))
}

tasks {
    shadowJar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveFileName.set("ChatRegulator.jar")
        configurations = listOf(project.configurations.shadow.get())
    }
}
