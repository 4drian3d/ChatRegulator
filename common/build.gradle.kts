plugins {
    `java-library`
    alias(libs.plugins.blossom)
    alias(libs.plugins.idea.ext)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    api(libs.configurate)
    api(projects.chatregulatorApi)
    compileOnly(libs.slf4j)
    compileOnly(libs.adventure.minimessage)
}

tasks {
    compileJava {
        options.release.set(17)
        options.encoding = "UTF-8"
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
