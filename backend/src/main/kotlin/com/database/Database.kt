package com.jvdev.com.database
import com.jvdev.com.models.User
import java.nio.file.Paths
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

fun connectToDatabase(): Connection {
    val path = Paths.get("backend/src/main/resources/Database/data.sqlite").toAbsolutePath()
    val url = "jdbc:sqlite:$path"
    return DriverManager.getConnection(url)
}

fun insertUser(connection: Connection, user: User): Boolean {
    val sql = "INSERT INTO users (username, password, email, cep, cidade, estado) VALUES (?, ?, ?, ?, ?, ?)"
    val statement = connection.prepareStatement(sql)

    statement.setString(1, user.name)
    statement.setString(2, user.psw)
    statement.setString(3, user.email)
    statement.setString(4, user.address.cep)
    statement.setString(5, user.address.localidade)
    statement.setString(6, user.address.uf)

    try { statement.execute() }
    catch (_: SQLException) { return false }
    return true
}