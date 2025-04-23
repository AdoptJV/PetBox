package com.jvdev

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDate


import com.jvdev.com.jvdev.models.User

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, World!")
        }
        route("/api") {
            get("/") {
                val usr: User = User(
                    "1", "Marcelo",
                    LocalDate.of(2005, 10, 10),
                    "jpbarioni@usp.br", "12345", "url",
                    "43999478536", null, null,
                    null, "Admin"
                )
                val age: Int = usr.getAge()
                call.respondText("I am $age years old!")
            }
        }
    }
}
