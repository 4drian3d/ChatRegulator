plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("kapt") version "1.7.10"
}

repositories {
    maven("https://repo.kryptonmc.org/releases")
}

dependencies {
    compileOnly(project(":chatregulator-api"))
    compileOnly("org.kryptonmc", "api", "0.37.1")
    kapt("org.kryptonmc", "api", "0.37.1")
    compileOnly(kotlin("stdlib"))
}