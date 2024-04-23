package com.example.freshmind.Database

import java.time.LocalDate

data class Settings_DataFiles(
    val settingID: Int,
    val userID: Int,
    val settingTheme: String,
    val taskHidden: Boolean,
    val dateModified: LocalDate
)
