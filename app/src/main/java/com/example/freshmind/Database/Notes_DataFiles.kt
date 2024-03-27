package com.example.freshmind.Database

import java.time.LocalDateTime

data class Notes_DataFiles(
    val noteID: Int,
    val userID: Int,
    val noteTitle: String,
    val noteContent: String,
    val dateCreated: String,
    val dateModified: LocalDateTime?,
    val isPinned: Boolean)
{


}

