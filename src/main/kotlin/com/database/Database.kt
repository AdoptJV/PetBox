package com.jvdev.com.database
import com.jvdev.com.models.User
import java.nio.file.Paths
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

fun connectToDatabase(): Connection {
    val path = Paths.get("src/main/resources/Database/data.sqlite").toAbsolutePath()
    val url = "jdbc:sqlite:$path"
    return DriverManager.getConnection(url)
}

fun insertUser(connection: Connection, user: User): Boolean {
    val sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)"
    val statement = connection.prepareStatement(sql)

    statement.setString(1, user.name)
    statement.setString(2, user.psw)
    statement.setString(3, user.email)

    try { statement.execute() }
    catch (_: SQLException) { return false }
    return true
}