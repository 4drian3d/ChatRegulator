plugins {
    java
    alias(libs.plugins.shadow)
    alias(libs.plugins.runvelocity)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation(projects.chatregulatorCommon)
    implementation(libs.bstats)

    compileOnly(libs.miniplaceholders)
    compileOnly(libs.futures)

    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        minimize()
        archiveFileName.set("ChatRegulator-Velocity-${project.version}.jar")
        relocate("org.bstats", "io.github._4drian3d.chatregulator.libs.bstats")
    }
    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }

    compileJava {
        options.release.set(21)
        options.encoding = "UTF-8"
    }
}