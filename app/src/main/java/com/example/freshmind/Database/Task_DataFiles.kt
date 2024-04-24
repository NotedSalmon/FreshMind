package com.example.freshmind.Database

import java.time.LocalDate

/**
 * This class is used to store the data of the Tasks in the database.
 */
data class Task_DataFiles(
    val taskID: Int,
    val userID: Int,
    val taskTitle: String,
    val taskDescription: String,
    val startTime: LocalDate,
    val endTime: LocalDate,
    val dateModified: LocalDate
)

