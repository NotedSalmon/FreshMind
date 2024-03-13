package com.example.freshmind.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val DataBaseName = "FreshMindDB.db"
private val ver : Int = 1

class DBHelper(context: Context) : SQLiteOpenHelper(context, DataBaseName, null, ver) {

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
    private val Task_Column_userID = "userID"
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
    private val Note_Column_userID = "userID"
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
        //-----------SQL Query for User Table----------------------------------
        var sqlCreateStatement: String = "CREATE TABLE " + UserTableName + " ( " + User_Column_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," + User_Column_FullName + " TEXT, " + User_Column_Email + " TEXT, " +
                User_Column_PhoneNo + " TEXT, " + User_Column_Username + " TEXT, " + User_Column_Password + " TEXT, " + User_Column_IsActive + " INTEGER DEFAULT 1, " + User_Column_DateModified + " TEXT, " + User_Column_DateCreated + " TEXT )"
        db?.execSQL(sqlCreateStatement)
        //------------SQL Query for Task Table---------------------------------
        sqlCreateStatement = "CREATE TABLE " + TaskTableName + " ( " + Task_Column_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," + Task_Column_userID + " INTEGER, " + Task_Column_TaskTitle + " TEXT, " +
                Task_Column_TaskDescription + " TEXT, " + Task_Column_StartTime + " TEXT, " + Task_Column_EndTime + " TEXT, " + Task_Column_DateModified + " TEXT, " + "FOREIGN KEY(" + Task_Column_userID + ") REFERENCES " + UserTableName + "(" + User_Column_ID + "))"
        db?.execSQL(sqlCreateStatement)
        //-------------SQL Query for Note Table--------------------------------
        sqlCreateStatement = "CREATE TABLE " + NoteTableName + " ( " + Note_Column_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," + Note_Column_userID + " INTEGER, " + Note_Column_NoteTitle + " TEXT, " +
                Note_Column_NoteContent + " TEXT, " + Note_Column_DateModified + " TEXT, " + Note_Column_DateCreated + " TEXT, " + "FOREIGN KEY(" + Note_Column_userID + ") REFERENCES " + UserTableName + "(" + User_Column_ID + "))"
        db?.execSQL(sqlCreateStatement)
        //-------------SQL Query for Calendar Table----------------------------
        sqlCreateStatement = "CREATE TABLE " + CalendarTableName + " ( " + Calendar_Column_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," + Calendar_Column_userID + " INTEGER, " + Calendar_Column_taskID + " INTEGER, " + Calendar_Column_EventTitle + " TEXT, " +
                Calendar_Column_EventDescription + " TEXT, " + Calendar_Column_EventColour + " TEXT, " + Calendar_Column_StartTime + " TEXT, " + Calendar_Column_EndTime + " TEXT, " + Calendar_Column_Reminder +
                " INTEGER, " + Calendar_Column_ReminderTime + " TEXT, " + Calendar_Column_DateModified + " TEXT, " + "FOREIGN KEY(" + Calendar_Column_userID + ") REFERENCES " + UserTableName + "(" + User_Column_ID + "), " + "FOREIGN KEY(" + Calendar_Column_taskID +
                ") REFERENCES " + TaskTableName + "(" + Task_Column_ID + "))"
        db?.execSQL(sqlCreateStatement)
        //--------------SQL Query for Settings Table---------------------------
        sqlCreateStatement = "CREATE TABLE " + SettingsTableName + " ( " + Settings_Column_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," + Settings_Column_userID + " INTEGER, " + Settings_Column_Theme + " TEXT, " +
                Settings_Column_PushNotification + " INTEGER, " + Settings_Column_EmailNotification + " INTEGER, " + Settings_Column_DateModified + " TEXT, " + "FOREIGN KEY(" + Settings_Column_userID + ") REFERENCES " + UserTableName + "(" + User_Column_ID + "))"
        db?.execSQL(sqlCreateStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE Users")
        db?.execSQL("DROP TABLE Tasks")
        db?.execSQL("DROP TABLE Notes")
        db?.execSQL("DROP TABLE Calendar")
        db?.execSQL("DROP TABLE Settings")
        onCreate(db)
    }

    /**
     * User Table Functions
     */
    fun addUser(user: User_DataFiles): Boolean {
        //WriteableDatabase for insert actions
        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        cv.put(User_Column_FullName, user.FullName)
        cv.put(User_Column_Email, user.Email)
        cv.put(User_Column_PhoneNo, user.PhoneNo)
        cv.put(User_Column_Username, user.Username)
        cv.put(User_Column_Password, user.Password)
        cv.put(User_Column_IsActive, user.IsActive)
        cv.put(User_Column_DateCreated, user.DateCreated.toString())
        cv.put(User_Column_DateModified, user.DateModified.toString())

        val success = db.insert(UserTableName, null, cv) //Creates a value to send back
        db.close()
        return success != -1L
    }

    fun validateUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val columns = arrayOf(User_Column_Username, User_Column_Password)
        val selection = "$User_Column_Username = ? AND $User_Column_Password = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor: Cursor =
            db.query(UserTableName, columns, selection, selectionArgs, null, null, null)
        val count = cursor.count

        cursor.close()
        return count > 0
    }

    fun checkUsername(username: String): Boolean {
        val db = this.readableDatabase
        val columns = arrayOf(User_Column_Username)
        val selection = "$User_Column_Username = ?"
        val selectionArgs = arrayOf(username)
        val cursor: Cursor =
            db.query(UserTableName, columns, selection, selectionArgs, null, null, null)
        val count = cursor.count

        cursor.close()
        return count > 0
    }

    fun deleteUser(username: String): Boolean {
        val db = this.writableDatabase
        val selection = "$User_Column_Username = ?"
        val selectionArgs = arrayOf(username)
        val success = db.delete(UserTableName, selection, selectionArgs)
        db.close()
        return success != -1
    }

    fun changePassword(oldPassword: String, newPassword: String): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(User_Column_Password, newPassword)
        val selection = "$User_Column_Password = ?"
        val selectionArgs = arrayOf(oldPassword)
        val success = db.update(UserTableName, cv, selection, selectionArgs)
        db.close()
        return success != -1
    }

    fun returnUserID(username: String): Int {
        val db = this.readableDatabase
        val columns = arrayOf(User_Column_ID)
        val selection = "$User_Column_Username = ?"
        val selectionArgs = arrayOf(username)
        val cursor: Cursor =
            db.query(UserTableName, columns, selection, selectionArgs, null, null, null)
        var userID = 0
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                userID = cursor.getInt(cursor.getColumnIndex(User_Column_ID))
            }
            cursor.close()
        }
        return userID
    }

    /**
     * Task Table Functions
     */

    fun addTask(task: Task_DataFiles): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        cv.put(Task_Column_userID, task.userID)
        cv.put(Task_Column_TaskTitle, task.taskTitle)
        cv.put(Task_Column_TaskDescription, task.taskDescription)
        cv.put(Task_Column_StartTime, task.startTime.toString())
        cv.put(Task_Column_EndTime, task.endTime.toString())
        cv.put(Task_Column_DateModified, task.dateModified.toString())

        val success = db.insert(TaskTableName, null, cv)
        db.close()
        return success != -1L
    }

    fun deleteTask(taskID: Int): Boolean {
        val db = this.writableDatabase
        val selection = "$Task_Column_ID = ?"
        val selectionArgs = arrayOf(taskID.toString())
        val success = db.delete(TaskTableName, selection, selectionArgs)
        db.close()
        return success != -1
    }

    fun updateTask(task: Task_DataFiles): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(Task_Column_TaskTitle, task.taskTitle)
        cv.put(Task_Column_TaskDescription, task.taskDescription)
        cv.put(Task_Column_StartTime, task.startTime.toString())
        cv.put(Task_Column_EndTime, task.endTime.toString())
        cv.put(Task_Column_DateModified, task.dateModified.toString())
        val selection = "$Task_Column_ID = ?"
        val selectionArgs = arrayOf(task.taskID.toString())
        val success = db.update(TaskTableName, cv, selection, selectionArgs)
        db.close()
        return success != -1
    }
    fun showAllTasks(userID: String): MutableList<Task_DataFiles> {
        val userId = returnUserID(userID)
        val taskList = mutableListOf<Task_DataFiles>()
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            // Query tasks associated with the given userID
            cursor = db.rawQuery("SELECT * FROM $TaskTableName WHERE $Task_Column_userID = $userId", null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        // Extract task details from cursor
                        val taskID = cursor.getInt(cursor.getColumnIndex(Task_Column_ID))
                        val taskTitle = cursor.getString(cursor.getColumnIndex(Task_Column_TaskTitle))
                        val taskDescription = cursor.getString(cursor.getColumnIndex(Task_Column_TaskDescription))
                        val startTime = cursor.getString(cursor.getColumnIndex(Task_Column_StartTime))
                        val endTime = cursor.getString(cursor.getColumnIndex(Task_Column_EndTime))

                        val localTime = LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss"))

                        val dateCreated: LocalDateTime = LocalDateTime.parse(localTime, DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss"))

                        // Create TaskDetails object and add to list
                        val taskDetails = Task_DataFiles(taskID,userId,taskTitle, taskDescription, startTime, endTime,dateCreated)
                        taskList.add(taskDetails)
                    } while (cursor.moveToNext())
                }
                cursor.close()
            }
        } catch (e: SQLiteException) {
            return mutableListOf()
        } finally {
            db.close()
        }
        return taskList
    }
}