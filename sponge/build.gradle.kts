import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    java
    id("org.spongepowered.gradle.plugin") version "2.0.2"
}

// This may not be required, but has solved issues in the past
//compileJava.options.encoding = 'UTF-8'

repositories {
    maven("https://repo.spongepowered.org/repository/maven-public/")
}

dependencies {
    compileOnly("org.spongepowered:spongeapi:9.0.0")
    compileOnly(project(":chatregulator-api"))
}

sponge {
    apiVersion("9.0.0")
    license("All Rights Reserved")
    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version("1.0")
    }
    plugin("chatregulator") {
        displayName("ChatRegulator")
        entrypoint("me.adrianed.chatregulator.sponge.SpongePlugin")
        description("A global chat regulator")
        contributor("4drian3d") {
            description("Author")
        }
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
    }
}
