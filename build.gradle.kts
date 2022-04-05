plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

subprojects {
    apply(plugin = "java")
    group = "me.dreamerzero.chatregulator"
    version = "3.0.0-SNAPSHOT"
    description = "A global chat regulator for you Velocity network"
    val url: String = "https://forums.velocitypowered.com/t/chatregulator-a-global-chat-regulator-for-velocity/962"
    val id: String = "chatregulator"

    repositories {
        mavenCentral()
        maven("https://papermc.io/repo/repository/maven-public/")
    }

    dependencies {
        compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
    }
}

tasks {
    shadowJar {
        archiveFileName.set("MiniPlaceholders.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations = listOf(project.configurations.shadow.get())
    }
    build {
        dependsOn(shadowJar)
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
