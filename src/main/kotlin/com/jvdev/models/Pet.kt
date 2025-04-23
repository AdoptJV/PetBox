package com.jvdev.com.jvdev.models

import java.time.LocalDate

data class Pet (
    val id : String,
    val species : String,
    val sex : Boolean,
    val name : String?,
    val age : Int?,
    val castrated : String,
    val photoUrl : String?,
    val owner : User,
    val registered : LocalDate = LocalDate.now()
)