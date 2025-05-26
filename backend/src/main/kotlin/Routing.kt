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
import com.jvdev.com.models.Pet
import com.jvdev.com.models.Sex
import com.jvdev.com.models.User
import io.ktor.server.auth.*
import io.ktor.server.freemarker.*
import java.time.LocalDate
import java.time.OffsetDateTime


const val debug = true

fun Application.configureRouting() {
    val root = File(System.getProperty("user.dir")) // define o caminho até a raiz do projeto
    if (debug) println("raiz: $root")
    routing {
        singlePageApplication {
            react("/react-frontend")
        }
        route("/api") {
            staticFiles("/", File("$root/frontend")) // path estatico

            /* protocolos de get */
            get("/") {
                if (debug) println("servidor inicializado")
                call.respondText("O servidor do PetBox está rodando!")
            }

            get("/home") {
                if (debug) println("solicitação home")
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    if (debug) println("não esta logado")
                    call.respond(HttpStatusCode.Forbidden, mapOf(
                        "username" to ""
                    ) )
                } else {
                    if (debug) println("esta logado")
                    call.respond(HttpStatusCode.OK, mapOf(
                        "username" to session.username
                    ))
                }
            }

            get("/cep-info/{cep}") {
                if (debug) println("processamento de cep")
                val cep = call.parameters["cep"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid CEP")
                if (debug) println("cep informado: $cep")
                try {
                    val address = buscarEndereco(cep)
                    if (debug) println("endereco obtido: $address")
                    call.respond(address)
                }
                catch (e: Exception) {
                    if (debug) println("erro ao obter o endereço")
                    call.respond(HttpStatusCode.InternalServerError, "Failed to retrieve address")
                }
            }


            get("/check/username/{username}"){
                if (debug) println("verificação de usuario")
                val username = call.parameters["username"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid username")
                if (debug) println("usuario solicitado: $username")
                val check = checkUsername(username)
                if (debug) println("já existe? $check")
                call.respond(mapOf("exists" to check))
            }

            get("/check/email/{email}") {
                if (debug) println("verificação de email")
                val email = call.parameters["email"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid username")
                if (debug) println("email solicitado: $email")
                val check = checkEmail(email)
                if (debug) println("já existe? $check")
                call.respond(mapOf("exists" to check))
            }

            get("/check/user") {
                val userSession = call.sessions.get<UserSession>()
                if (userSession == null) call.respond(HttpStatusCode.Unauthorized)
                call.respond(HttpStatusCode.OK)
            }

            /* protocolos de post */
            post("/login-user") {
                if (debug) println("requisição de login")

                val request = call.receive<Map<String, String>>()
                if (debug) println("informações de login: $request")

                val username = request["username"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val password = request["password"] ?: return@post call.respond(HttpStatusCode.BadRequest)


                val user = getUserByUsername(username)

                if (user == null) {
                    if (debug) println("usuario inexistente")
                    call.respond(mapOf(
                        "redirect" to "/login?error=1",
                        "message" to "Invalid credentials"
                    ))
                }

                if (pswUtil.verify(password, user!!.psw)) {
                    if (debug) println("verificação de usuario bem sucedida")
                    call.sessions.set(UserSession(user.id, user.username))
                    call.respond(mapOf(
                        "redirect" to "/home",
                        "message" to "Login successful"
                    ))
                }
                else {
                    if (debug) println("verificação de usuario mal sucedida")
                    call.respond(mapOf(
                        "redirect" to "/login?error=2",
                        "message" to "Invalid credentials"
                    ))
                }
            }

            post("/logout"){
                call.sessions.clear<UserSession>()
                call.respond(mapOf(
                    "redirect" to "/login",
                    "message" to "Logout successful"
                ))
            }

            post("/register-user") {
                if (debug) println("registro de usuario")
                val parameters = call.receive<Map<String, String>>()
                if (debug) println("parametros $parameters")

                val username = parameters["username"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val name = parameters["name"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val password = parameters["password"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val email = parameters["email"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val birthdateRaw = parameters["birthdate"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val phone = parameters["phone"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val cep = parameters["cep"] ?: return@post call.respond(HttpStatusCode.BadRequest)

                // converte a birthdate para o formato da database
                val birthdate: LocalDate = try {
                    OffsetDateTime.parse(birthdateRaw).toLocalDate()
                }
                catch (e: Exception) {
                    LocalDate.parse(birthdateRaw.substring(0, 10))
                }

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
                ) {
                    if (debug) println("registro bem sucedido")
                    call.respond(
                        mapOf(
                            "redirect" to "/login",
                            "message" to "success"
                        )
                    )
                }
                else {
                    if (debug) println("registro mal sucedido")
                    call.respond(mapOf(
                        "redirect" to "/register?error=1",
                        "message" to "failure"))
                }
            }
            authenticate("auth-session") {
                post("/register-pet") {
                    if (debug) println("registro de usuario")
                    val parameters = call.receive<Map<String, String>>()
                    if (debug) println("parametros $parameters")

                    val session =
                        call.sessions.get<UserSession>() ?: return@post call.respond(HttpStatusCode.BadRequest)

                    println("usuario:$session")

                    val name = parameters["name"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                    val sex = parameters["sex"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                    val castrated = parameters["castrated"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                    val specie = parameters["specie"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                    val race = parameters["race"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                    val age = parameters["age"] ?: return@post call.respond(HttpStatusCode.BadRequest)

                    val species = "$specie:$race"

                    if (insertPet(
                            Pet(
                                id = -1,
                                name = name,
                                sex = Sex.valueOf(sex),
                                castrated = castrated.equals("true", ignoreCase = true),
                                age = Integer.valueOf(age),
                                species = species,
                                photoUrl = null,
                                owner = session.id
                            )
                        )
                    ) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        if (debug) println("erro ao inserir pet")
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }

                get("/JSON/species") {
                    if (debug) println("Solicitação json species")

                    val inputStream = javaClass.getResourceAsStream("/PetData/species.json")
                    val jsonSpecies = inputStream?.bufferedReader()?.readText()

                    if (jsonSpecies != null) {
                        if (debug) println(jsonSpecies)
                        call.respondText(jsonSpecies, ContentType.Application.Json)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Not found")
                    }
                }
            }
        }
    }
}