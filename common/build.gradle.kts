plugins {
    `java-library`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    api(libs.configurate)
    api(projects.chatregulatorApi)
}

tasks {
    compileJava {
        options.release.set(17)
        options.encoding = "UTF-8"
    }
}