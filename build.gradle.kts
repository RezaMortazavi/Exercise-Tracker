val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val kgraphqlVersion: String by project
val exposedVersion: String by project
val h2Version: String by project
val hikariCpVersion: String by project
val koinVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
}

group = "com.octopus"
version = "0.0.1"
application {
    mainClass.set("com.octopus.ApplicationKt")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-serialization:$ktorVersion")

    implementation("com.apurebase:kgraphql:$kgraphqlVersion")
    implementation("com.apurebase:kgraphql-ktor:$kgraphqlVersion")

    implementation("com.h2database:h2:$h2Version")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("com.zaxxer:HikariCP:$hikariCpVersion")

    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")


    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}