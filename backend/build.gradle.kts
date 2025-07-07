val kotlin_version: String by project
val ktor_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "2.3.10"
    kotlin("plugin.serialization") version "2.1.10"
}

group = "com.jvdev"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
}

dependencies {
    implementation("io.ktor:ktor-server-cors:$ktor_version")    // CORS

    implementation("io.ktor:ktor-server-core:$ktor_version")

    // Authentication
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")

    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-config-yaml:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // bcrypt (encriptação de senhas)
    implementation("at.favre.lib:bcrypt:0.10.2")

    // HTTP Client
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Implementação do banco de dados com sqlite
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")

    // Implementação da API ViaCEP
    // Créditos: https://github.com/thiago-cury/viacep-android-kotlin-retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.3.0")
    implementation("com.squareup.retrofit2:converter-gson:2.3.0")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-websockets:$ktor_version")

    testImplementation("io.ktor:ktor-server-test-host")

    // FreeMaker pra html dinamico
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
    implementation("io.ktor:ktor-server-freemarker:2.2.1")
    implementation("io.ktor:ktor-server-websockets:2.3.9")
    testImplementation("io.ktor:ktor-server-tests:2.3.10")

    testImplementation("io.ktor:ktor-client-plugins:2.3.10")
    testImplementation("io.ktor:ktor-server-test-host-jvm:3.1.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlin_version")

    testImplementation("org.jetbrains.kotlin:kotlin-test") // Kotlin test DSL
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

tasks.test {
    useJUnitPlatform()
}