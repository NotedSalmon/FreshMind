package com.example.freshmind.Database

import java.time.LocalDateTime

/**
 * This class is used to store the data of the Notes in the database.
 */
data class Notes_DataFiles(
    val noteID: Int,
    val userID: Int,
    val noteTitle: String,
    val noteContent: String,
    val dateCreated: String,
    val dateModified: LocalDateTime?,
    val isPinned: Boolean)

