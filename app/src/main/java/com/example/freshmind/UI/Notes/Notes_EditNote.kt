package com.example.freshmind.UI.Notes

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Notes_DataFiles
import com.example.freshmind.Database.Task_DataFiles
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
    private lateinit var buttonSaveTask: Button
    private lateinit var noteDateCreated: String
    private var noteID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        dbHelper = DBHelper(this)
        editTextTitle = findViewById(R.id.txtEditNote_Title)
        editTextContent = findViewById(R.id.txtEditNote_Content)
        buttonSaveTask = findViewById(R.id.buttonSaveNote)

        // Retrieve task details passed from NoteFragment
        val intent = intent
        noteID = intent.getIntExtra("noteID", -1)
        val noteTitle = intent.getStringExtra("noteTitle")
        val noteContent = intent.getStringExtra("noteContent")
        val noteDateCreated = intent.getStringExtra("startTime")

        // Set the retrieved task details to EditText fields
        editTextTitle.setText(noteTitle)
        editTextContent.setText(noteContent)

        // Set click listener for the Save Task button
        buttonSaveTask.setOnClickListener {
            updateTask()
        }
    }

    private fun updateTask() {
        val newTitle = editTextTitle.text.toString()
        val newContent = editTextContent.text.toString()
        // Validate the dates

        val localTime =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss"))
        val dateUpdated: LocalDateTime =
            LocalDateTime.parse(localTime, DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss"))

            // Create a new Task_DataFiles object with updated information
        val updatedNote = Notes_DataFiles(noteID, -1, newTitle, newContent, noteDateCreated, dateUpdated)
            // Update task in the database
        dbHelper.updateNotes(updatedNote)

            // Close the activity and return to TaskListFragment
        finish()

    }
}