package com.jvdev.com.database
import com.jvdev.com.cep.buscarEndereco
import com.jvdev.com.encryption.pswUtil
import com.jvdev.com.models.User
import com.jvdev.com.models.UserType
import java.sql.SQLException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/* Modificação */
fun insertUser(user: User): Boolean { // insert user in database
    val connection = connectToDatabase() ?: throw SQLException("Could not connect to database.")
    val sql = """
      INSERT INTO USERS
        (username, name, birthday, email, password,
         photoURL, phone, CEP, description, userType, joined)
      VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """.trimIndent()
    val stmt = connection.prepareStatement(sql)
    try {
        stmt.setString(1, user.username)
        stmt.setString(2, user.name)
        stmt.setString(3, user.birthday.toString())
        stmt.setString(4, user.email)
        stmt.setString(5, pswUtil.generateHash(user.psw))
        stmt.setString(6, user.pfpUrl)
        stmt.setString(7, user.phone)
        stmt.setString(8, user.address.cep)
        stmt.setString(9, user.description)
        stmt.setString(10, if (user.usrType == UserType.ONG) "ONG" else "REGULAR")
        stmt.setString(11, user.joined.toString())

        return stmt.executeUpdate() == 1 // verifica se uma linha foi atualizada no banco de dados
    } catch (e: SQLException) {
        e.printStackTrace()   // log
        return false
    } finally {
        stmt.close()
    }
}

/* consulta */

fun checkUser(username: String): Boolean {
    val connection = connectToDatabase() ?: throw SQLException("Could not connect to database.")
    try {
        val sql = """
        SELECT * FROM USERS WHERE username = ?
        """.trimIndent()

        val statement = connection.prepareStatement(sql)
        statement.setString(1, username)

        val resultSet = statement.executeQuery()
        if (resultSet.next()) return true
        return false
    }
    catch (e: SQLException) {
        e.printStackTrace()
    }
    finally {
        connection.close()
    }
    return false
}

suspend fun queryUser(username: String): User? {
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")
    try {
        val sql = """
        SELECT * FROM USERS WHERE username = ?
        """.trimIndent()

        val statement = connection.prepareStatement(sql)

        statement.setString(1, username)
        val resultSet = statement.executeQuery()
        if (resultSet.next()) {
            val id = resultSet.getInt("id")
            val name = resultSet.getString("name")
            val birthday = resultSet.getString("birthday")
            val cep = resultSet.getString("CEP")
            val password = resultSet.getString("password")
            val email = resultSet.getString("email")
            val phone = resultSet.getString("phone")
            val photoURL = resultSet.getString("photoURL")
            val userType = UserType.valueOf(resultSet.getString("userType"))
            val joined = resultSet.getString("joined")
            val description = resultSet.getString("description")
            return User(
                id = id,
                username = username,
                name = name,
                psw = password,
                address = buscarEndereco(cep),
                birthday = LocalDate.parse(birthday, DateTimeFormatter.ISO_DATE),
                email = email,
                phone = phone,
                usrType = userType,
                joined = LocalDate.parse(joined, DateTimeFormatter.ISO_DATE),
                pfpUrl = photoURL,
                description = description
            )
        }
        else {
            return null
        }
    }

    catch (e: SQLException) {
        e.printStackTrace()
        return null
    }
    finally {
        connection.close()
    }
    return null
}

