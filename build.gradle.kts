import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation

plugins {
    java
    id("net.kyori.blossom") version "1.2.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://nexus.velocitypowered.com/repository/maven-public/")
    }

    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://jitpack.io")
    }

    maven {
        url = uri("https://repo.fvdh.dev/releases")
    }

}

dependencies {
    shadow("com.github.simplix-softworks:simplixstorage:3.2.3")
    shadow("net.kyori:adventure-text-minimessage:4.2.0-SNAPSHOT"){
        exclude("net.kyori", "adventure-api")
    }

    compileOnly("net.frankheijden.serverutils:ServerUtils:3.1.1")
    compileOnly("com.velocitypowered:velocity-api:3.0.1")
    annotationProcessor("com.velocitypowered:velocity-api:3.0.1")
}

group = "net.dreamerzero.chatregulator"
version = "1.3.0"
description = "A Chat regulator for you Velocity network"
java.sourceCompatibility = JavaVersion.VERSION_11

blossom{
	val constants = "src/main/java/net/dreamerzero/chatregulator/utils/Constants.java"
	replaceToken("{name}", rootProject.name, constants)
	replaceToken("{version}", version, constants)
	replaceToken("{description}", description, constants)
    replaceToken("{url}", "https://forums.velocitypowered.com/t/chatregulator-a-global-chat-regulator-for-velocity/962", constants)
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        dependsOn(getByName("relocateShadowJar") as ConfigureShadowRelocation)
        minimize()
        archiveFileName.set("ChatRegulator.jar")
        configurations = listOf(project.configurations.shadow.get())
    }

    create<ConfigureShadowRelocation>("relocateShadowJar") {
        target = shadowJar.get()
        prefix = "net.dreamerzero.chatregulator.libs"
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
