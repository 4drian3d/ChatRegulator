plugins {
    java
}

dependencies {
    compileOnly("net.kyori:adventure-api:4.11.0")
    compileOnly("net.kyori:adventure-text-serializer-plain:4.11.0")
    compileOnly("org.spongepowered:configurate-hocon:4.0.0")
    compileOnly("org.slf4j:slf4j-api:1.7.36")
    implementation("com.spotify:completable-futures:0.3.1")
}

java {
    withSourcesJar()
    withJavadocJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}