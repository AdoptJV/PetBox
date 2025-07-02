package com.jvdev.com.database
import com.jvdev.com.cep.Endereco
import com.jvdev.com.cep.buscarEndereco
import com.jvdev.com.cep.externalApiAvailable
import com.jvdev.com.encryption.pswUtil
import com.jvdev.com.models.User
import com.jvdev.com.models.UserType
import java.sql.SQLException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/* Operações de Modificação */

/**
 * Registra um novo endereço no sistema
 *
 * @param address Objeto user contendo dados do endereço
 * @return booleano informando se deu certo a inserção
 * @throws SQLException conexão com o banco de dados falhou
 */
suspend fun insertAddress(address: Endereco): Boolean {
    val connection = connectToDatabase() ?: throw SQLException("Could not connect to database.")
    val sql = """
      INSERT INTO ADDRESS
        (cep, logradouro, complemento, bairro, localidade, uf, ibge, gia, ddd, siafi)
      VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """.trimIndent()
    val stmt = connection.prepareStatement(sql)
    try {
        stmt.setString(1, address.cep)
        stmt.setString(2, address.logradouro)
        stmt.setString(3, address.complemento)
        stmt.setString(4, address.bairro)
        stmt.setString(5, address.localidade)
        stmt.setString(6, address.uf)
        stmt.setString(7, address.ibge)
        stmt.setString(8, address.gia)
        stmt.setString(9, address.ddd)
        stmt.setString(10, address.siafi)

        return stmt.executeUpdate() == 1 // verifica se uma linha foi atualizada no banco de dados
    } catch (e: SQLException) {
        e.printStackTrace()   // log erro
        return false
    } finally {
        stmt.close()
    }
}

suspend fun getAddressByCep(cep: String): Endereco? {
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")
    try {
        val sql = """
        SELECT * FROM ADDRESS WHERE cep = ?
        """.trimIndent()
        val statement = connection.prepareStatement(sql)

        statement.setString(1, cep)
        val resultSet = statement.executeQuery()
        if (resultSet.next()) {
            return Endereco (
                cep = cep,
                logradouro = resultSet.getString("logradouro"),
                complemento = resultSet.getString("complemento"),
                bairro = resultSet.getString("bairro"),
                localidade = resultSet.getString("localidade"),
                uf = resultSet.getString("uf"),
                ibge = resultSet.getString("ibge"),
                gia = resultSet.getString("gia"),
                ddd = resultSet.getString("ddd"),
                siafi = resultSet.getString("siafi")
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