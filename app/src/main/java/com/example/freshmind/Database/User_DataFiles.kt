package com.example.freshmind.Database

import java.time.LocalDateTime

data class User_DataFiles(
    val userID: Int,
    val FullName: String,
    val Email: String,
    val PhoneNo: String,
    val Username: String,
    val Password: String,
    val IsActive: Boolean,
    val DateCreated: LocalDateTime,
    val DateModified: LocalDateTime?)
{

}
