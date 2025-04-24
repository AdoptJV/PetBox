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
import com.jvdev.com.cep.buscarEndereco

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
            val cep = parameters["cep"] ?: return@post call.respond(HttpStatusCode.BadRequest)

            val connection = connectToDatabase()

            val endereco = buscarEndereco(cep)

            if (insertUser(connection , User(username, email, password, endereco)))
                call.respondFile(File("src/main/resources/HtmlData/RegisterSuccessfully.html"))
            else
                call.respondFile(File("src/main/resources/HtmlData/RegisterFailed.html"))
        }
        get("/cep/{cep}") {
            val cep = call.parameters["cep"] ?: return@get call.respondText("CEP não informado", status = io.ktor.http.HttpStatusCode.BadRequest)

            try {
                val endereco = buscarEndereco(cep)
                call.respond(println(endereco))
            } catch (e: Exception) {
                call.respondText("Erro ao buscar o CEP: ${e.localizedMessage}", status = io.ktor.http.HttpStatusCode.InternalServerError)
            }
        }

    }
}
