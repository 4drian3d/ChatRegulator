import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation

plugins {
    java
    id("net.kyori.blossom") version "1.3.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenLocal()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.fvdh.dev/releases")
}

dependencies {
    shadow("org.spongepowered:configurate-hocon:4.1.2")
    shadow("org.jetbrains:annotations:23.0.0")
    //TODO: Remove in Velocity 3.1.2
    shadow("net.kyori:adventure-text-minimessage:4.10.0"){
        exclude("net.kyori", "adventure-api")
    }

    compileOnly("net.frankheijden.serverutils:ServerUtils:3.4.0")

    compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")

    testImplementation("org.slf4j:slf4j-api:1.7.32")
    testImplementation("org.spongepowered:configurate-hocon:4.1.2")
    testImplementation(platform("org.junit:junit-bom:5.8.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:4.1.0")
    testImplementation("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
    testImplementation("net.kyori:adventure-text-minimessage:4.10.0")

}

group = "me.dreamerzero.chatregulator"
version = "2.1.0"
description = "A global chat regulator for you Velocity network"
val url = "https://forums.velocitypowered.com/t/chatregulator-a-global-chat-regulator-for-velocity/962"
val id = "chatregulator"

java.sourceCompatibility = JavaVersion.VERSION_11

blossom{
	val constants = "src/main/java/me/dreamerzero/chatregulator/utils/Constants.java"
	replaceToken("{name}", rootProject.name, constants)
    replaceToken("{id}", id, constants)
	replaceToken("{version}", version, constants)
	replaceToken("{description}", description, constants)
    replaceToken("{url}", url, constants)
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
        prefix = "me.dreamerzero.chatregulator.libs"
    }
    test {
        useJUnitPlatform()
        testLogging {
		    events("passed", "skipped", "failed")
	    }
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
