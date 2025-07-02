import com.jvdev.module
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlin.test.Test

class ConfigureRoutingTest {
    /*
    @Test
    fun testGetApi() = testApplication {
        application {
            TODO("Add the Ktor module for the test")
        }
        client.get("/api").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetApiJsonSpecies() = testApplication {
        application {
            TODO("Add the Ktor module for the test")
        }
        client.get("/api/JSON/species").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetApiAllusers() = testApplication {
        application {
            TODO("Add the Ktor module for the test")
        }
        client.get("/api/all-users").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetApiCepinfoCep() = testApplication {
        application {
            TODO("Add the Ktor module for the test")
        }
        client.get("/api/cep-info/{cep}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testWebsocketApiChat() = testApplication {
        application {
            TODO("Add the Ktor module for the test")
        }
        val client = createClient {
            install(WebSockets)
        }
        client.webSocket("/api/chat") {
            TODO("Please write your test here")
        }
    }
    */

    @Test
    fun testGetApiCheckEmailEmail() = testApplication {
        application {
            module()
        }
        client.get("/api/check/email/{email}").apply {
            val testEmail = "teste@gmail.com"
            val response = client.get("/api/check/email/$testEmail")
            assertEquals(HttpStatusCode.OK, response.status)
            val responseText = response.bodyAsText()
            println("Resposta: $responseText")
            assertTrue("valido" in responseText || "exists" in responseText)
        }
    }

    /*
    @Test
    fun testGetApiCheckUser() = testApplication {
        application {
            TODO("Add the Ktor module for the test")
        }
        client.get("/api/check/user").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetApiCheckUsernameUsername() = testApplication {
        application {
            TODO("Add the Ktor module for the test")
        }
        client.get("/api/check/username/{username}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetApiHomepets() = testApplication {
        application {
            TODO("Add the Ktor module for the test")
        }
        client.get("/api/homepets").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetApiHomeposts() = testApplication {
        application {
            TODO("Add the Ktor module for the test")
        }
        client.get("/api/homeposts").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostApiLoginuser() = testApplication {
        application {
            TODO("Add the Ktor module for the test")
        }
        client.post("/api/login-user").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostApiLogout() = testApplication {
        application {
            TODO("Add the Ktor module for the test")
        }
        client.post("/api/logout").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetApiMessages() = testApplication {
        application {
            TODO("Add the Ktor module for the test")
        }
        client.get("/api/messages").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetApiNav() = testApplication {
        application {
            TODO("Add the Ktor module for the test")
        }
        client.get("/api/nav").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostApiPoster() = testApplication {
        application {
            TODO("Add the Ktor module for the test")
        }
        client.post("/api/poster").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetApiProfilepets() = testApplication {
        application {
            TODO("Add the Ktor module for the test")
        }
        client.get("/api/profilepets").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetApiProfileposts() = testApplication {
        application {
            TODO("Add the Ktor module for the test")
        }
        client.get("/api/profileposts").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostApiRegisteruser() = testApplication {
        application {
            TODO("Add the Ktor module for the test")
        }
        client.post("/api/register-user").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostApiWrite() = testApplication {
        application {
            TODO("Add the Ktor module for the test")
        }
        client.post("/api/write").apply {
            TODO("Please write your test here")
        }
    }
     */
}