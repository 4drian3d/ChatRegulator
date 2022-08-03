plugins {
    kotlin("jvm") version "1.5.21"
    kotlin("kapt") version "1.5.21"
}

repositories {
    maven("https://repo.kryptonmc.org/releases")
}

dependencies {
    compileOnly("org.kryptonmc", "api", "0.37.1")
    kapt("org.kryptonmc", "api", "0.37.1")
    compileOnly(kotlin("stdlib"))
}