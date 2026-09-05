plugins {
    `java-library`
    alias(libs.plugins.blossom)
    alias(libs.plugins.idea.ext)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    compileOnlyApi(libs.configurate)
    api(projects.chatregulatorApi)
    compileOnly(libs.slf4j)
    compileOnly(libs.adventure.minimessage)
    compileOnly(libs.miniplaceholders)
    compileOnly(libs.guava)
    compileOnly(libs.adventure.plain)

    testImplementation(libs.configurate)
    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(libs.mockito)
    testImplementation(libs.slf4j)
    testImplementation(libs.adventure.api)
    testImplementation(libs.adventure.minimessage)
    testImplementation(libs.velocity)
}

tasks {
    compileJava {
        options.release.set(21)
        options.encoding = "UTF-8"
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "failed")
        }
    }

    compileTestJava {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
}

sourceSets {
    main {
        blossom {
            javaSources {
                property("version", project.version.toString())
            }
        }
    }
}
