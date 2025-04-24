package com.jvdev
import com.jvdev.com.database.connectToDatabase
import com.jvdev.com.database.insertUser
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.jvdev.com.models.User
import io.ktor.http.*
import io.ktor.server.request.*
import java.io.File
import java.time.LocalDate


import com.jvdev.com.jvdev.models.User

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondFile(File("src/main/resources/HtmlData/HomePage.html"))
        }
        get ("/register") {
            call.respondFile(File("src/main/resources/HtmlData/Register.html"))
        }
        post("/register") {
            val parameters = call.receiveParameters()
            val username = parameters["username"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val password = parameters["password"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val email = parameters["email"] ?: return@post call.respond(HttpStatusCode.BadRequest)

            val connection = connectToDatabase()

            if (insertUser(connection , User(username, email, password)))
                call.respondFile(File("src/main/resources/HtmlData/RegisterSuccessfully.html"))
            else
                call.respondFile(File("src/main/resources/HtmlData/RegisterFailed.html"))
        }

    }
}
