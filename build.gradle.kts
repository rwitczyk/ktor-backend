val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.4.32"
}

group = "com.rwitczyk"
version = "0.0.1"
application {
    mainClass.set("com.rwitczyk.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")

    implementation("org.jetbrains.exposed:exposed:0.17.13")
    implementation("com.zaxxer", "HikariCP", "3.2.0")
    runtimeOnly("org.postgresql", "postgresql", "42.2.20")
}