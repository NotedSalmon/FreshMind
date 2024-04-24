package com.example.freshmind.Database

import java.time.LocalDate

/**
 * This class is used to store the data of the Settings in the database.
 */
data class Settings_DataFiles(
    val settingID: Int,
    val userID: Int,
    val settingTheme: String,
    val taskHidden: Boolean,
    val dateModified: LocalDate
)
