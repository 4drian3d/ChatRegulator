plugins {
    java
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.1-R0.1-SNAPSHOT")
    compileOnly(project(":chatregulator-api"))
}

bukkit {
    name = "ChatRegulator"
    main = "me.adrianed.chatregulator.paper.PaperPlugin"
    apiVersion = "1.18"
    website = "https://github.com/4drian3d/ChatRegulator"
    authors = listOf("4drian3d")
    version = project.version as String
}