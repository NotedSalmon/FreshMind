package com.example.freshmind.Database

import java.time.LocalDateTime

data class Settings_DataFiles(
    val settingID: Int,
    val userID: Int,
    val settingTheme: String,
    val pushNotification: Boolean,
    val emailNotification: Boolean, //Maybe
    val dateModified: LocalDateTime?
)
{

}
