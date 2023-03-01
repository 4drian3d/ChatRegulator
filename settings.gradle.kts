rootProject.name = "ChatRegulator"

arrayOf("api", "plugin").forEach {
    include("chatregulator-$it")
    project(":chatregulator-$it").projectDir = file(it)
}