package com.jvdev.com.database
import com.jvdev.com.cep.buscarEndereco
import com.jvdev.com.encryption.pswUtil
import com.jvdev.com.models.User
import com.jvdev.com.models.UserType
import java.sql.SQLException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/* Operações de Modificação */

/**
 * Registra um novo usuário no sistema
 *
 * @param user Objeto user contendo dados do usuário
 * @return booleano informando se deu certo a inserção
 * @throws SQLException conexão com o banco de dados falhou
 */
fun insertUser(user: User): Boolean {
    val connection = connectToDatabase() ?: throw SQLException("Could not connect to database.")
    val sql = """
      INSERT INTO USERS
        (username, name, birthday, email, password,
         photoURL, phone, CEP, description, userType, joined, city)
      VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
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
        stmt.setString(8, user.address?.cep)
        stmt.setString(9, user.description)
        stmt.setString(10, if (user.usrType == UserType.ONG) "ONG" else "REGULAR")
        stmt.setString(11, user.joined.toString())

        return stmt.executeUpdate() == 1 // verifica se uma linha foi atualizada no banco de dados
    } catch (e: SQLException) {
        e.printStackTrace()   // log erro
        return false
    } finally {
        stmt.close()
    }
}

/* Operações de consulta */

/**
 * Verifica se existe um usuário com 'username' no banco de dados
 *
 * @param username username que será buscado
 * @return booleano que indica se o username está sendo utilizado
 * @throws SQLException conexão com o banco de dados falhou
 */

fun checkUsername(username: String): Boolean {
    val connection = connectToDatabase() ?: throw SQLException("Could not connect to database.")
    try {
        val sql = """
        SELECT * FROM USERS WHERE username = ?
        """.trimIndent()

        val statement = connection.prepareStatement(sql)
        statement.setString(1, username)
        val resultSet = statement.executeQuery()
        return resultSet.next()
    }
    catch (e: SQLException) {
        e.printStackTrace()
    }
    finally {
        connection.close()
    }
    return false
}

/**
 * Verifica se existe um usuário com 'email' no banco de dados
 *
 * @param email email que será buscado
 * @return booleano que indica se o email está sendo utilizado
 * @throws SQLException conexão com o banco de dados falhou
 */
fun checkEmail(email: String): Boolean {
    val connection = connectToDatabase() ?: throw SQLException("Could not connect to database.")
    try {
        val sql = """
            SELECT * FROM USERS WHERE email = ?
        """.trimIndent()

        val statement = connection.prepareStatement(sql)
        statement.setString(1, email)
        val resultSet = statement.executeQuery()
        return resultSet.next()
    }
    catch (e: SQLException) {
        e.printStackTrace()
    }
    finally {
        connection.close()
    }
    return false
}

/**
 * Retorna um objeto user correspondente ao usuário com 'username'
 *
 * @param username username para buscar no banco de dados
 * @return User? caso exista o usuário com o 'username' retorna o objeto correspondente. Caso contrário retorna null
 * @throws SQLException conexão com o banco de dados falhou
 */
suspend fun getUserByUsername(username: String): User? {
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")
    try {
        val sql = """
        SELECT * FROM USERS WHERE username = ?
        """.trimIndent()
        println(1)
        val statement = connection.prepareStatement(sql)
        println(2)

        statement.setString(1, username)
        println(3)
        val resultSet = statement.executeQuery()
        println(4)
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
            println(5)
            return User(
                id = id,
                username = username,
                name = name,
                psw = password,
                address = null,
                birthday = LocalDate.parse(birthday, DateTimeFormatter.ISO_DATE),
                email = email,
                phone = phone,
                usrType = userType,
                joined = LocalDate.parse(joined, DateTimeFormatter.ISO_DATE),
                pfpUrl = photoURL,
                description = description
            )
        }
        else return null

    }
    catch (e: SQLException) {
        e.printStackTrace()
        return null
    }
    finally {
        connection.close()
        println("Fechou")
    }
}

suspend fun getUserByID(id: Int): User? {
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")
    try {
        val sql = """
        SELECT * FROM USERS WHERE id = ?
        """.trimIndent()

        val statement = connection.prepareStatement(sql)

        statement.setInt(1, id)
        val resultSet = statement.executeQuery()
        if (resultSet.next()) {
            val username = resultSet.getString("username")
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
                address = null,
                birthday = LocalDate.parse(birthday, DateTimeFormatter.ISO_DATE),
                email = email,
                phone = phone,
                usrType = userType,
                joined = LocalDate.parse(joined, DateTimeFormatter.ISO_DATE),
                pfpUrl = photoURL,
                description = description
            )
        }
        else return null

    }
    catch (e: SQLException) {
        e.printStackTrace()
        return null
    }
    finally {
        connection.close()
    }
}

fun getAllUsers(): List<String> {
    val connection = connectToDatabase() ?: throw SQLException("Could not connect to database.")
    val users = mutableListOf<String>()
    try {
        val sql = """
            SELECT * FROM USERS
        """.trimIndent()
        val statement = connection.prepareStatement(sql)

        val resultSet = statement.executeQuery()
        while (resultSet.next()) {
            val username = resultSet.getString("username")
            users.add(username)
        }
    }
    catch (e: SQLException) {
        e.printStackTrace()
    }
    finally {
        connection.close()
    }
    return users
}

fun getIDbyUsername(username : String) : Int {
    val connection = connectToDatabase() ?: throw SQLException("Could not connect to database.")
    try {
        val sql = """
            SELECT id FROM USERS
            WHERE username LIKE ?
        """.trimIndent()
        val statement = connection.prepareStatement(sql)
        statement.setString(1, username)
        var userID = -1
        val resultSet = statement.executeQuery()
        if (resultSet.next()) {
            userID= resultSet.getInt("id")
        }
        return userID
    }
    catch (e: SQLException) {
        e.printStackTrace()
        return -1
    }
    finally {
        connection.close()
    }
}