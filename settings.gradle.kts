rootProject.name = "chatregulator-parent"

include("chatregulator-api")
include("chatregulator-core")

project(":chatregulator-api").projectDir = file("api")
project(":chatregulator-core").projectDir = file("core")