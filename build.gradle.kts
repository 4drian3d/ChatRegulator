import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation

plugins {
    java
    id("net.kyori.blossom") version "1.3.1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    `maven-publish`
}

repositories {
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.fvdh.dev/releases")
    maven("https://jitpack.io")
    maven("https://repo.alessiodp.com/releases/")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
    withSourcesJar()
    withJavadocJar()
}

val url = property("url") as String ?: ""
val id = property("id") as String ?: "chatregulator"
val configurate = property("configurate-version") as String
val geantyref = property("geantyref-version") as String
val caffeine = property("caffeine-version") as String

dependencies {
    compileOnly("org.spongepowered:configurate-hocon:$configurate")
    compileOnly("com.github.ben-manes.caffeine:caffeine:$caffeine")
    compileOnly("org.jetbrains:annotations:23.0.0")
    shadow("net.byteflux:libby-velocity:1.1.5")

    compileOnly("com.github.4drian3d:MiniPlaceholders:1.1.1")

    compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")

    testImplementation("org.slf4j:slf4j-api:1.7.36")
    testImplementation("org.spongepowered:configurate-hocon:$configurate")
    testImplementation(platform("org.junit:junit-bom:5.8.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:4.1.0")
    testImplementation("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
    testImplementation("com.github.ben-manes.caffeine:caffeine:$caffeine")
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group as String
            artifactId = id
            version = version
            from(components["java"])
        }
    }
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).links(
        "https://jd.adventure.kyori.net/api/4.11.0/",
        "https://jd.adventure.kyori.net/text-minimessage/4.11.0/",
        "https://jd.papermc.io/velocity/3.0.0/"
    )
}

blossom {
    replaceTokenIn("src/main/java/me/dreamerzero/chatregulator/utils/Constants.java")
	replaceToken("{name}", rootProject.name)
    replaceToken("{id}", id)
	replaceToken("{version}", version)
	replaceToken("{description}", description)
    replaceToken("{url}", url)
    replaceToken("{configurate}", configurate)
    replaceToken("{geantyref}", geantyref)
    replaceToken("{caffeine}", caffeine)
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

    compileJava {
        options.release.set(16)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
