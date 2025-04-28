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
import com.jvdev.com.database.insertPet
import com.jvdev.com.database.queryUser
import com.jvdev.com.encryption.pswUtil
import com.jvdev.com.models.Pet
import com.jvdev.com.models.Sex
import com.jvdev.com.models.UserID
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.sessions.*
import io.ktor.util.*
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
            val userSession = call.sessions.get<UserSession>()
            call.respond(FreeMarkerContent("HomePage.ftl", mapOf(
                "IsUserLoggedIn" to (userSession != null),
                "username" to (userSession?.username ?: "")

            )))
            // call.respondFile(File("$root/frontend/html/HomePage.html"))
        }
        get ("/register") {
            call.respondFile(File("$root/frontend/html/RegisterUser.html"))
        }
        get ("/register-pet") {
            call.respondFile(File("$root/frontend/html/RegisterPetPage.html"))
        }
        get ("/login") {
            call.respondFile(File("$root/frontend/html/LoginPage.html"))
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

            println("$username $name $password $email $birthday $cep $description")

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


        post("/register-pet") {
            val parameters = call.receiveParameters() // recebe os parametros do formulario

            val name = parameters["name"] ?: return@post call.respond(HttpStatusCode.BadRequest) // nome do pet
            val specie = parameters["specie"] ?: return@post call.respond(HttpStatusCode.BadRequest) // especie do pet
            val age = (parameters["age"] ?: return@post call.respond(HttpStatusCode.BadRequest)).toInt() // idade do pet
            val sex = Sex.valueOf(parameters["sex"] ?: return@post call.respond(HttpStatusCode.BadRequest)) // sexo do pet
            val castrated = (parameters["castrated"] ?: return@post call.respond(HttpStatusCode.BadRequest)) == "true" // se o pet é castrado

            println("$name $age $specie $sex $castrated")

            insertPet(
                Pet(
                    id = -1,
                    name = name,
                    species = specie,
                    sex = sex,
                    age = age,
                    castrated = castrated,
                    photoUrl = null,
                    owner = 1
                )
            )
            call.respondFile(File("$root/frontend/html/RegisterSuccessfully.html"))
        }

        post("/login") {
            val parameters = call.receiveParameters()

            val username = parameters["username"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val password = parameters["password"] ?: return@post call.respond(HttpStatusCode.BadRequest)

            val user = queryUser(username)

            if (user == null) call.respondText("usuario inexistente", status=HttpStatusCode.BadRequest)
            if (pswUtil.verify(password, user!!.psw)) {
                call.sessions.set(UserSession(user.id, user.username))
                call.respondText("logado", status = io.ktor.http.HttpStatusCode.OK)
            }
            else call.respondText("senha errada", status = io.ktor.http.HttpStatusCode.BadRequest)
        }

        post("/logout") {
            call.sessions.clear<UserSession>()
            call.respondFile(File("$root/frontend/html/HomePage.html"))
        }

    }
}
