package com.example.freshmind.UI.Notes

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Notes_DataFiles
import com.example.freshmind.Extras.changeEditBoxColor
import com.example.freshmind.Extras.changeTextBoxColor
import com.example.freshmind.Extras.changeTextColorsNT
import com.example.freshmind.Extras.changeTitleColor
import com.example.freshmind.Extras.getColorResource
import com.example.freshmind.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * This activity allows the user to edit a note
 * It retrieves the note details from the intent and displays them in the EditText fields
 */
class Notes_EditNote : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText
    private lateinit var checkboxIsPinned: CheckBox
    private lateinit var buttonSaveNote: Button
    private lateinit var noteDateCreated: String
    private lateinit var txtTitleEditNote: TextView
    private var noteID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        val rootLayout = findViewById<View>(android.R.id.content)
        rootLayout.setBackgroundColor(ContextCompat.getColor(this, getColorResource(this))) // Set background color

        dbHelper = DBHelper(this) // Initialize DBHelper in onCreate()
        editTextTitle = findViewById(R.id.txtEditNote_Title)
        editTextContent = findViewById(R.id.txtEditNote_Content)
        checkboxIsPinned = findViewById(R.id.checkIsPinned)
        buttonSaveNote = findViewById(R.id.buttonSaveNote)
        txtTitleEditNote = findViewById(R.id.txtTitleEditNote)

        // Retrieve task details passed from NoteFragment
        val intent = intent
        noteID = intent.getIntExtra("noteID", -1)
        val noteTitle = intent.getStringExtra("noteTitle")
        val noteContent = intent.getStringExtra("noteContent")
        noteDateCreated = intent.getStringExtra("noteDateCreated").toString()
        val noteIsPinned = intent.getBooleanExtra("checkPin", false)

        // Set the retrieved task details to EditText fields
        editTextTitle.setText(noteTitle)
        editTextContent.setText(noteContent)
        checkboxIsPinned.isChecked = noteIsPinned

        // Set click listener for the Save Task button
        buttonSaveNote.setOnClickListener {
            updateNote()
        }

        // Change colours of the text and background
        changeTitleColor(this, txtTitleEditNote)
        changeEditBoxColor(this, editTextTitle, editTextContent)
        changeTextBoxColor(this, checkboxIsPinned, buttonSaveNote)
        changeTextColorsNT(this , editTextTitle, checkboxIsPinned, buttonSaveNote, editTextContent)
    }

    /**
     *  This function updates the note in the database with the new information
     *  It retrieves the new title and content from the EditText fields
     */
    private fun updateNote() {
        val newTitle = editTextTitle.text.toString()
        val newContent = editTextContent.text.toString()
        val checkPin: CheckBox = findViewById(R.id.checkIsPinned)
        var isPinned: Boolean = checkPin.isChecked
        val localTime =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss")) // Get current time
        val dateUpdated: LocalDateTime =
            LocalDateTime.parse(localTime, DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss")) // Parse the current time

        // Create a new Note_DataFiles object with updated information
        val updatedNote = Notes_DataFiles(noteID, -1, newTitle, newContent, noteDateCreated, dateUpdated, isPinned)
        // Update note in the database
        dbHelper.updateNotes(updatedNote)

        setResult(Activity.RESULT_OK)
        finish() // Finish the activity
    }
}