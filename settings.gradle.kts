pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "chatregulator-parent"

listOf("api", "common", "paper", "velocity", "krypton").forEach {
    include("chatregulator-$it")
    project(":chatregulator-$it").projectDir = file(it)
}