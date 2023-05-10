plugins {
    `java-library`
    `maven-publish`
    signing
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    compileOnlyApi(libs.velocity)
}



tasks {
    compileJava {
        options.release.set(17)
        options.encoding = "UTF-8"
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
        (options as StandardJavadocDocletOptions).links(
            "https://jd.advntr.dev/api/4.12.0/",
            "https://jd.advntr.dev/text-minimessage/4.12.0/",
            "https://jd.papermc.io/velocity/3.0.0/"
        )
    }
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
