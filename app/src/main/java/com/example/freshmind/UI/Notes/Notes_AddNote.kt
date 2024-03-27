package com.example.freshmind.UI.Notes


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.freshmind.Authentication.globalUser
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Notes_DataFiles
import com.example.freshmind.Database.Task_DataFiles
import com.example.freshmind.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class Notes_AddNote : AppCompatActivity() {
    val dbHelper: DBHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        val btnAddNote = findViewById<View>(R.id.buttonSaveNote)

        btnAddNote.setOnClickListener {
            btnAddNotes()
        }
    }

    private fun btnAddNotes() {
        val noteTitleEditText: EditText = findViewById(R.id.txtCreateNote_Title)
        val noteContentEditText: EditText = findViewById(R.id.txtCreateNote_Content)
        val checkPin: CheckBox = findViewById(R.id.checkIsPinned)
        val userId = dbHelper.returnUserID(globalUser)

        val noteTitle = noteTitleEditText.text.toString()
        val noteContent = noteContentEditText.text.toString()
        val localTime = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss"))
        val dateCreated: LocalDateTime = LocalDateTime.parse(localTime, DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss"))

        var isPinned: Boolean = checkPin.isChecked



        val note = Notes_DataFiles(0,userId,noteTitle,noteContent, localTime, dateCreated, isPinned)
        if (dbHelper.addNote(note)) {
            Toast.makeText(this, "Note created successfully", Toast.LENGTH_LONG).show()
            setResult(RESULT_OK) // Set the result to indicate success
        } else {
            Toast.makeText(this, "Error: Note not created", Toast.LENGTH_SHORT).show()
        }
        finish() // Finish the activity

    }

}
