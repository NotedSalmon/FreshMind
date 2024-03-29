package com.example.freshmind.Database

import java.time.LocalDate

data class Task_DataFiles(
    val taskID: Int,
    val userID: Int,
    val taskTitle: String,
    val taskDescription: String,
    val startTime: LocalDate,
    val endTime: LocalDate,
    val dateModified: LocalDate
)
{

}

