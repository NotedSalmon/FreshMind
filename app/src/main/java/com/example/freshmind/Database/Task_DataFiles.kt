package com.example.freshmind.Database

import java.time.LocalDateTime

data class Task_DataFiles(
    val taskID: Int,
    val userID: Int,
    val taskTitle: String,
    val taskDescription: String,
    val startTime: String,
    val endTime: String,
    val dateModified: LocalDateTime?
)
{

}

