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
import com.jvdev.com.models.UserID
import io.ktor.server.http.content.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Application.configureRouting() {
    val root = File(System.getProperty("user.dir")).let {
        if (File(it, "frontend").exists()) it
        else it.parentFile
    }
    routing {
        staticFiles("/", File("$root/frontend"))
        get("/") {
            call.respondFile(File("$root/frontend/html/HomePage.html"))
        }
        get ("/register") {
            call.respondFile(File("$root/frontend/html/RegisterUser.html"))
        }
        post("/register") {
            val parameters = call.receiveParameters()

            val username = parameters["username"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val name = parameters["name"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val password = parameters["password"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val email = parameters["email"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val birthday = parameters["birthday"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val phone = parameters["phone"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val cep = parameters["cep"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val description = parameters["description"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val connection = connectToDatabase()

            println(username + name + password + email + birthday + cep + description)

            if (insertUser(
                    User(
                        id = -1,
                        username = username,
                        name = name,
                        psw = password,
                        address = buscarEndereco(cep) ,
                        birthday = LocalDate.parse(birthday, DateTimeFormatter.ISO_DATE),
                        email = email,
                        phone = phone,
                        description = description
                    )))
                call.respondFile(File("$root/frontend/html/RegisterSuccessfully.html"))
            else
                call.respondFile(File("$root/frontend/html/RegisterFailed.html"))
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
