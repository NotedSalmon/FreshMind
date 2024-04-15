package com.example.freshmind.Database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.freshmind.Authentication.globalUser
import com.google.common.collect.Table
import com.google.firebase.firestore.auth.User
import java.time.LocalDate
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
    private val Note_Column_isPinned = "isPinned"

    /**
     * Settings Table
     */
    private val SettingsTableName = "tblSettings"
    private val Settings_Column_ID = "settingID"
    private val Settings_Column_userID = "userID"
    private val Settings_Column_Theme = "Theme"
    private val Settings_Column_HideTasks = "HideTasks"
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
                Note_Column_NoteContent + " TEXT, " + Note_Column_DateModified + " TEXT, " + Note_Column_DateCreated + " TEXT, " + Note_Column_isPinned + " BIT, " + "FOREIGN KEY(" + Note_Column_userID + ") REFERENCES " + UserTableName + "(" + User_Column_ID + "))"
        db?.execSQL(sqlCreateStatement)
        //--------------SQL Query for Settings Table---------------------------
        sqlCreateStatement = "CREATE TABLE " + SettingsTableName + " ( " + Settings_Column_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," + Settings_Column_userID + " INTEGER, " + Settings_Column_Theme + " TEXT, " +
                Settings_Column_HideTasks + " BIT, " + Settings_Column_DateModified + " TEXT, " + "FOREIGN KEY(" + Settings_Column_userID + ") REFERENCES " + UserTableName + "(" + User_Column_ID + "))"
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

    fun changePassword(oldPassword: String, newPassword: String, username: String) : Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        return try {
            val sqlStatement = "UPDATE $UserTableName SET $User_Column_Password = '$newPassword' WHERE $User_Column_Username = '$username' AND $User_Column_Password = '$oldPassword'"
            db.execSQL(sqlStatement)
            db.close()
            true
        } catch (e: Exception) {
            // Log the error or handle it gracefully
            e.printStackTrace()
            db.close()
            false
        }
    }


    fun changeUsername(oldUsername: String, newUsername: String): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(User_Column_Username, newUsername)
        val selection = "$User_Column_Username = ?"
        val selectionArgs = arrayOf(oldUsername)
        val success = db.update(UserTableName, cv, selection, selectionArgs)
        db.close()
        return success != -1
    }

    @SuppressLint("Range")
    fun getUser(username: String) : User_DataFiles {
        val db = this.readableDatabase
        val columns = arrayOf(User_Column_ID, User_Column_FullName, User_Column_Email, User_Column_PhoneNo, User_Column_Username, User_Column_Password, User_Column_IsActive, User_Column_DateCreated, User_Column_DateModified)
        val selection = "$User_Column_Username = ?"
        val selectionArgs = arrayOf(username)
        val cursor: Cursor = db.query(UserTableName, columns, selection, selectionArgs, null, null, null)
        var user = User_DataFiles(0, "", "", "", "", "", 0, LocalDateTime.now(), LocalDateTime.now())
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val userID = cursor.getInt(cursor.getColumnIndex(User_Column_ID))
                val fullName = cursor.getString(cursor.getColumnIndex(User_Column_FullName))
                val email = cursor.getString(cursor.getColumnIndex(User_Column_Email))
                val phoneNo = cursor.getString(cursor.getColumnIndex(User_Column_PhoneNo))
                val username = cursor.getString(cursor.getColumnIndex(User_Column_Username))
                val password = cursor.getString(cursor.getColumnIndex(User_Column_Password))
                val isActive = cursor.getInt(cursor.getColumnIndex(User_Column_IsActive))
                val dateCreated = LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(User_Column_DateCreated)))
                val dateModified = LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(User_Column_DateModified)))

                user = User_DataFiles(userID, fullName, email, phoneNo, username, password, isActive, dateCreated, dateModified)
            }
            cursor.close()
        }
        return user
    }

    fun changeEmail(username: String, newEmail: String): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        return try {
            val sqlStatement = "UPDATE $UserTableName SET $User_Column_Email= '$newEmail' WHERE $User_Column_Username = '$username'"
            db.execSQL(sqlStatement)
            db.close()
            true
        } catch (e: Exception) {
            // Log the error or handle it gracefully
            e.printStackTrace()
            db.close()
            false
        }
    }

    @SuppressLint("Range")
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
    @SuppressLint("Range")
    fun showAllTasks(userID: String): MutableList<Task_DataFiles> {
        val userId = returnUserID(userID)
        val taskList = mutableListOf<Task_DataFiles>()
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            // Query tasks associated with the given userID
            cursor = db.rawQuery("SELECT * FROM $TaskTableName WHERE $Task_Column_userID = ? ORDER BY $Task_Column_StartTime ASC", arrayOf(userId.toString()))
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        // Extract task details from cursor
                        val taskID = cursor.getInt(cursor.getColumnIndex(Task_Column_ID))
                        val taskTitle = cursor.getString(cursor.getColumnIndex(Task_Column_TaskTitle))
                        val taskDescription = cursor.getString(cursor.getColumnIndex(Task_Column_TaskDescription)) ?: ""
                        val startTimeString = cursor.getString(cursor.getColumnIndex(Task_Column_StartTime))
                        val endTimeString = cursor.getString(cursor.getColumnIndex(Task_Column_EndTime))
                        val dateModifiedString = cursor.getString(cursor.getColumnIndex(Task_Column_DateModified))

                        val startTime = LocalDate.parse(startTimeString)
                        val endTime = LocalDate.parse(endTimeString)
                        val dateModified = LocalDate.parse(dateModifiedString)

                        // Create TaskDetails object and add to list
                        val taskDetails = Task_DataFiles(taskID, userId, taskTitle, taskDescription, startTime, endTime, dateModified)
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


    @SuppressLint("Range")
    fun showTasksForDate(userID: String, selectedDate: LocalDate): List<Task_DataFiles> {
        val userId = returnUserID(userID)
        val taskList = mutableListOf<Task_DataFiles>()
        val db = this.readableDatabase

        return try {
            val cursor = db.rawQuery(
                "SELECT * FROM $TaskTableName WHERE $Task_Column_userID = ? AND $Task_Column_StartTime = ?",
                arrayOf(userId.toString(), selectedDate.toString())  // Assuming the date format in the database matches LocalDate.toString()
            )

            cursor.use {
                while (cursor.moveToNext()) {
                    val taskID = cursor.getInt(cursor.getColumnIndex(Task_Column_ID))
                    val taskTitle = cursor.getString(cursor.getColumnIndex(Task_Column_TaskTitle))
                    val taskDescription = cursor.getString(cursor.getColumnIndex(Task_Column_TaskDescription))
                    val startTimeString = cursor.getString(cursor.getColumnIndex(Task_Column_StartTime))
                    val endTimeString = cursor.getString(cursor.getColumnIndex(Task_Column_EndTime))
                    val dateModifiedString = cursor.getString(cursor.getColumnIndex(Task_Column_DateModified))

                    val startTime = LocalDate.parse(startTimeString)
                    val endTime = LocalDate.parse(endTimeString)
                    val dateModified = LocalDate.parse(dateModifiedString)


                    val taskDetails = Task_DataFiles(taskID, userId, taskTitle, taskDescription, startTime, endTime, dateModified)
                    taskList.add(taskDetails)
                }
            }

            taskList
        } catch (e: SQLiteException) {
            emptyList()
        } finally {
            db.close()
        }
    }

    @SuppressLint("Range")
    fun closesTasks(userID: String): MutableList<Task_DataFiles> {
        val userId = returnUserID(userID)
        val localDate = LocalDate.now()
        val taskList = mutableListOf<Task_DataFiles>()
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery(
            "SELECT * FROM $TaskTableName WHERE $Task_Column_userID = ? AND $Task_Column_EndTime >= ? ORDER BY $Task_Column_EndTime ASC LIMIT 2",
            arrayOf(userId.toString(), localDate.toString())
        )

        try {
            cursor?.use { cursor ->
                while (cursor.moveToNext()) {
                    val taskID = cursor.getInt(cursor.getColumnIndex(Task_Column_ID))
                    val taskTitle = cursor.getString(cursor.getColumnIndex(Task_Column_TaskTitle))
                    val taskDescription = cursor.getString(cursor.getColumnIndex(Task_Column_TaskDescription))
                    val startTimeString = cursor.getString(cursor.getColumnIndex(Task_Column_StartTime))
                    val endTimeString = cursor.getString(cursor.getColumnIndex(Task_Column_EndTime))
                    val dateModifiedString = cursor.getString(cursor.getColumnIndex(Task_Column_DateModified))

                    val startTime = LocalDate.parse(startTimeString)
                    val endTime = LocalDate.parse(endTimeString)
                    val dateModified = LocalDate.parse(dateModifiedString)

                    val taskDetails = Task_DataFiles(taskID, userId, taskTitle, taskDescription, startTime, endTime, dateModified)
                    taskList.add(taskDetails)
                }
            }
        } catch (e: SQLiteException) {
            // Handle the exception appropriately, e.g., log or display an error message
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }
        return taskList
    }

    @SuppressLint("Range")
    fun hideExpiredTasks(userID: String): MutableList<Task_DataFiles> {
        val userId = returnUserID(userID)
        val localDate = LocalDate.now()
        val taskList = mutableListOf<Task_DataFiles>()
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery(
            "SELECT * FROM $TaskTableName WHERE $Task_Column_userID = ? AND $Task_Column_EndTime >= ? ORDER BY $Task_Column_StartTime ASC",
            arrayOf(userId.toString(), localDate.toString())
        )

        try {
            cursor?.use { cursor ->
                while (cursor.moveToNext()) {
                    val taskID = cursor.getInt(cursor.getColumnIndex(Task_Column_ID))
                    val taskTitle = cursor.getString(cursor.getColumnIndex(Task_Column_TaskTitle))
                    val taskDescription = cursor.getString(cursor.getColumnIndex(Task_Column_TaskDescription))
                    val startTimeString = cursor.getString(cursor.getColumnIndex(Task_Column_StartTime))
                    val endTimeString = cursor.getString(cursor.getColumnIndex(Task_Column_EndTime))
                    val dateModifiedString = cursor.getString(cursor.getColumnIndex(Task_Column_DateModified))

                    val startTime = LocalDate.parse(startTimeString)
                    val endTime = LocalDate.parse(endTimeString)
                    val dateModified = LocalDate.parse(dateModifiedString)

                    val taskDetails = Task_DataFiles(taskID, userId, taskTitle, taskDescription, startTime, endTime, dateModified)
                    taskList.add(taskDetails)
                }
            }
        } catch (e: SQLiteException) {
            // Handle the exception appropriately, e.g., log or display an error message
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }
        return taskList
    }



    /**
     * Notes Table Functions
     */

    @SuppressLint("Range")
    fun showAllNotes(userID: String): MutableList<Notes_DataFiles> {
        val userId = returnUserID(userID)
        val notesList = mutableListOf<Notes_DataFiles>()
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            // Query tasks associated with the given userID
            cursor = db.rawQuery("SELECT * FROM $NoteTableName WHERE $Note_Column_userID = $userId", null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        // Extract task details from cursor
                        val noteID = cursor.getInt(cursor.getColumnIndex(Note_Column_ID))
                        val noteTitle = cursor.getString(cursor.getColumnIndex(Note_Column_NoteTitle))
                        val noteContent = cursor.getString(cursor.getColumnIndex(Note_Column_NoteContent))
                        val dateCreated = cursor.getString(cursor.getColumnIndex(Note_Column_DateCreated))
                        val isPinned = cursor.getInt(cursor.getColumnIndex(Note_Column_isPinned))

                        val localTime = LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss"))

                        val dateModified: LocalDateTime = LocalDateTime.parse(localTime, DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss"))

                        // Create NoteDetails object and add to list
                        val noteDetails = Notes_DataFiles(noteID,userId,noteTitle, noteContent, dateCreated,dateModified, isPinned == 1)
                        notesList.add(noteDetails)
                    } while (cursor.moveToNext())
                }
                cursor.close()
            }
        } catch (e: SQLiteException) {
            return mutableListOf()
        } finally {
            db.close()
        }
        return notesList
    }

    fun addNote(note: Notes_DataFiles): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        cv.put(Note_Column_userID, note.userID)
        cv.put(Note_Column_NoteTitle, note.noteTitle)
        cv.put(Note_Column_NoteContent, note.noteContent)
        cv.put(Note_Column_DateCreated, note.dateCreated)
        cv.put(Note_Column_DateModified, note.dateModified.toString())
        cv.put(Note_Column_isPinned, note.isPinned)

        val success = db.insert(NoteTableName, null, cv)
        db.close()
        return success != -1L
    }

    fun deleteNotes(noteID: Int): Boolean {
        val db = this.writableDatabase
        val selection = "$Note_Column_ID = ?"
        val selectionArgs = arrayOf(noteID.toString())
        val success = db.delete(NoteTableName, selection, selectionArgs)
        db.close()
        return success != -1
    }

    fun updateNotes(note: Notes_DataFiles): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(Note_Column_NoteTitle, note.noteTitle)
        cv.put(Note_Column_NoteContent, note.noteContent)
        cv.put(Note_Column_isPinned, note.isPinned)
        cv.put(Note_Column_DateModified, note.dateModified.toString())
        val selection = "$Note_Column_ID = ?"
        val selectionArgs = arrayOf(note.noteID.toString())
        val success = db.update(NoteTableName, cv, selection, selectionArgs)
        db.close()
        return success != -1
    }

    @SuppressLint("Range")
    fun showAllPinnedNotes(userID: String): MutableList<Notes_DataFiles> {
        val userId = returnUserID(userID)
        val notesList = mutableListOf<Notes_DataFiles>()
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            // Query tasks associated with the given userID
            cursor = db.rawQuery("SELECT * FROM $NoteTableName WHERE $Note_Column_userID = $userId AND $Note_Column_isPinned = 1", null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        // Extract task details from cursor
                        val noteID = cursor.getInt(cursor.getColumnIndex(Note_Column_ID))
                        val noteTitle = cursor.getString(cursor.getColumnIndex(Note_Column_NoteTitle))
                        val noteContent = cursor.getString(cursor.getColumnIndex(Note_Column_NoteContent))
                        val dateCreated = cursor.getString(cursor.getColumnIndex(Note_Column_DateCreated))
                        val isPinned = cursor.getInt(cursor.getColumnIndex(Note_Column_isPinned))

                        val localTime = LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss"))

                        val dateModified: LocalDateTime = LocalDateTime.parse(localTime, DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss"))

                        // Create NoteDetails object and add to list
                        val noteDetails = Notes_DataFiles(noteID,userId,noteTitle, noteContent, dateCreated,dateModified, isPinned == 1)
                        notesList.add(noteDetails)
                    } while (cursor.moveToNext())
                }
                cursor.close()
            }
        } catch (e: SQLiteException) {
            return mutableListOf()
        } finally {
            db.close()
        }
        return notesList
    }

    /**
     * Settings Table Functions
     */

    fun retrieveHideTasks(): Boolean {
        val retrievedID = returnUserID(globalUser)
        val db = this.readableDatabase
        val columns = arrayOf(Settings_Column_HideTasks)
        val selection = "$Settings_Column_userID = ?"
        val selectionArgs = arrayOf(retrievedID.toString())
        val cursor: Cursor =
            db.query(SettingsTableName, columns, selection, selectionArgs, null, null, null)
        var hideTasks = false
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                hideTasks = cursor.getInt(cursor.getColumnIndex(Settings_Column_HideTasks)) == 1
            }
            cursor.close()
        }
        db.close()
        return hideTasks
    }

    fun retrieveTheme(): String {
        val retrievedID = returnUserID(globalUser)
        val db = this.readableDatabase
        var theme = "midnight" // Default theme

        try {
            val columns = arrayOf(Settings_Column_Theme)
            val selection = "$Settings_Column_userID = ?"
            val selectionArgs = arrayOf(retrievedID.toString())
            val cursor: Cursor =
                db.query(SettingsTableName, columns, selection, selectionArgs, null, null, null)

            if (cursor.moveToFirst()) {
                theme = cursor.getString(cursor.getColumnIndex(Settings_Column_Theme))
            }
            cursor.close()
        } catch (e: SQLiteException) {
            // Handle SQLiteException
            e.printStackTrace()
        } finally {
            db.close()
        }

        return theme
    }

    fun updateHideTasks(hideTasks: Boolean): Boolean {
        val retrievedID = returnUserID(globalUser)
        val db = this.writableDatabase

        // Check if settings row exists for the user
        if (!checkSettingsExist(db)) {
            // If not exists, create a new row with default values
            createDefaultSettingsRow(db)
        }

        val cv = ContentValues()
        cv.put(Settings_Column_HideTasks, hideTasks)
        val selection = "$Settings_Column_userID = ?"
        val selectionArgs = arrayOf(retrievedID.toString())
        val success = db.update(SettingsTableName, cv, selection, selectionArgs)
        db.close()
        return success != -1
    }

    fun updateTheme(theme: String): Boolean {
        val retrievedID = returnUserID(globalUser)
        val db = this.writableDatabase

        // Check if settings row exists for the user
        if (!checkSettingsExist(db)) {
            // If not exists, create a new row with default values
            createDefaultSettingsRow(db)
        }

        val cv = ContentValues()
        cv.put(Settings_Column_Theme, theme)
        val selection = "$Settings_Column_userID = ?"
        val selectionArgs = arrayOf(retrievedID.toString())
        val success = db.update(SettingsTableName, cv, selection, selectionArgs)
        db.close()
        return success != -1
    }

    private fun checkSettingsExist(db: SQLiteDatabase): Boolean {
        val retrievedID = returnUserID(globalUser)
        val columns = arrayOf(Settings_Column_userID)
        val selection = "$Settings_Column_userID = ?"
        val selectionArgs = arrayOf(retrievedID.toString())
        val cursor = db.query(SettingsTableName, columns, selection, selectionArgs, null, null, null)
        val exists = cursor != null && cursor.count > 0
        cursor.close()
        return exists
    }

    private fun createDefaultSettingsRow(db: SQLiteDatabase) {
        val retrievedID = returnUserID(globalUser)
        val cv = ContentValues()
        cv.put(Settings_Column_userID, retrievedID)
        cv.put(Settings_Column_HideTasks, 0) // Default value for hideTasks
        cv.put(Settings_Column_Theme, "midnight") // Default value for theme
        cv.put(Settings_Column_DateModified, LocalDate.now().toString())
        db.insert(SettingsTableName, null, cv)
    }


}