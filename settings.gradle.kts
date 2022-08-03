rootProject.name = "chatregulator-parent"

listOf("api", "paper", "velocity", /*"krypton",*/ "sponge").forEach {
    include("chatregulator-$it")
    project(":chatregulator-$it").projectDir = file(it)
}