package com.example.freshmind.UI.Notes

import android.app.Activity
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
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Notes_DataFiles
import com.example.freshmind.Database.Task_DataFiles
import com.example.freshmind.Extras.changeEditBoxColor
import com.example.freshmind.Extras.changeTextBoxColor
import com.example.freshmind.Extras.changeTextColors
import com.example.freshmind.Extras.getColorResource
import com.example.freshmind.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class Notes_EditNote : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText
    private lateinit var checkboxIsPinned: CheckBox
    private lateinit var buttonSaveTask: Button
    private lateinit var noteDateCreated: String
    private var noteID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        val rootLayout = findViewById<View>(android.R.id.content)
        rootLayout.setBackgroundColor(ContextCompat.getColor(this, getColorResource(this)))

        dbHelper = DBHelper(this)
        editTextTitle = findViewById(R.id.txtEditNote_Title)
        editTextContent = findViewById(R.id.txtEditNote_Content)
        checkboxIsPinned = findViewById(R.id.checkIsPinned)
        buttonSaveTask = findViewById(R.id.buttonSaveNote)

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
        buttonSaveTask.setOnClickListener {
            updateTask()
        }

        changeEditBoxColor(this, editTextTitle, editTextContent)
        changeTextBoxColor(this, checkboxIsPinned, buttonSaveTask)
        changeTextColors(this , editTextTitle, checkboxIsPinned, buttonSaveTask, editTextContent)
    }

    private fun updateTask() {
        val newTitle = editTextTitle.text.toString()
        val newContent = editTextContent.text.toString()
        val checkPin: CheckBox = findViewById(R.id.checkIsPinned)
        var isPinned: Boolean = checkPin.isChecked
        val localTime =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss"))
        val dateUpdated: LocalDateTime =
            LocalDateTime.parse(localTime, DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss"))

            // Create a new Note_DataFiles object with updated information
        val updatedNote = Notes_DataFiles(noteID, -1, newTitle, newContent, noteDateCreated, dateUpdated, isPinned)
            // Update note in the database
        dbHelper.updateNotes(updatedNote)

        setResult(Activity.RESULT_OK)
        finish()

    }
}