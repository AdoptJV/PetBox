package com.jvdev

import Message
import PetDto
import com.jvdev.com.ChatServer.ChatServer
import com.jvdev.com.cep.Endereco
import com.jvdev.com.cep.buscarEndereco
import com.jvdev.com.cep.externalApiAvailable
import com.jvdev.com.database.*
import com.jvdev.com.encryption.pswUtil
import com.jvdev.com.models.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.*
import java.io.File
import java.time.LocalDate
import java.time.OffsetDateTime

const val debug = true

fun Application.configureRouting() {
    val root = File(System.getProperty("user.dir")) // define o caminho até a raiz do projeto
    if (debug) println("raiz: $root")
    install(WebSockets)
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
                            "username" to "",
                            "userId" to "",
                            "pfp" to ""
                        )
                    )
                } else {
                    if (debug) println("esta logado")
                    val pfpUrl = "http://localhost:8080/pfps/${session.username}_pfp" // URL accessible from frontend

                    return@get call.respond(
                        HttpStatusCode.OK, mapOf(
                            "username" to session.username,
                            "userId" to session.id.toString(),
                            "pfp" to pfpUrl
                        )
                    )
                }
            }

            static("/pfps") {
                files("src/main/resources/UserPfp")
            }
            static("/PostImg") {
                files("src/main/resources/PostImg")
            }
            static("/petimg") {
                files("src/main/resources/PetPfp")
            }

            get("/pets/{id}") {
                val idParam = call.parameters["id"]
                val petId = idParam?.toIntOrNull()
                if (petId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID inválido"))
                    return@get
                }

                val pet = getPetByID(petId)
                if (pet == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Pet não encontrado"))
                    return@get
                }

                val ownerUser = getUserByID(pet.owner)
                if (ownerUser == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Dono do pet não encontrado"))
                    return@get
                }

                val dto = PetDto(
                    id = pet.id,
                    name = pet.name,
                    species = pet.species,
                    sex = if (pet.sex == Sex.MALE) "Macho" else "Fêmea",
                    age = pet.age,
                    castrated = pet.castrated,
                    photoUrl = pet.photoUrl.orEmpty(),
                    description = pet.description.orEmpty(),
                    owner = ownerUser.username,
                    ownerID = pet.owner
                )

                print("DATA:::")
                print(dto)

                call.respond(HttpStatusCode.OK, dto)
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

                    println(uid)
                    println(user)
                    println(city)

                    println("Query PETs por cidade")
                    val petCityList = getPetByCity(city)

                    println(petCityList)
                    val petCityJson = Json.encodeToString(petCityList)
                    if (debug) println(petCityJson)

                    call.respond(HttpStatusCode.OK, petCityJson)
                }
            }

            post("/profilepets") {
                if (debug) println("solicitação profilepets")

                val json = call.receive<JsonObject>()
                val username = json["username"]?.jsonPrimitive?.content ?: "unknown"
                val userId = getIDbyUsername(username)

                println("Query PETs por user")
                val petUserList = getPetByUser(userId)
                println(petUserList)
                val petUserJson = Json.encodeToString(petUserList)
                if (debug) println(petUserJson)

                call.respond(HttpStatusCode.OK, petUserJson)
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
                    if (debug) println(postList)

                    call.respond(HttpStatusCode.OK, postJson)
                }
            }

            post("/profileposts") {
                if (debug) println("solicitação profileposts")

                val json = call.receive<JsonObject>()
                val username = json["username"]?.jsonPrimitive?.content ?: "unknown"
                val userId = getIDbyUsername(username)

                println("Query user posts")
                val postList = getPostByUser(userId)
                val postJson = Json.encodeToString(postList)
                if (debug) println(postList)

                call.respond(HttpStatusCode.OK, postJson)
            }

            post("/getuser") {
                if (debug) println("getuser request")
                val json = call.receive<JsonObject>()
                val userId = json["userId"]?.jsonPrimitive?.content ?: "unknown"

                val username = getUserByID(userId.toInt())?.username

                val response = buildJsonObject {
                    put("userId", userId)
                    put("username", username)
                }
                if (debug) println(response)
                call.respond(response)
            }

            post("/comments") {
                val json = call.receive<JsonObject>()
                val postID = json["postID"]?.jsonPrimitive?.intOrNull

                if (postID == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid or missing postID"))
                    return@post
                }

                val comments: List<Comment> = getCommentByPost(postID) ?: emptyList()
                if (debug) println(comments.isEmpty().toString() + " " + comments)

                call.respond(HttpStatusCode.OK, comments)
            }

            post("/writecomment") {
                try {
                    val time = System.currentTimeMillis()
                    println("1")
                    val json = call.receive<JsonObject>()
                    println("2")
                    val commentText = json["comment"]?.jsonPrimitive?.contentOrNull
                    val post = json["post"]?.jsonPrimitive?.intOrNull
                    val session = call.sessions.get<UserSession>()

                    if (debug) println("Oi comentário")

                    val comment = Comment(
                        id = -1,
                        post = post!!,
                        user = session?.id!!,
                        text = commentText!!,
                        timestamp = time
                    )

                    if (insertComment(comment)) {
                        if (debug) println("comentário bem sucedido")
                        call.respond(HttpStatusCode.OK, mapOf("message" to "Sucesso"))
                    } else {
                        if (debug) println("comentário mal sucedido")
                        call.respond(HttpStatusCode.Conflict, mapOf("message" to "Erro ao comentar"))
                    }

                } catch (e: Exception) {
                    if (debug) println("Deu pau")
                    call.respond(mapOf("message" to "Error: ${e.message}"))
                }
            }

            post("/filter") {
                if (debug) println("Solicitação de busca")
                val session = call.sessions.get<UserSession>()
                if (debug) println("1")
                val filters = call.receive<Map<String?, String?>>()
                if (debug) println("2")

                if (session == null) {
                    if (debug) println("Não está logado")
                    call.respond(
                        HttpStatusCode.Forbidden,
                        mapOf("username" to "")
                    )
                } else {
                    if (debug) println("Filtros recebidos: $filters")
                    val petList = getPetByFilters(filters)
                    val petJson = Json.encodeToString(petList)
                    call.respond(HttpStatusCode.OK, petJson)
                }
            }


            get("/cep-info/{cep}") {
                if (debug) println("processamento de cep")
                val cep = call.parameters["cep"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid CEP")
                if (debug) println("cep informado: $cep")
                try {
                    val mock = Endereco(
                        cep = "00000000",
                        logradouro = "API fora do ar",
                        complemento = "123",
                        bairro = "API fora do ar",
                        localidade = "API fora do ar",
                        uf = "API fora do ar",
                        ibge = "?",
                        gia = "?",
                        ddd = "00",
                        siafi = "?"
                    )
                    val address = if (externalApiAvailable.get()) buscarEndereco(cep) else mock
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
                println("Before")
                val user = getUserByUsername(username)
                println("After")

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

                val mock = Endereco(
                    cep = formData["cep"]!!,
                    logradouro = "API fora do ar",
                    complemento = "123",
                    bairro = "API fora do ar",
                    localidade = "API fora do ar",
                    uf = "API fora do ar",
                    ibge = "?",
                    gia = "?",
                    ddd = "00",
                    siafi = "?"
                )

                val endereco = if (externalApiAvailable.get()) buscarEndereco(formData["cep"]!!) else mock
                if (endereco != mock) insertAddress(endereco)

                val user = User(
                    id = -1,
                    username = formData["username"]!!,
                    name = formData["name"]!!,
                    psw = formData["password"]!!,
                    address = endereco,
                    birthday = birthdate,
                    email = formData["email"]!!,
                    phone = formData["phone"]!!,
                    description = null
                )

                if (debug) println("Created user $user.")

                if (profilePictureBytes != null) {
                    if (debug) println("Pfp not null")
                    val filePath = "src/main/resources/UserPfp/${formData["username"]}_pfp.jpg"
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
                                if (debug) println("form")
                                if (part.name == "caption") {
                                    caption = part.value
                                }
                            }

                            is PartData.FileItem -> {
                                if (part.name == "image") {
                                    val fileName = "${time}_post.jpg"
                                    imageFile = File("src/main/resources/PostImg/$fileName")
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
                    if (debug) println("7")

                    val session = call.sessions.get<UserSession>()

                    val post = Post(
                        postID = -1,
                        ownerID = session?.id!!,
                        imgUrl = "http://localhost:8080/api/PostImg/${time}_post.jpg",
                        caption = caption!!,
                        timestamp = time
                    )
                    if (insertPost(post)) {
                        if (debug) println("postagem bem sucedida")
                        call.respond(HttpStatusCode.OK, mapOf("message" to "Sucesso"))
                    } else {
                        if (debug) println("registro mal sucedido")
                        call.respond(HttpStatusCode.Conflict, mapOf("message" to "Erro ao registrar usuário"))
                    }

                } catch (e: Exception) {
                    call.respond(mapOf("message" to "Error: ${e.message}"))
                }
            }
            get("/JSON/species") {
                if (debug) println("Solicitação do JSON de espécies")

                val file = File("src/main/resources/PetData/species.json")
                if (file.exists()) {
                    val jsonContent = file.readText()
                    call.respondText(jsonContent, ContentType.Application.Json)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Arquivo species.json não encontrado")
                }
            }
            post("/register-pet") {
                if (debug) println("registro de pet")
                val multipart = call.receiveMultipart()
                val formData = mutableMapOf<String, String>()
                var petPictureBytes: ByteArray? = null
                var petPictureFileName: String? = null

                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            formData[part.name ?: ""] = part.value
                        }

                        is PartData.FileItem -> {
                            if (part.name == "profilePicture") {
                                petPictureFileName = part.originalFileName
                                petPictureBytes = part.streamProvider().readBytes()
                            }
                        }

                        else -> {}
                    }
                    part.dispose()
                }
                val requiredFields = listOf("name", "age", "sex", "specie", "castrated")
                for (field in requiredFields) {
                    if (formData[field].isNullOrBlank()) {
                        return@post call.respond(HttpStatusCode.BadRequest, mapOf("message" to "$field ausente"))
                    }
                }

                // Extrair e converter os dados
                val name = formData["name"]!!
                val age = formData["age"]!!.toIntOrNull() ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("message" to "age inválido")
                )
                val sex = formData["sex"]!!  // depende de como você está enviando (ex: "M"/"F" ou "true"/"false")
                val specie = formData["specie"]!!
                val castrated = formData["castrated"]!!.toBooleanStrictOrNull() ?: false
                val description = formData["description"] ?: null

                val pet = Pet(
                    id = -1,  // se for autogerado no banco
                    name = name,
                    age = age,
                    sex = Sex.valueOf(sex),
                    species = specie,
                    castrated = castrated,
                    owner = call.sessions.get<UserSession>()?.id ?: -1,
                    photoUrl = null,
                    description = description.toString()
                )
                if (debug) {
                    println(pet)
                }
                val success = insertPet(pet)
                val id = getPetID(name, call.sessions.get<UserSession>()?.id ?: -1, specie)

                if (petPictureBytes != null) {
                    val username = call.sessions.get<UserSession>()?.username ?: "unknown"
                    val filePath = "src/main/resources/PetPfp/${id}_pfp.jpg"
                    File(filePath).writeBytes(petPictureBytes!!)
                    val photoUrl = "http://localhost:8080/pfps/${id}_pfp.jpg"
                    updatePetPhotoUrl(id ?: -1, photoUrl)
                }

                if (success) {
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Pet registrado com sucesso"))
                } else {
                    call.respond(HttpStatusCode.Conflict, mapOf("message" to "Erro ao registrar pet"))
                }
            }

        }
    }
}
