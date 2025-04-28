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
        val smtm = connection.prepareStatement(sql) ?: throw SQLException("Failed to execute SQL statement.")
        smtm.setString(1, pet.species)
        smtm.setString(2, if (pet.sex == Sex.MALE) "MALE" else "FEMALE")
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

fun queryAllPets(): List<Pet> {
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")
    val sql = "SELECT * FROM PETS"
    val resultSet = connection.createStatement().executeQuery(sql) ?: throw SQLException("Failed to execute SQL statement.")
    val pets = mutableListOf<Pet>()
    while (resultSet.next()) {
        pets.add(
         Pet(
             id = resultSet.getInt("id"),
             species = resultSet.getString("specie"),
             name = resultSet.getString("name"),
             age = resultSet.getInt("age"),
             photoUrl = resultSet.getString("photoUrl"),
             castrated = resultSet.getBoolean("castrated"),
             owner = resultSet.getInt("owner"),
             sex = Sex.valueOf(resultSet.getString("sex")))
         )
    }
    return pets
}