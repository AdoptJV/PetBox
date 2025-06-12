package com.jvdev
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

            post("/login-user") {
                if (debug) println("requisição de login")

                val request = call.receive<Map<String, String>>()
                val username = request["username"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Username required")
                val password = request["password"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Password required")

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

            post("/logout"){
                call.sessions.clear<UserSession>()
                call.respond(HttpStatusCode.OK, mapOf( "message" to "Logout successful"))
            }

            post("/register-user") {
                if (debug) println("registro de usuario")
                val parameters = call.receive<Map<String, String>>()
                if (debug) println("parametros $parameters")

                val username = parameters["username"] ?: return@post call.respond(HttpStatusCode.BadRequest, mapOf("message" to "username ausente"))
                val name = parameters["name"] ?: return@post call.respond(HttpStatusCode.BadRequest, mapOf("message" to "nome ausente"))
                val password = parameters["password"] ?: return@post call.respond(HttpStatusCode.BadRequest, mapOf("message" to "senha ausente"))
                val email = parameters["email"] ?: return@post call.respond(HttpStatusCode.BadRequest, mapOf("message" to "email ausente"))
                val birthdateRaw = parameters["birthdate"] ?: return@post call.respond(HttpStatusCode.BadRequest, mapOf("message" to "data de nascimento ausente"))
                val phone = parameters["phone"] ?: return@post call.respond(HttpStatusCode.BadRequest, mapOf("message" to "telefone ausente"))
                val cep = parameters["cep"] ?: return@post call.respond(HttpStatusCode.BadRequest, mapOf("message" to "CEP ausente"))

                val birthdate: LocalDate = try {
                    OffsetDateTime.parse(birthdateRaw).toLocalDate()
                } catch (e: Exception) {
                    try {
                        LocalDate.parse(birthdateRaw.substring(0, 10))
                    } catch (e: Exception) {
                        return@post call.respond(HttpStatusCode.BadRequest, mapOf("message" to "Data de nascimento inválida"))
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
                    if (debug) println("registro bem sucedido")
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Sucesso"))
                } else {
                    if (debug) println("registro mal sucedido")
                    call.respond(HttpStatusCode.Conflict, mapOf("message" to "Erro ao registrar usuário"))
                }
            }


            /* implementa o chat usando um websocket */
            webSocket("/chat") {
                    val username = call.request.queryParameters["username"] ?: "Anon"

                    ChatServer.register(this, username)

                    try {
                        for (frame in incoming) {
                            if (frame is Frame.Text) {
                                val text = frame.readText()
                                ChatServer.broadcast("$username: $text")
                            }
                        }
                    } finally {
                        ChatServer.unregister(this)
                    }
                }
            }

    }
}