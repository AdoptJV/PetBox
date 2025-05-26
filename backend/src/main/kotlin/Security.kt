package com.jvdev

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.auth.*

fun Application.configureSecurity() {
        install(Authentication) {
            session<UserSession>("auth-session") {
                validate { session ->
                    if (session.id > 0) session else null
                }
                challenge {
                    // responde 401 e interrompe o processamento
                    call.respond(HttpStatusCode.Unauthorized, "Você precisa estar logado")
                }
            }
        }
}

