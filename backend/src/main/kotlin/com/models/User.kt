package com.jvdev.com.models

import com.jvdev.com.cep.Endereco

@JvmInline
value class UserID(val value: String)

enum class UserType {REGULAR, ONG}

data class User (
    // val id : UserID,                                // user id
    val name : String,                              // user name
    // val birthday : LocalDate,                       // user birthday
    val email : String,                             // user email
    val psw : String,                               // user password
    // val pfpUrl : String,                            // user profile photo URL
    // val phone : String,                             // user phone number
    val address : Endereco                          // user address
    // val address : String,                           // user address
    // val description : String? = null,               // user description
    // val usrType : UserType,                         // user type
    // val joined : LocalDate = LocalDate.now()        // user joined date
) {
    /*
    public fun getAge() : Int {
        return Period.between(birthday, LocalDate.now()).years
    }
     */

}