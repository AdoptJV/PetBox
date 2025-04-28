package com.jvdev
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
import com.jvdev.com.database.queryAllPets
import com.jvdev.com.database.queryUser
import com.jvdev.com.encryption.pswUtil
import com.jvdev.com.models.Pet
import com.jvdev.com.models.Sex
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.sessions.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Application.configureRouting() {
    val root = File(System.getProperty("user.dir")).let { // define o caminho até a raiz do projeto
        if (File(it, "frontend").exists()) it
        else it.parentFile
    }
    routing {
        staticFiles("/", File("$root/frontend")) // path estatico
        get("/") { // pagina inicial
            val userSession = call.sessions.get<UserSession>()
            call.respond(FreeMarkerContent("homepage.ftl", mapOf( // usa o template para definir qual a home page (logged?)
                "isUserLoggedIn" to (userSession != null),
                "username" to (userSession?.username ?: "")
            )))
        }
        get ("/register-user") { // pagina de registro de usuario
            call.respond(FreeMarkerContent("RegisterUser.ftl", mapOf(
                "failedRegister" to (false)
            )))
        }
        get ("/register-pet") { // pagina de registro de pet
            if (call.sessions.get<UserSession>() == null) { call.respond(HttpStatusCode.Unauthorized) } // precisa estar logado
            call.respondFile(File("$root/frontend/html/RegisterPetPage.html"))
        }
        get ("/login") { // pagina de login de usuario
            call.respondFile(File("$root/frontend/html/LoginPage.html"))
        }

        get("/pets") {
            val pets = queryAllPets() // pets no banco de dados
            call.respond(FreeMarkerContent("PetsPage.ftl", mapOf(
                "pets" to (pets)
            )))
        }

        get("/cep/{cep}") { // (oq isso faz, joão? ) !!!!!
            val cep = call.parameters["cep"] ?: return@get call.respondText("CEP não informado", status = io.ktor.http.HttpStatusCode.BadRequest)
            try {
                val endereco = buscarEndereco(cep)
                call.respond(println(endereco))
            } catch (e: Exception) {
                call.respondText("Erro ao buscar o CEP: ${e.localizedMessage}", status = io.ktor.http.HttpStatusCode.InternalServerError)
            }
        }

        post("/register-user") { // registra usuario
            val parameters = call.receiveParameters()

            // recebe os parametros
            val username = parameters["username"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val name = parameters["name"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val password = parameters["password"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val email = parameters["email"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val birthday = parameters["birthday"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val phone = parameters["phone"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val cep = parameters["cep"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val description = parameters["description"] ?: return@post call.respond(HttpStatusCode.BadRequest)

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
                call.respondRedirect("/")
            else
                call.respond(FreeMarkerContent("RegisterUser.ftl", mapOf(
                    "failedRegister" to (true)
                )))
        }
        post("/register-pet") {
            val userSession = call.sessions.get<UserSession>()
            val parameters = call.receiveParameters()

            // recebe os parametros
            val name = parameters["name"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val specie = parameters["specie"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val age = (parameters["age"] ?: return@post call.respond(HttpStatusCode.BadRequest)).toInt()
            val sex = Sex.valueOf(parameters["sex"] ?: return@post call.respond(HttpStatusCode.BadRequest))
            val castrated = (parameters["castrated"] ?: return@post call.respond(HttpStatusCode.BadRequest)) == "true"

            if (insertPet(
                Pet(
                    id = -1,
                    name = name,
                    species = specie,
                    sex = sex,
                    age = age,
                    castrated = castrated,
                    photoUrl = null,
                    owner = userSession!!.id
                )
            ))
                call.respondRedirect("/")
            else
                call.respond(HttpStatusCode.BadRequest)

        }

        post("/login") {
            val parameters = call.receiveParameters()

            // recebe os parametros
            val username = parameters["username"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val password = parameters["password"] ?: return@post call.respond(HttpStatusCode.BadRequest)

            val user = queryUser(username) // procura pelo usuario

            if (user == null) call.respondText("Usuário inexistente", status=HttpStatusCode.BadRequest)
            if (pswUtil.verify(password, user!!.psw)) {
                call.sessions.set(UserSession(user.id, user.username))
                call.respondRedirect("/")
            }
            else call.respondText("Senha errada", status = io.ktor.http.HttpStatusCode.BadRequest)
        }

        post("/logout") {
            call.sessions.clear<UserSession>()
            call.respondRedirect("/")
        }

    }
}
