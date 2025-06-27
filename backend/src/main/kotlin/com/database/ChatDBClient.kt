package com.jvdev.com.database

import Message
import java.sql.SQLException

fun saveMessage(message: Message) {
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")
    try {
        // Buscar ID do remetente (from)
        val stmtFrom = connection.prepareStatement("SELECT id FROM USERS WHERE username = ?")
        stmtFrom.setString(1, message.from)
        val rsFrom = stmtFrom.executeQuery()
        if (!rsFrom.next()) {
            throw SQLException("Usuário remetente '${message.from}' não encontrado.")
        }
        val fromId = rsFrom.getInt("id")

        // Buscar ID do destinatário (to)
        val stmtTo = connection.prepareStatement("SELECT id FROM USERS WHERE username = ?")
        stmtTo.setString(1, message.to)
        val rsTo = stmtTo.executeQuery()
        if (!rsTo.next()) {
            throw SQLException("Usuário destinatário '${message.to}' não encontrado.")
        }
        val toId = rsTo.getInt("id")

        // Inserir a mensagem
        val insertStmt = connection.prepareStatement(
            """
            INSERT INTO MESSAGES (from_user_id, to_user_id, content)
            VALUES (?, ?, ?)
            """
        )
        insertStmt.setInt(1, fromId)
        insertStmt.setInt(2, toId)
        insertStmt.setString(3, message.content)
        insertStmt.executeUpdate()
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        connection.close()
    }
}

fun getMessages(from: String, to: String): List<Message> {
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")
    val list = ArrayList<Message>()

    try {
        val stmtFrom = connection.prepareStatement("SELECT id FROM USERS WHERE username = ?")
        stmtFrom.setString(1, from)
        val rsFrom = stmtFrom.executeQuery()
        if (!rsFrom.next()) throw SQLException("Usuário remetente '$from' não encontrado.")
        val fromId = rsFrom.getInt("id")

        val stmtTo = connection.prepareStatement("SELECT id FROM USERS WHERE username = ?")
        stmtTo.setString(1, to)
        val rsTo = stmtTo.executeQuery()
        if (!rsTo.next()) throw SQLException("Usuário destinatário '$to' não encontrado.")
        val toId = rsTo.getInt("id")

        val getsmt = connection.prepareStatement(
            """
            SELECT * FROM MESSAGES 
            WHERE (from_user_id = ? AND to_user_id = ?) 
               OR (from_user_id = ? AND to_user_id = ?)
            ORDER BY timestamp ASC
            """
        )
        getsmt.setInt(1, fromId)
        getsmt.setInt(2, toId)
        getsmt.setInt(3, toId)
        getsmt.setInt(4, fromId)

        val rs = getsmt.executeQuery()
        while (rs.next()) {
            val content = rs.getString("content")
            val fromUserId = rs.getInt("from_user_id")
            val timestamp = rs.getTimestamp("timestamp").time

            val msg = if (fromUserId == fromId) {
                Message(from = from, to = to, content = content, timestamp = timestamp)
            } else {
                Message(from = to, to = from, content = content, timestamp = timestamp)
            }

            list.add(msg)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        connection.close()
    }

    return list
}