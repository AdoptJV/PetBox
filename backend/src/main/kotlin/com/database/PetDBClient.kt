package com.jvdev.com.database
import com.jvdev.com.models.Pet
import com.jvdev.com.models.PetID
import com.jvdev.com.models.Sex
import com.jvdev.com.models.UserID
import java.sql.Date
import java.sql.SQLException
import java.sql.Types
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/*
 * Funções relacionadas a manipulação de dados na base da dados dos pets
 */

fun insertPet(pet: Pet): Boolean {
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")
    try {
        val sql = """
        INSERT INTO PETS
        (species, sex, name, age, castrated, photoURL, owner, registered, description) 
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
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
        smtm.setString(9, pet.description)

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
             photoUrl = resultSet.getString("photoURL"),
             description = resultSet.getString("description"),
             castrated = resultSet.getBoolean("castrated"),
             owner = resultSet.getInt("owner"),
             sex = Sex.valueOf(resultSet.getString("sex")))
         )
    }
    return pets
}

suspend fun getPetByCity(city: String?): MutableList<Map<String?, String?>>? {
    if(city == null) return null
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")
    try {
        val sql = """
        SELECT PETS.name, PETS.species, PETS.description, PETS.photoURL, PETS.id
        FROM PETS
        INNER JOIN USERS ON PETS.owner=USERS.id
        WHERE USERS.city LIKE ?
        """.trimIndent()

        val statement = connection.prepareStatement(sql)
        statement.setString(1, city)

        val resultSet = statement.executeQuery()

        val returnList : MutableList<Map<String?, String?>> = mutableListOf()
        while (resultSet.next()) {
            val name = resultSet.getString("name")
            val id = resultSet.getInt("id")
            val species = resultSet.getString("species")
            val description = resultSet.getString("description")
            val photoURL = resultSet.getString("photoURL")
            returnList.add(mapOf(
                "name" to name,
                "species" to species,
                "description" to description,
                "url" to photoURL,
                "id" to id.toString()
            ))

        }
        if(returnList.isEmpty()) return null
        return returnList
    }
    catch (e: SQLException) {
        e.printStackTrace()
        return null
    }
    finally {
        connection.close()
    }
}

suspend fun getPetByUser(uid: Int?): MutableList<Map<String, String?>>? {
    if(uid == -1) return null
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")
    try {
        val sql = """
        SELECT PETS.name, PETS.species, PETS.description, PETS.photoURL, PETS.id
        FROM PETS
        WHERE owner = ?
        """.trimIndent()

        val statement = connection.prepareStatement(sql)
        statement.setInt(1, uid!!)

        val resultSet = statement.executeQuery()

        val returnList : MutableList<Map<String, String?>> = mutableListOf()
        while (resultSet.next()) {
            val name = resultSet.getString("name")
            val id = resultSet.getInt("id")
            val species = resultSet.getString("species")
            val description = resultSet.getString("description")
            val photoURL = resultSet.getString("photoURL")
            returnList.add(mapOf(
                "name" to name,
                "species" to species,
                "description" to description,
                "url" to photoURL,
                "id" to id.toString()
            ))

        }
        if(returnList.isEmpty()) return null
        return returnList
    }
    catch (e: SQLException) {
        e.printStackTrace()
        return null
    }
    finally {
        connection.close()
    }
}
fun getPetID(petName: String, ownerId: UserID, specie: String): PetID? {
    var petId: PetID? = null
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")

    println(" $petName $ownerId $petId")

    try {
        val sql = """
            SELECT PETS.id
            FROM PETS
            WHERE name = ? 
            AND owner = ?
            AND species = ?
        """.trimIndent()

        val statement = connection.prepareStatement(sql)
        statement.setString(1, petName)
        statement.setInt(2, ownerId)
        statement.setString(3, specie)

        val result = statement.executeQuery()

        if (result.next()) {
            petId = result.getInt("id")
        }
        result.close()
        statement.close()
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        connection.close()
    }
    println(petId)
    return petId
}

suspend fun getPetByID(id: Int): Pet? {
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")
    try {
        val sql = """
        SELECT * FROM PETS WHERE id = ?
        """.trimIndent()

        val statement = connection.prepareStatement(sql)

        statement.setInt(1, id)
        val resultSet = statement.executeQuery()
        if (resultSet.next()) {
            val name = resultSet.getString("name")
            val species = resultSet.getString("species")
            val sexB = resultSet.getBoolean("sex")
            val age = resultSet.getInt("age")
            val castrated = resultSet.getBoolean("castrated")
            val photoURL = resultSet.getString("photoURL")
            val owner = resultSet.getInt("owner")
            val sex = if(sexB) Sex.MALE else Sex.FEMALE
            val description = resultSet.getString("description")

            return Pet(
                id = id,
                species = species,
                sex = sex,
                age = age,
                castrated = castrated,
                photoUrl = photoURL,
                owner = owner,
                name = name,
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


suspend fun getPetByFilters(filters: Map<String?, String?>): MutableList<Pet>? {
    if (filters.isEmpty()) return null
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")

    try {
        val sql = """
            SELECT *
            FROM PETS
            WHERE (? IS NULL OR sex LIKE ?)
              AND (? IS NULL OR age >= ?)
              AND (? IS NULL OR age <= ?)
              AND (? IS NULL OR castrated LIKE ?)
              AND (? IS NULL OR species LIKE ?)
        """.trimIndent()

        val statement = connection.prepareStatement(sql)

        val sex = filters["sex"]?.takeIf { it.isNotBlank() && it != "Qualquer" }
        val minAge = filters["minAge"]?.takeIf { it.isNotBlank() }
        val maxAge = filters["maxAge"]?.takeIf { it.isNotBlank() }
        val castratedStr = filters["castrated"]?.takeIf { it.isNotBlank() && it != "Não Especificar" }
        val castratedBool = castratedStr?.lowercase()?.toBooleanStrictOrNull()
        val species = filters["specie"]?.takeIf { it.isNotBlank() && it != "Qualquer" }

        var i = 1
        statement.setString(i++, sex)
        statement.setString(i++, sex)
        statement.setString(i++, minAge)
        statement.setInt(i++, minAge?.toIntOrNull() ?: 0)
        statement.setString(i++, maxAge)
        statement.setInt(i++, maxAge?.toIntOrNull() ?: Int.MAX_VALUE)
        statement.setString(i++, castratedStr)
        if (castratedBool != null)  statement.setBoolean(i++, castratedBool)
        else  statement.setNull(i++, Types.BOOLEAN)
        statement.setString(i++, species)
        statement.setString(i++, species)

        val resultSet = statement.executeQuery()

        val returnList: MutableList<Pet> = mutableListOf()
        while (resultSet.next()) {
            val id = resultSet.getInt("id")
            val name = resultSet.getString("name")
            val sexB = resultSet.getBoolean("sex")
            val age = resultSet.getInt("age")
            val castratedVal = resultSet.getBoolean("castrated")
            val photoURL = resultSet.getString("photoURL")
            val owner = resultSet.getInt("owner")
            val speciesVal = resultSet.getString("species")
            val description = resultSet.getString("description")

            val sexEnum = if (sexB) Sex.MALE else Sex.FEMALE

            returnList.add(
                Pet(
                    id = id,
                    species = speciesVal,
                    sex = sexEnum,
                    age = age,
                    castrated = castratedVal,
                    photoUrl = photoURL,
                    owner = owner,
                    name = name,
                    description = description
                )
            )
        }

        return if (returnList.isEmpty()) null else returnList
    } catch (e: SQLException) {
        e.printStackTrace()
        return null
    } finally {
        connection.close()
    }
}

fun updatePetPhotoUrl(petId: Int, url: String): Boolean {
    val connection = connectToDatabase() ?: throw SQLException("Failed to connect to database.")

    try {
        val sql = """
            UPDATE PETS
            SET photoURL = ?
            WHERE id = ?
        """.trimIndent()

        val stmt = connection.prepareStatement(sql)
        stmt.setString(1, url)
        stmt.setInt(2, petId)

        return stmt.executeUpdate() == 1
    } catch (e: SQLException) {
        e.printStackTrace()
        return false
    } finally {
        connection.close()
    }
}