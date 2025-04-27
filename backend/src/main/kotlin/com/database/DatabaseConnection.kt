package com.jvdev.com.database
import java.nio.file.Paths
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/*
 * Funções relacionadas a conexão com a base de dados do projeto
 */

fun connectToDatabase(): Connection? {
    try {
        val path = Paths.get("src/main/resources/Database/data.sqlite").toAbsolutePath()
        val url = "jdbc:sqlite:$path"
        return DriverManager.getConnection(url)
    } catch (e: SQLException) {
        e.printStackTrace()
        return null
    }
}