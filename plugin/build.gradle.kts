plugins {
    java
    alias(libs.plugins.shadow)
    alias(libs.plugins.runvelocity)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation(projects.chatregulatorCommon)
    implementation(libs.bstats)

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
        relocate("org.bstats", "io.github._4drian3d.chatregulator.libs.bstats")
    }
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "failed")
        }
    }
    runVelocity {
        velocityVersion(libs.versions.velocity.get())
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