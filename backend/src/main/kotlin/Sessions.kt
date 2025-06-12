package com.jvdev
import com.jvdev.com.models.UserID
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable

fun Application.configureSessions() {
    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.path = "/" // cookie disponível em todo o site
            cookie.httpOnly = false
            cookie.extensions["SameSite"] = "lax" // permite navegação com links externos
            // cookie.secure = true // ative isso se estiver usando HTTPS
        }
    }
}

@Serializable
data class UserSession(val id: UserID, val username: String)