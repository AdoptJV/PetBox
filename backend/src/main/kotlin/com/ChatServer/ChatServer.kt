package com.jvdev.com.ChatServer

import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import Message

object ChatServer {
    private val clients = mutableMapOf<String, WebSocketSession>()

    fun register(session: WebSocketSession, username: String) {
        clients[username] = session
        println("Conectado: $username")
    }

    suspend fun broadcast(message: Message) {
        val jsonMessage = Json.encodeToString(message)

        // Envia para o remetente e o destinatário
        listOfNotNull(clients[message.from], clients[message.to]).forEach { client ->
            try {
                client.send(Frame.Text(jsonMessage))
            } catch (e: Exception) {
                println("Erro ao enviar mensagem para ${message.from} ou ${message.to}: ${e.message}")
            }
        }
    }

    fun unregister(session: WebSocketSession) {
        val user = clients.entries.find { it.value == session }?.key
        if (user != null) {
            clients.remove(user)
            println("Desconectado: $user")
        }
    }
}
