enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "ChatRegulator"


plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

arrayOf("api", "plugin").forEach {
    include("chatregulator-$it")
    project(":chatregulator-$it").projectDir = file(it)
}
