package com.jvdev.com.models

import kotlinx.serialization.Serializable
import java.time.LocalDate

enum class Sex { MALE, FEMALE }

typealias PetID = Int

@Serializable
data class Pet (
    val id : PetID,                                     // identification
    val species : String,                               // pet specie
    val sex : Sex,                                      // MALE or FEMALE
    val name : String,                                  // pet name
    val age : Int,                                      // pet age
    val castrated : Boolean,                            // is the pet castrated?
    var photoUrl : String?,                             // pet photo URL
    val owner : UserID,                                 // pet owner id
    val description : String?,                          // pet description
    val registered : Long = System.currentTimeMillis()  // register date
)