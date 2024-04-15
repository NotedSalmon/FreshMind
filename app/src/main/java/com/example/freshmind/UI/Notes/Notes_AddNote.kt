package com.example.freshmind.UI.Notes


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.freshmind.Authentication.globalUser
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Notes_DataFiles
import com.example.freshmind.Database.Task_DataFiles
import com.example.freshmind.Extras.changeEditBoxColor
import com.example.freshmind.Extras.changeTextBoxColor
import com.example.freshmind.Extras.changeTextColors
import com.example.freshmind.Extras.changeTextColorsNT
import com.example.freshmind.Extras.changeTitleColor
import com.example.freshmind.Extras.getColorResource
import com.example.freshmind.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class Notes_AddNote : AppCompatActivity() {
    val dbHelper: DBHelper = DBHelper(this)
    private lateinit var noteTitleEditText: EditText
    private lateinit var noteContentEditText: EditText
    private lateinit var checkPin: CheckBox
    private lateinit var btnAddNote: Button
    private lateinit var txtTitleCreateNote: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)
        val rootLayout = findViewById<View>(android.R.id.content)
        rootLayout.setBackgroundColor(ContextCompat.getColor(this, getColorResource(this)))
        noteTitleEditText = findViewById(R.id.txtCreateNote_Title)
        noteContentEditText= findViewById(R.id.txtCreateNote_Content)
        checkPin= findViewById(R.id.checkIsPinned)
        txtTitleCreateNote = findViewById(R.id.txtTitleCreateNote)

        btnAddNote = findViewById(R.id.buttonSaveNote)

        btnAddNote.setOnClickListener {
            btnAddNotes()
        }

        changeTitleColor(this, txtTitleCreateNote)
        changeEditBoxColor(this, noteTitleEditText, noteContentEditText)
        changeTextBoxColor(this, checkPin, btnAddNote)
        changeTextColorsNT(this , checkPin, btnAddNote, noteTitleEditText, noteContentEditText)
    }

    private fun btnAddNotes() {
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
