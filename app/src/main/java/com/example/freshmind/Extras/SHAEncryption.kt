package com.example.wagonersexperts.extra

import java.security.MessageDigest

//https://gist.github.com/lovubuntu/164b6b9021f5ba54cefc67f60f7a1a25
//Encryption used when storing passwords
object SHAEncryption {
    fun shaEncrypt(info : String): String {
        val bytes = info.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}