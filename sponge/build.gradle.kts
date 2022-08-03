plugins {
    java
    id("org.spongepowered.gradle.plugin") version "2.0.2"
}

// This may not be required, but has solved issues in the past
//compileJava.options.encoding = 'UTF-8'

repositories {
    jcenter()
    maven("https://repo.spongepowered.org/repository/maven-public/")
}

dependencies {
    compileOnly("org.spongepowered:spongeapi:8.0.0")
}
