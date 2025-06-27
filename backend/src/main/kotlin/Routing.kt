package com.jvdev
import Message
import com.jvdev.com.ChatServer.ChatServer
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
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import messageHistory
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

            get("/all-users") {
                val exclude = call.request.queryParameters["exclude"]
                val all = getAllUsers()
                val filtered = if (exclude != null) all.filter { it != exclude } else all
                call.respond(filtered)
            }


            get("/check/user") {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    if (debug) println("não esta logado")
                    call.respond(HttpStatusCode.Forbidden)
                } else {
                    if (debug) println("esta logado")
                    call.respond(HttpStatusCode.OK)
                }
            }

            get("/messages") {
                val from = call.request.queryParameters["from"]
                val to = call.request.queryParameters["to"]

                if (from == null || to == null) {
                    call.respond(HttpStatusCode.BadRequest, "Parâmetros 'from' e 'to' são obrigatórios.")
                    return@get
                }

                val history = getMessages(from, to)

                call.respond(history)
            }

            webSocket("/chat") {
                val username = call.request.queryParameters["username"]
                if (username == null) {
                    close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Nome de usuário ausente."))
                    return@webSocket
                }

                ChatServer.register(this, username)

                try {
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            val text = frame.readText()
                            val message = Json.decodeFromString<Message>(text)

                            // Salva no histórico em memória (ou banco se quiser)
                            saveMessage(message)

                            // Envia para ambos os envolvidos
                            ChatServer.broadcast(message)
                        }
                    }
                } catch (e: Exception) {
                    println("Erro WebSocket: ${e.message}")
                } finally {
                    ChatServer.unregister(this)
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
                } catch (e: Exception) {
                    if (debug) println("erro ao obter o endereço")
                    call.respond(HttpStatusCode.InternalServerError, "Failed to retrieve address")
                }
            }


            get("/check/username/{username}") {
                if (debug) println("verificação de usuario")
                val username = call.parameters["username"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "Invalid username"
                )
                if (debug) println("usuario solicitado: $username")
                val check = checkUsername(username)
                if (debug) println("já existe? $check")
                call.respond(mapOf("exists" to check))
            }

            get("/check/email/{email}") {
                if (debug) println("verificação de email")
                val email =
                    call.parameters["email"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid username")
                if (debug) println("email solicitado: $email")
                val check = checkEmail(email)
                if (debug) println("já existe? $check")
                call.respond(mapOf("exists" to check))
            }

            /* protocolos de post */
            post("/login-user") {
                if (debug) println("requisição de login")

                val request = call.receive<Map<String, String>>()
                val username =
                    request["username"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Username required")
                val password =
                    request["password"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Password required")

                val user = getUserByUsername(username)

                if (user == null) {
                    if (debug) println("Usuário não encontrado")
                    return@post call.respond(HttpStatusCode.NotFound, mapOf("message" to "Usuário inexistente"))
                }

                if (!pswUtil.verify(password, user.psw)) {
                    if (debug) println("Senha incorreta")
                    return@post call.respond(HttpStatusCode.Unauthorized, mapOf("message" to "Credenciais inválidas"))
                }

                // Login OK
                call.sessions.set(UserSession(user.id, user.username))
                if (debug) println("Login OK")
                call.respond(HttpStatusCode.OK, mapOf("message" to "Login realizado com sucesso"))
            }

            post("/logout") {
                call.sessions.clear<UserSession>()
                call.respond(HttpStatusCode.OK, mapOf("message" to "Logout successful"))
            }

            post("/register-user") {
                if (debug) println("registro de usuario")
                val parameters = call.receive<Map<String, String>>()
                if (debug) println("parametros $parameters")

                val username = parameters["username"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("message" to "username ausente")
                )
                val name = parameters["name"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("message" to "nome ausente")
                )
                val password = parameters["password"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("message" to "senha ausente")
                )
                val email = parameters["email"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("message" to "email ausente")
                )
                val birthdateRaw = parameters["birthdate"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("message" to "data de nascimento ausente")
                )
                val phone = parameters["phone"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("message" to "telefone ausente")
                )
                val cep = parameters["cep"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("message" to "CEP ausente")
                )

                // converte a birthdate para o formato da database
                val birthdate: LocalDate = try {
                    OffsetDateTime.parse(birthdateRaw).toLocalDate()
                } catch (e: Exception) {
                    try {
                        LocalDate.parse(birthdateRaw.substring(0, 10))
                    } catch (e: Exception) {
                        return@post call.respond(
                            HttpStatusCode.BadRequest,
                            mapOf("message" to "Data de nascimento inválida")
                        )
                    }
                }

                val user = User(
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

                if (insertUser(user)) {
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
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Sucesso"))
                } else {
                    if (debug) println("registro mal sucedido")
                    call.respond(HttpStatusCode.Conflict, mapOf("message" to "Erro ao registrar usuário"))
                }
            }

        }
    }
}