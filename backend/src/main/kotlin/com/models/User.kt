package com.jvdev.com.models

import com.jvdev.com.cep.Endereco
import java.time.LocalDate

typealias UserID = Int

enum class UserType {REGULAR, ONG}

data class User (
    val id : UserID,                                // user id
    val username : String,                          // unique username
    val name : String,                              // user name
    val birthday : LocalDate,                       // user birthday
    val email : String,                             // user email
    val psw : String,                               // user password
    val phone : String,                             // user phone number
    val address : Endereco?,                         // user address
    val usrType : UserType = UserType.REGULAR,      // user type
    val joined : LocalDate = LocalDate.now(),       // user joined date
    val pfpUrl : String? = null,                    // user profile photo URL
    val description : String? = null,               // user description
)