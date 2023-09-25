plugins {
    java
    alias(libs.plugins.shadow)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation(projects.chatregulatorCommon)
    implementation(libs.hexlogger)

    compileOnly(libs.miniplaceholders)
    compileOnly(libs.futures)

    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)

    testImplementation(libs.configurate)
    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.mockito)
    testImplementation(libs.velocity)
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        minimize()
        archiveFileName.set("ChatRegulator-${project.version}.jar")
        relocate("io.github._4drian3d.velocityhexlogger", "io.github._4drian3d.chatregulator.libs.hexlogger")
        relocate("net.kyori.adventure.text.logger.slf4j", "io.github._4drian3d.chatregulator.libs.component.logger")
        relocate("org.spongepowered.configurate", "io.github._4drian3d.chatregulator.libs.configurate")
        relocate("io.leangen.geantyref", "io.github._4drian3d.chatregulator.libs.geantyref")
        relocate("com.typesafe.config", "io.github._4drian3d.chatregulator.libs.config")
    }
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "failed")
        }
    }

    compileJava {
        options.release.set(17)
        options.encoding = "UTF-8"
    }

    compileTestJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }
}