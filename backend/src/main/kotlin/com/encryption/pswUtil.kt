package com.jvdev.com.encryption
import at.favre.lib.crypto.bcrypt.BCrypt

object pswUtil {
    private const val bcryptCost : Int = 10

    fun generateHash(psw : String) : String =
        BCrypt.withDefaults().hashToString(bcryptCost, psw.toCharArray())

    fun verify(psw : String, hash : String) : Boolean {
        return BCrypt.verifyer().verify(psw.toCharArray(), hash).verified
    }

}