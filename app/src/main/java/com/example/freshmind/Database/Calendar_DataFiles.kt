package com.example.freshmind.Database

import java.time.LocalDateTime

data class Calendar_DataFiles(
    val eventID: Int,
    val userID: Int,
    val taskID: Int,
    val eventTitle: String,
    val eventDescription: String,
    val colour: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val reminder: LocalDateTime?
)
{

}
