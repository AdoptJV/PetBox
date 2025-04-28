package com.jvdev

import com.jvdev.com.models.UserID
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable

fun Application.configureSessions() {
    install(Sessions) {
        cookie<UserSession>("USER_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
}

@Serializable
data class UserSession(val id: UserID, val username: String)