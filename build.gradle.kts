plugins {
    `java-library`
    `maven-publish`
    signing
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
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    compileOnly(libs.configurate)
    compileOnly(libs.caffeine)
    shadow(libs.libby)

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


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group as String
            artifactId = "chatregulator-api"
            version = version
            from(components["java"])
        }
    }
}

blossom {
    replaceTokenIn("src/main/java/me/dreamerzero/chatregulator/utils/Constants.java")
    replaceToken("{name}", rootProject.name)
    replaceToken("{id}", property("id"))
    replaceToken("{version}", version)
    replaceToken("{description}", description)
    replaceToken("{url}", property("url"))
    replaceToken("{configurate}", libs.versions.configurate)
    replaceToken("{geantyref}", libs.versions.geantyref)
    replaceToken("{caffeine}", libs.versions.caffeine)
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

    javadoc {
        options.encoding = Charsets.UTF_8.name()
        (options as StandardJavadocDocletOptions).links(
            "https://jd.adventure.kyori.net/api/4.12.0/",
            "https://jd.adventure.kyori.net/text-minimessage/4.12.0/",
            "https://jd.papermc.io/velocity/3.0.0/"
        )
    }
}
