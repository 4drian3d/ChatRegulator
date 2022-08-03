plugins {
    java
    id("org.spongepowered.plugin") version "0.11.3"
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

sponge {
    plugin {
        id = "chatregulator"
    }
}