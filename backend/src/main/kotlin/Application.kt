package com.jvdev

import com.jvdev.com.cep.externalApiAvailable
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureSessions()
    install(ContentNegotiation) { json() }
    install(CORS) {
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

    launch {
        while (true) {
            try {
                val url = URL("https://viacep.com.br/")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 2000
                connection.readTimeout = 2000

                val responseCode = connection.responseCode
                externalApiAvailable.set(responseCode == 200)

            } catch (e: Exception) {
                externalApiAvailable.set(false)
            }

            delay(10_000)
        }
    }
}