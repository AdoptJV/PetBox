package com.jvdev.com.ChatServer

import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.util.*

object ChatServer {

    // Lista de conexões WebSocket ativas
    private val clients: MutableSet<DefaultWebSocketServerSession> =
        Collections.synchronizedSet(mutableSetOf<DefaultWebSocketServerSession>())


    suspend fun register(session: DefaultWebSocketServerSession, username: String) {
        clients.add(session)
        broadcast("$username entrou no chat")
    }

    fun unregister(session: DefaultWebSocketServerSession) {
        clients.remove(session)
    }

    suspend fun broadcast(message: String) {
        clients.forEach {
            it.send(Frame.Text(message)) // envia para cada usuário conectado }
        }
    }
}