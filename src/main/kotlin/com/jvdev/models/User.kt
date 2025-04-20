package com.jvdev.com.jvdev.models

import java.time.LocalDate
import java.time.Period

data class User (
    val id : String,
    val name : String,
    val birthday : LocalDate,
    val email : String,
    val psw : String,
    val pfpUrl : String,
    val phone : String,
    val location : Location,
    val address : String,
    val description : String,
    val usrType : String,
    val joined : LocalDate = LocalDate.now()
) {
    public fun getAge() : Int {
        return Period.between(birthday, LocalDate.now()).years
    }

}