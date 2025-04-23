package com.jvdev.com.models

import java.math.BigInteger
import java.time.LocalDate

enum class Sex { MALE, FEMALE }

@JvmInline
value class PetID(val value: String)

data class Pet (
    val id : PetID,                  // identification
    val species : String,           // pet specie
    val sex : Sex,                  // MALE or FEMALE
    val name : String,              // pet name
    val age : Int,                  // pet age
    val castrated : Boolean,        // is the pet castrated?
    val photoUrl : String,          // pet photo URL
    val owner : UserID,             // pet owner id
    val registered : LocalDate      // register date
)