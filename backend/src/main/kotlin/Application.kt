package com.jvdev

import freemarker.cache.FileTemplateLoader
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import java.io.File

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureSessions()
    install(ContentNegotiation) {
        json()
    }
    install(CORS) {
        //allowHost("http://localhost:3000")
        anyHost()   // APAGAR ESSA LINHA DEPOIS!!!
        allowHost("localhost:8080", schemes = listOf("http", "https"))
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowCredentials = true
        allowNonSimpleContentTypes = true
        methods.addAll(HttpMethod.DefaultMethods)
        headers.addAll(listOf(HttpHeaders.ContentType))
    }
    configureRouting()

}

