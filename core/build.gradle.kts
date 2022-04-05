plugins {
    id("net.kyori.blossom") version "1.3.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    maven("https://repo.fvdh.dev/releases")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly(project(":chatregulator-api"))
    shadow("org.spongepowered:configurate-hocon:4.1.2")
    shadow("org.jetbrains:annotations:23.0.0")

    compileOnly("com.github.4drian3d:MiniPlaceholders:20a8c111f5")

    compileOnly("net.frankheijden.serverutils:ServerUtils:3.4.0")

    annotationProcessor("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

tasks.compileJava {
    options.encoding = Charsets.UTF_8.name()

    options.release.set(11)
}

blossom{
    replaceTokenIn("src/main/java/me/dreamerzero/chatregulator/core/utils/Constants.java")
    replaceToken("{name}", rootProject.name)
    replaceToken("{id}", id,)
    replaceToken("{version}", version)
    replaceToken("{description}", description)
    replaceToken("{url}", url)
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
}