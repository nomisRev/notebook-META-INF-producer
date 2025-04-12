plugins {
    kotlin("jvm") version "2.1.10"
}

group = "org.jetbrains"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":"))
    compileOnly("org.jetbrains.kotlinx:kotlin-jupyter-api:0.12.0-398")
    compileOnly("org.jetbrains.kotlinx:kotlin-jupyter-lib:0.12.0-398")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
