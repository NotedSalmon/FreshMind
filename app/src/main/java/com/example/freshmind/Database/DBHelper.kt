package com.example.freshmind.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private val DataBaseName = "FreshMindDB.db"
private val ver : Int = 1

class DBHelper(context: Context) : SQLiteOpenHelper(context, DataBaseName, null, ver)
{

    /**
     * User Table
     */
    private val UserTableName = "tblUser"
    private val User_Column_ID = "userID"
    private val User_Column_FullName = "FullName"
    private val User_Column_Email = "Email"
    private val User_Column_PhoneNo = "PhoneNo"
    private val User_Column_Username = "Username"
    private val User_Column_Password = "Password"
    private val User_Column_IsActive = "IsActive"
    private val User_Column_DateCreated = "DateCreated"
    private val User_Column_DateModified = "DateModified"

    /**
     *  Task Table
     */
    private val TaskTableName = "tblTask"
    private val Task_Column_ID = "taskID"
    private val Task_Column_TaskTitle = "TaskTitle"
    private val Task_Column_TaskDescription = "TaskDescription"
    private val Task_Column_StartTime = "StartTime"
    private val Task_Column_EndTime = "EndTime"
    private val Task_Column_DateModified = "DateModified"

    /**
     *  Note Table
     */
    private val NoteTableName = "tblNote"
    private val Note_Column_ID = "noteID"
    private val Note_Column_NoteTitle = "NoteTitle"
    private val Note_Column_NoteContent = "NoteContent"
    private val Note_Column_DateCreated = "DateCreated"
    private val Note_Column_DateModified = "DateModified"

    /**
     *  Calendar Table
     */
    private val CalendarTableName = "tblCalendar"
    private val Calendar_Column_ID = "eventID"
    private val Calendar_Column_userID = "userID"
    private val Calendar_Column_taskID = "taskID"
    private val Calendar_Column_EventTitle = "EventTitle"
    private val Calendar_Column_EventDescription = "EventDescription"
    private val Calendar_Column_EventColour = "Colour"
    private val Calendar_Column_StartTime = "StartTime"
    private val Calendar_Column_EndTime = "EndTime"
    private val Calendar_Column_Reminder = "Reminder"
    private val Calendar_Column_ReminderTime = "ReminderTime"
    private val Calendar_Column_DateModified = "DateModified"

    /**
     * Settings Table
     */
    private val SettingsTableName = "tblSettings"
    private val Settings_Column_ID = "settingID"
    private val Settings_Column_userID = "userID"
    private val Settings_Column_Theme = "Theme"
    private val Settings_Column_PushNotification = "PushNotification"
    private val Settings_Column_EmailNotification = "EmailNotification" //Maybe?
    private val Settings_Column_DateModified = "DateModified"
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS Tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS Notes (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, content TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE Tasks")
        db?.execSQL("DROP TABLE Notes")
        onCreate(db)
    }

}