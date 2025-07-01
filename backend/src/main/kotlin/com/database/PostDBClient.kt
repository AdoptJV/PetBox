package com.jvdev.com.database
import com.jvdev.com.models.Post
import com.jvdev.com.models.User
import java.sql.Date
import java.sql.SQLException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/*
 * Funções relacionadas a manipulação de dados na base da dados dos posts
 */

fun insertPost(post: Post): Boolean {
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")
    try {
        val sql = """
        INSERT INTO POSTS
        (user, photoUrl, caption, timestamp) 
        VALUES (?, ?, ?, ?)
        """.trimIndent()

        val smtm = connection.prepareStatement(sql) ?: throw SQLException("Failed to execute SQL statement.")
        smtm.setInt(1, post.ownerID)
        smtm.setString(2, post.imgUrl)
        smtm.setString(3, post.caption)
        smtm.setLong(4, post.timestamp)

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

fun queryAllPosts(): List<Post> {
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")
    val sql = "SELECT * FROM POSTS ORDER BY timestamp DESC"
    val resultSet = connection.createStatement().executeQuery(sql) ?: throw SQLException("Failed to execute SQL statement.")
    val posts = mutableListOf<Post>()
    while (resultSet.next()) {
        posts.add(
            Post(
                postID = resultSet.getInt("id"),
                ownerID = resultSet.getInt("user"),
                imgUrl = resultSet.getString("photoUrl"),
                caption = resultSet.getString("caption"),
                timestamp = resultSet.getLong("timestamp"),
        ))
    }
    return posts
}

suspend fun getPostByUser(uid : Int): MutableList<Post>? {
    if(uid == -1) return null
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")
    try {
        val sql = """
        SELECT *
        FROM POSTS
        WHERE user LIKE ?
        """.trimIndent()

        val statement = connection.prepareStatement(sql)
        statement.setInt(1, uid)

        val resultSet = statement.executeQuery()

        val posts : MutableList<Post> = mutableListOf()
        while (resultSet.next()) {
            posts.add(
                Post(
                    postID = resultSet.getInt("id"),
                    ownerID = resultSet.getInt("user"),
                    imgUrl = resultSet.getString("photoUrl"),
                    caption = resultSet.getString("caption"),
                    timestamp = resultSet.getLong("timestamp"),
                ))
        }
        if(posts.isEmpty()) return null
        return posts
    }
    catch (e: SQLException) {
        e.printStackTrace()
        return null
    }
    finally {
        connection.close()
    }
}