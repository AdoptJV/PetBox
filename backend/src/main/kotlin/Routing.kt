package com.jvdev
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.request.*
import java.io.File
import io.ktor.server.http.content.*
import io.ktor.server.sessions.*
import com.jvdev.com.cep.buscarEndereco
import com.jvdev.com.encryption.pswUtil
import com.jvdev.com.database.*
import com.jvdev.com.models.User
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun Application.configureRouting() {
    val root = File(System.getProperty("user.dir")) // define o caminho até a raiz do projeto

    println(root)
    routing {
        singlePageApplication {
            react("/react-frontend")
        }
        route("/api") {
            staticFiles("/", File("$root/frontend")) // path estatico
            get("/") {
                call.respondText("Servidor rodando!!")
            }
            post("/") {
                val request = call.receive<Map<String, String>>()
                println("Received from React: ${request}")

                // recebe os parametros
                val username = request["username"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val password = request["password"] ?: return@post call.respond(HttpStatusCode.BadRequest)

                val user = queryUser(username) // procura pelo usuario

                if (user == null) call.respond(mapOf(
                    "redirect" to "/login?error=1",
                    "message" to "Invalid credentials"
                ))
                if (pswUtil.verify(password, user!!.psw)) {
                    println("$username ESTÁ LOGADO")
                    call.sessions.set(UserSession(user.id, user.username))
                    call.respond(mapOf(
                        "redirect" to "/home",
                        "message" to "Login successful"
                    ))
                } else call.respond(mapOf(
                    "redirect" to "/login?error=2",
                    "message" to "Invalid credentials"
                ))
            }
            get("/buscar-cep/{cep}") {
                val cep = call.parameters["cep"] ?: return@get call.respond(HttpStatusCode.BadRequest, "CEP inválido")
                try {
                    val endereco = buscarEndereco(cep)
                    println(endereco)
                    call.respond(endereco)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Erro ao buscar o CEP")
                }
            }

            get("/check/{username}"){
                val username = call.parameters["username"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Usarname inválido")
                call.respond(mapOf("exists" to checkUser(username)))
            }

//            get("/") { // pagina inicial
//                val userSession = call.sessions.get<UserSession>()
//                call.respond(
//                    FreeMarkerContent(
//                        "homepage.ftl", mapOf( // usa o template para definir qual a home page (logged?)
//                            "isUserLoggedIn" to (userSession != null),
//                            "username" to (userSession?.username ?: "")
//                        )
//                    )
//                )
//            }
//            get("/register-user") { // pagina de registro de usuario
//                call.respond(
//                    FreeMarkerContent(
//                        "RegisterUser.ftl", mapOf(
//                            "failedRegister" to (false)
//                        )
//                    )
//                )
//            }
//            get("/register-pet") { // pagina de registro de pet
//                if (call.sessions.get<UserSession>() == null) {
//                    call.respond(HttpStatusCode.Unauthorized)
//                } // precisa estar logado
//                call.respondFile(File("$root/frontend/html/RegisterPetPage.html"))
//            }
//            get("/login") { // pagina de login de usuario
//                call.respondFile(File("$root/frontend/html/LoginPage.html"))
//            }
//
//            get("/pets") {
//                val pets = queryAllPets() // pets no banco de dados
//                call.respond(
//                    FreeMarkerContent(
//                        "PetsPage.ftl", mapOf(
//                            "pets" to (pets)
//                        )
//                    )
//                )
//            }
//
//            get("/cep/{cep}") { // (oq isso faz, joão? ) !!!!!
//                val cep = call.parameters["cep"] ?: return@get call.respondText(
//                    "CEP não informado",
//                    status = io.ktor.http.HttpStatusCode.BadRequest
//                )
//                try {
//                    val endereco = buscarEndereco(cep)
//                    call.respond(println(endereco))
//                } catch (e: Exception) {
//                    call.respondText(
//                        "Erro ao buscar o CEP: ${e.localizedMessage}",
//                        status = io.ktor.http.HttpStatusCode.InternalServerError
//                    )
//                }
//            }
//
            post("/register-user") { // registra usuario
                val parameters = call.receive<Map<String, String>>()

                println("chegou no servidor")
                println(parameters)
                // recebe os parametros
                val username = parameters["username"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val name = parameters["name"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val password = parameters["password"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val email = parameters["email"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val birthdateRaw = parameters["birthdate"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val phone = parameters["phone"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val cep = parameters["cep"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                // val description = parameters["description"] ?: return@post call.respond(HttpStatusCode.BadRequest)

                val birthdate: LocalDate = try {
                    OffsetDateTime.parse(birthdateRaw)
                        .toLocalDate()
                } catch (e: Exception) {
                    LocalDate.parse(birthdateRaw.substring(0, 10))
                }

                println("passou na consistencia")

                if (insertUser(
                        User(
                            id = -1,
                            username = username,
                            name = name,
                            psw = password,
                            address = buscarEndereco(cep),
                            birthday = birthdate,
                            email = email,
                            phone = phone,
                            description = null
                        )
                    )
                )
                    call.respond(mapOf(
                        "redirect" to "/home",
                        "message" to "success"))
                else
                    call.respond(mapOf(
                        "redirect" to "/register?error=1",
                        "message" to "failure"))
            }
//            post("/register-pet") {
//                val userSession = call.sessions.get<UserSession>()
//                val parameters = call.receiveParameters()
//
//                // recebe os parametros
//                val name = parameters["name"] ?: return@post call.respond(HttpStatusCode.BadRequest)
//                val specie = parameters["specie"] ?: return@post call.respond(HttpStatusCode.BadRequest)
//                val age = (parameters["age"] ?: return@post call.respond(HttpStatusCode.BadRequest)).toInt()
//                val sex = Sex.valueOf(parameters["sex"] ?: return@post call.respond(HttpStatusCode.BadRequest))
//                val castrated =
//                    (parameters["castrated"] ?: return@post call.respond(HttpStatusCode.BadRequest)) == "true"
//
//                if (insertPet(
//                        Pet(
//                            id = -1,
//                            name = name,
//                            species = specie,
//                            sex = sex,
//                            age = age,
//                            castrated = castrated,
//                            photoUrl = null,
//                            owner = userSession!!.id
//                        )
//                    )
//                )
//                    call.respondRedirect("/")
//                else
//                    call.respond(HttpStatusCode.BadRequest)
//
//            }
//
//            post("/login") {
//                val parameters = call.receiveParameters()
//
//                // recebe os parametros
//                val username = parameters["username"] ?: return@post call.respond(HttpStatusCode.BadRequest)
//                val password = parameters["password"] ?: return@post call.respond(HttpStatusCode.BadRequest)
//
//                val user = queryUser(username) // procura pelo usuario
//
//                if (user == null) call.respondText("Usuário inexistente", status = HttpStatusCode.BadRequest)
//                if (pswUtil.verify(password, user!!.psw)) {
//                    call.sessions.set(UserSession(user.id, user.username))
//                    call.respondRedirect("/")
//                } else call.respondText("Senha errada", status = io.ktor.http.HttpStatusCode.BadRequest)
//            }
//
//            post("/logout") {
//                call.sessions.clear<UserSession>()
//                call.respondRedirect("/")
//            }

        }
    }
}
