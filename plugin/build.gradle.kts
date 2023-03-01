plugins {
    java
    alias(libs.plugins.blossom)
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://repo.alessiodp.com/releases/")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    compileOnly(libs.configurate)
    compileOnly(libs.caffeine)
    implementation(libs.libby)
    implementation(project(":chatregulator-api"))

    compileOnly(libs.miniplaceholders)

    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)

    testImplementation(libs.slf4j)
    testImplementation(libs.configurate)
    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.mockito)
    testImplementation(libs.velocity)
    testImplementation(libs.caffeine)
}

blossom {
    replaceTokenIn("src/main/java/io/github/_4drian3d/utils/Constants.java")
    replaceToken("{name}", rootProject.name)
    replaceToken("{id}", property("id"))
    replaceToken("{version}", version)
    replaceToken("{description}", description)
    replaceToken("{url}", property("url"))
    replaceToken("{configurate}", libs.versions.configurate)
    replaceToken("{geantyref}", libs.versions.geantyref)
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        minimize()
        archiveFileName.set("ChatRegulator-${project.version}.jar")
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
}