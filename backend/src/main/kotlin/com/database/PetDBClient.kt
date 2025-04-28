package com.jvdev.com.database
import com.jvdev.com.models.Pet
import com.jvdev.com.models.Sex
import java.sql.Date
import java.sql.SQLException
import java.time.LocalDate

/*
 * Funções relacionadas a manipulação de dados na base da dados dos pets
 */

fun insertPet(pet: Pet): Boolean {
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")
    try {
        val sql = """
        INSERT INTO PETS
        (specie, sex, name, age, castrated, photoURL, owner, registered) 
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()
        val smtm = connection.prepareStatement(sql)
        smtm.setString(1, pet.species)
        smtm.setBoolean(2, pet.sex == Sex.MALE)
        smtm.setString(3, pet.name)
        smtm.setInt(4, pet.age)
        smtm.setBoolean(5, pet.castrated)
        smtm.setString(6, pet.photoUrl)
        smtm.setInt(7, pet.owner)
        smtm.setDate(8, Date.valueOf(LocalDate.now()))

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