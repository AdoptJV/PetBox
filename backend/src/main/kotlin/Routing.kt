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
import io.ktor.http.content.*
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.OffsetDateTime
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.readByteArray
import java.io.FileOutputStream
import kotlin.io.use

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

            get("/homepets") {
                if (debug) println("solicitação homepets")
                val session = call.sessions.get<UserSession>()
                if(session == null) {
                    if (debug) println("não está logado")
                    call.respond(
                        HttpStatusCode.Forbidden, mapOf(
                            "username" to ""
                        )
                    )
                } else {
                    val uid = session.id
                    val user = getUserByID(uid)
                    val city = user?.address?.localidade

                    println("Query PETs por cidade")
                    val petCityList = getPetByCity(city)
                    val petCityJson = Json.encodeToString(petCityList)
                    println(petCityJson)

                    call.respond(HttpStatusCode.OK, petCityJson)
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
                            description = null,
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