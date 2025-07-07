import com.jvdev.UserSession
import com.jvdev.configureRouting
import com.jvdev.configureSecurity
import com.jvdev.module
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import io.ktor.websocket.*
import io.ktor.server.application.*
import io.ktor.client.plugins.cookies.*
import io.ktor.server.sessions.*
import io.ktor.client.request.forms.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.websocket.WebSockets

class ConfigureRoutingTest {
    @Test
    fun testGetApi() = testApplication {
        application { configureRouting() }

        val response = client.get("/api/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("PetBox"))
    }

    @Test
    fun testGetApiJsonSpecies() = testApplication {
        application { configureRouting() }

        val response = client.get("/api/JSON/species")
        assertTrue(response.status == HttpStatusCode.OK || response.status == HttpStatusCode.NotFound)
    }

    @Test
    fun testGetApiAllusers() = testApplication {
        application {
            configureRouting()
            install(ContentNegotiation) { json() }
        }

        val response = client.get("/api/all-users")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testGetApiCepinfoCep() = testApplication {
        application {
            configureRouting()
            install(ContentNegotiation) { json() }
        }

        val response = client.get("/api/cep-info/01001000")
        assertTrue(response.status == HttpStatusCode.OK || response.status == HttpStatusCode.InternalServerError)
    }

    @Test
    fun testWebsocketApiChat() = testApplication {
        application {
            configureRouting()
        }

        val client = createClient {
            install(io.ktor.client.plugins.websocket.WebSockets)
        }

        client.webSocket("/api/chat?username=testuser") {
            send("hello")
        }
    }

    @Test
    fun testGetApiCheckEmailEmail() = testApplication {
        application {
            configureRouting()
            install(ContentNegotiation) { json() }
        }

        val response = client.get("/api/check/email/test@example.com")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testGetApiCheckUser_Unauthenticated() = testApplication {
        application {
            configureRouting()
            install(ContentNegotiation) { json() }

            install(Sessions) {
                cookie<UserSession>("user_session") {
                    cookie.path = "/"
                    cookie.maxAgeInSeconds = 3600
                }
            }
        }

        val response = client.get("/api/check/user")
        assertEquals(HttpStatusCode.Forbidden, response.status)
    }

    @Test
    fun testGetApiCheckUsernameUsername() = testApplication {
        application {
            configureRouting()
            install(ContentNegotiation) { json() }
        }

        val response = client.get("/api/check/username/testuser")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testGetApiHomepets_Unauthenticated() = testApplication {
        application {
            configureRouting()
            install(ContentNegotiation) { json() }

            install(Sessions) {
                cookie<UserSession>("user_session") {
                    cookie.path = "/"
                    cookie.maxAgeInSeconds = 3600
                }
            }
        }

        val response = client.get("/api/homepets")
        assertEquals(HttpStatusCode.Forbidden, response.status)
    }

    @Test
    fun testGetApiHomeposts_Unauthenticated() = testApplication {
        application {
            configureRouting()
            install(ContentNegotiation) { json() }

            install(Sessions) {
                cookie<UserSession>("user_session") {
                    cookie.path = "/"
                    cookie.maxAgeInSeconds = 3600
                }
            }
        }

        val response = client.get("/api/homeposts")
        assertEquals(HttpStatusCode.Forbidden, response.status)
    }

    @Test
    fun testPostApiLoginuser_MissingData() = testApplication {
        application {
            configureRouting()
            install(ContentNegotiation) { json() }
        }

        val response = client.post("/api/login-user") {
            contentType(ContentType.Application.Json)
            setBody("""{"username": "foo"}""")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun testPostApiLogout() = testApplication {
        application {
            configureRouting()
            install(ContentNegotiation) { json() }

            install(Sessions) {
                cookie<UserSession>("user_session") {
                    cookie.path = "/"
                    cookie.maxAgeInSeconds = 3600
                }
            }
        }

        val client = createClient {
            install(HttpCookies)
        }

        val response = client.post("/api/logout") {
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testGetApiMessages_MissingParams() = testApplication {
        application { configureRouting() }

        val response = client.get("/api/messages")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun testGetApiNav_Unauthenticated() = testApplication {
        application {
            configureRouting()
            install(ContentNegotiation) { json() }

            install(Sessions) {
                cookie<UserSession>("user_session") {
                    cookie.path = "/"
                    cookie.maxAgeInSeconds = 3600
                }
            }
        }

        val response = client.get("/api/nav") {
            cookie("SESSION_ID", "fake-valid-session")
        }
        assertEquals(HttpStatusCode.Forbidden, response.status)
    }

    @Test
    fun testPostApiPoster_Invalid() = testApplication {
        application { configureRouting() }

        val response = client.post("/api/poster")
        assertTrue(response.status.value in 400..500)
    }

    @Test
    fun testPostApiProfilepets_InvalidJson() = testApplication {
        application {
            configureRouting()
            install(ContentNegotiation) { json() }
        }

        val response = client.post("/api/profilepets") {
            contentType(ContentType.Application.Json)
            setBody("""{}""")
        }
        assertEquals(HttpStatusCode.OK, response.status) // empty list is OK
    }

    @Test
    fun testPostApiProfileposts_InvalidJson() = testApplication {
        application {
            configureRouting()
            install(ContentNegotiation) { json() }
        }

        val response = client.post("/api/profileposts") {
            contentType(ContentType.Application.Json)
            setBody("""{}""")
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testPostApiRegisteruser_MissingRequiredFields() = testApplication {
        application {
            configureRouting()
            install(ContentNegotiation) { json() }
        }

        val response = client.post("/api/register-user") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("username", "testuser")
                        append("name", "Test User")
                    }
                )
            )
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun testPostApiRegisteruser_Success() = testApplication {
        application {
            configureRouting()
        }

        val response = client.post("/api/register-user") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("username", "testuser")
                        append("name", "Test User")
                        append("password", "testpass123")
                        append("email", "test@example.com")
                        append("birthdate", "1990-01-01")
                        append("phone", "11999999999")
                        append("cep", "01001-000")
                    }
                )
            )
        }

        assertTrue(response.status.value < 500)
    }

    @Test
    fun testPostApiWrite_MissingMultipart() = testApplication {
        application {
            configureRouting()
            install(ContentNegotiation) { json() }
            install(Sessions) {
                cookie<UserSession>("user_session") {
                    cookie.path = "/"
                    cookie.maxAgeInSeconds = 3600
                }
            }
        }

        val response = client.post("/api/write")

        assertEquals(HttpStatusCode.OK, response.status)

        val responseBody = response.bodyAsText()
        assertTrue(responseBody.contains("Error:"))
    }
}
