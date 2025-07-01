package com.jvdev.com.database
import com.jvdev.com.models.Post
import com.jvdev.com.models.Comment
import com.jvdev.com.models.User
import java.sql.Date
import java.sql.SQLException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/*
 * Funções relacionadas a manipulação de dados na base da dados dos posts
 */

fun insertComment(comment: Comment): Boolean {
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")
    try {
        val sql = """
        INSERT INTO COMMENT
        (post, user, text, timestamp) 
        VALUES (?, ?, ?, ?)
        """.trimIndent()

        val smtm = connection.prepareStatement(sql) ?: throw SQLException("Failed to execute SQL statement.")
        smtm.setInt(1, comment.post)
        smtm.setInt(2, comment.user)
        smtm.setString(3, comment.text)
        smtm.setLong(4, comment.timestamp)

        return smtm.executeUpdate() == 1 // verifica se uma linha foi atualizada no banco de dados
    }
    catch (e: SQLException) {
        e.printStackTrace()
        return false
    }
    finally {
        connection.close()
    }
}

fun queryAllComments(): List<Comment> {
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")
    val sql = "SELECT * FROM COMMENTS ORDER BY timestamp DESC"
    val resultSet = connection.createStatement().executeQuery(sql) ?: throw SQLException("Failed to execute SQL statement.")
    val comments = mutableListOf<Comment>()
    while (resultSet.next()) {
        comments.add(
            Comment(
                id = resultSet.getInt("id"),
                post = resultSet.getInt("post"),
                user = resultSet.getInt("user"),
                text = resultSet.getString("text"),
                timestamp = resultSet.getLong("timestamp"),
        ))
    }
    return comments
}

suspend fun getCommentByPost(pid : Int): MutableList<Comment>? {
    if(pid == -1) return null
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")
    try {
        val sql = """
        SELECT *
        FROM COMMENTS
        WHERE post LIKE ?
        """.trimIndent()

        val statement = connection.prepareStatement(sql)
        statement.setInt(1, pid)

        val resultSet = statement.executeQuery()

        val comments : MutableList<Comment> = mutableListOf()
        while (resultSet.next()) {
            comments.add(
                Comment(
                    id = resultSet.getInt("id"),
                    post = resultSet.getInt("post"),
                    user = resultSet.getInt("user"),
                    text = resultSet.getString("text"),
                    timestamp = resultSet.getLong("timestamp")
                ))
        }
        if(comments.isEmpty()) return null
        return comments
    }
    catch (e: SQLException) {
        e.printStackTrace()
        return null
    }
    finally {
        connection.close()
    }
}