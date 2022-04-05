plugins {
    `maven-publish`
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
    testImplementation(platform("org.junit:junit-bom:5.8.1"))
    testImplementation("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "failed")
        }
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

val projectGroup: String = project.group as String
val projectVersion: String = project.version as String

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = projectGroup
            artifactId = project.name
            version = projectVersion
            from(components["java"])
        }
    }
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).links(
        "https://jd.adventure.kyori.net/api/4.10.1/",
        "https://jd.adventure.kyori.net/text-minimessage/4.10.1/",
        "https://jd.velocitypowered.com/3.0.0/"
    )
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(11)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}