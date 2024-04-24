package com.example.freshmind.Database

import java.time.LocalDateTime

/**
 * This class is used to store the data of the User in the database.
 */
data class User_DataFiles(
    val userID: Int,
    val FullName: String,
    val Email: String,
    val PhoneNo: String,
    val Username: String,
    val Password: String,
    val IsActive: Int,
    val DateCreated: LocalDateTime,
    val DateModified: LocalDateTime?)
