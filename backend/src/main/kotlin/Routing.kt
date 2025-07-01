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
import com.jvdev.com.models.Post
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import messageHistory
import io.ktor.http.content.*
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
import kotlinx.serialization.json.*
import kotlinx.serialization.json.jsonPrimitive
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

            get("/nav") {
                if (debug) println("solicitação home")
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    if (debug) println("não esta logado")
                    call.respond(
                        HttpStatusCode.Forbidden, mapOf(
                            "username" to ""
                        )
                    )
                } else {
                    if (debug) println("esta logado")
                    val pfpUrl = "http://localhost:8080/pfps/${session.username}_pfp" // URL accessible from frontend

                    return@get call.respond(
                        HttpStatusCode.OK, mapOf(
                            "username" to session.username,
                            "pfp" to pfpUrl
                        )
                    )
                }
            }
            static("/pfps") {
                files("src/main/resources/UserPfp")
            }
            static("/postimg") {
                files("src/main/resources/PostImg")
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
                if (session == null) {
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
                    if(debug) println(petCityJson)

                    call.respond(HttpStatusCode.OK, petCityJson)
                }
            }

            get("/profilepets") {
                if (debug) println("solicitação profilepets")
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    if (debug) println("não está logado")
                    call.respond(
                        HttpStatusCode.Forbidden, mapOf(
                            "username" to ""
                        )
                    )
                } else {
                    println("Query PETs por user")
                    val petUserList = getPetByUser(session.id)
                    val petUserJson = Json.encodeToString(petUserList)
                    if(debug) println(petUserJson)

                    call.respond(HttpStatusCode.OK, petUserJson)
                }
            }

            get("/homeposts") {
                if (debug) println("solicitação homeposts")
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    if (debug) println("não está logado")
                    call.respond(
                        HttpStatusCode.Forbidden, mapOf(
                            "username" to ""
                        )
                    )
                } else {
                    println("Query Posts")
                    val postList = queryAllPosts()
                    val postJson = Json.encodeToString(postList)
                    if(debug) println(postList)

                    call.respond(HttpStatusCode.OK, postJson)
                }
            }

            get("/profileposts") {
                if (debug) println("solicitação profileposts")
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    if (debug) println("não está logado")
                    call.respond(
                        HttpStatusCode.Forbidden, mapOf(
                            "username" to ""
                        )
                    )
                } else {
                    println("Query user posts")
                    val postList = getPostByUser(session.id)
                    val postJson = Json.encodeToString(postList)
                    if(debug) println(postList)

                    call.respond(HttpStatusCode.OK, postJson)
                }
            }

            post("/poster") {
                val json = call.receive<JsonObject>()
                val userId = json["userId"]?.jsonPrimitive?.content ?: "unknown"

                val username = getUserByID(userId.toInt())?.username

                val response = buildJsonObject {
                    put("userId", userId)
                    put("username", username)
                }

                call.respond(response)
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

                val multipart = call.receiveMultipart()
                val formData = mutableMapOf<String, String>()
                var profilePictureBytes: ByteArray? = null
                var profilePictureFileName: String? = null

                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            formData[part.name ?: ""] = part.value
                        }

                        is PartData.FileItem -> {
                            if (part.name == "profilePicture") {
                                profilePictureFileName = part.originalFileName
                                profilePictureBytes = part.streamProvider().readBytes()
                            }
                        }

                        else -> {}
                    }

                    part.dispose()
                }

                if (debug) println("formData: $formData")

                val requiredFields = listOf("username", "name", "password", "email", "birthdate", "phone", "cep")
                for (field in requiredFields) {
                    if (formData[field].isNullOrBlank()) {
                        return@post call.respond(HttpStatusCode.BadRequest, mapOf("message" to "$field ausente"))
                    }
                }

                val birthdate: LocalDate = try {
                    OffsetDateTime.parse(formData["birthdate"]).toLocalDate()
                } catch (e: Exception) {
                    try {
                        LocalDate.parse(formData["birthdate"]?.substring(0, 10))
                    } catch (e: Exception) {
                        return@post call.respond(
                            HttpStatusCode.BadRequest,
                            mapOf("message" to "Data de nascimento inválida")
                        )
                    }
                }

                val user = User(
                    id = -1,
                    username = formData["username"]!!,
                    name = formData["name"]!!,
                    psw = formData["password"]!!,
                    address = buscarEndereco(formData["cep"]!!),
                    birthday = birthdate,
                    email = formData["email"]!!,
                    phone = formData["phone"]!!,
                    description = null
                )

                if (debug) println("Created user.")

                if (profilePictureBytes != null) {
                    if (debug) println("Pfp not null")
                    val filePath = "backend/src/main/resources/UserPfp/${formData["username"]}_pfp.jpg"
                    File(filePath).writeBytes(profilePictureBytes!!)
                    if (debug) println("Salvou foto em $filePath")
                }

                if (insertUser(user)) {
                    if (debug) println("registro bem sucedido")
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Sucesso"))
                } else {
                    if (debug) println("registro mal sucedido")
                    call.respond(HttpStatusCode.Conflict, mapOf("message" to "Erro ao registrar usuário"))
                }
            }

            post("/write") {
                try {
                    val time = System.currentTimeMillis()
                    val multipart = call.receiveMultipart()
                    var caption: String? = null
                    var imageFile: File? = null

                    multipart.forEachPart { part ->
                        when (part) {
                            is PartData.FormItem -> {
                                if (part.name == "caption") {
                                    caption = part.value
                                }
                            }

                            is PartData.FileItem -> {
                                if (part.name == "image") {
                                    val fileName = "${time}_post.jpg"
                                    imageFile = File("backend/src/main/resources/PostImg/$fileName")
                                    part.streamProvider().use { input ->
                                        imageFile!!.outputStream().buffered().use { output ->
                                            input.copyTo(output)
                                        }
                                    }
                                }
                            }

                            else -> {}
                        }
                        part.dispose()
                    }

                    val session = call.sessions.get<UserSession>()

                    val post = Post(
                        postID = -1,
                        ownerID = session?.id!!,
                        imgUrl = "http://localhost:8080/api/postimg/${time}_post.jpg",
                        caption = caption!!,
                        timestamp = time
                    )

                    // 4. Validate and respond

                    if (insertPost(post)) {
                        if (debug) println("postagem bem sucedida")
                        call.respond(HttpStatusCode.OK, mapOf("message" to "Sucesso"))
                    } else {
                        if (debug) println("registro mal sucedido")
                        call.respond(HttpStatusCode.Conflict, mapOf("message" to "Erro ao registrar usuário"))
                    }

                } catch (e: Exception) {
                    call.respond(mapOf("success" to false, "message" to "Error: ${e.message}"))
                }
            }


        }
    }
}
