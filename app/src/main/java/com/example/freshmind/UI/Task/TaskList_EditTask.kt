package com.example.freshmind.UI.Task

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Task_DataFiles
import com.example.freshmind.R

class TaskList_EditTask : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var buttonSaveTask: Button
    private var taskID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        dbHelper = DBHelper(this)
        editTextTitle = findViewById(R.id.editTextTitle)
        editTextDescription = findViewById(R.id.editTextDescription)
        buttonSaveTask = findViewById(R.id.buttonSaveTask)

        // Retrieve task details passed from TaskListFragment
        val intent = intent
        taskID = intent.getIntExtra("taskID", -1)
        val taskTitle = intent.getStringExtra("taskTitle")
        val taskDescription = intent.getStringExtra("taskDescription")

        // Set the retrieved task details to EditText fields
        editTextTitle.setText(taskTitle)
        editTextDescription.setText(taskDescription)

        // Set click listener for the Save Task button
        buttonSaveTask.setOnClickListener {
            // Update task with new information
            updateTask()
        }
    }

    private fun updateTask() {
        val newTitle = editTextTitle.text.toString()
        val newDescription = editTextDescription.text.toString()

        // Create a new Task_DataFiles object with updated information
        val updatedTask = Task_DataFiles(taskID, -1, newTitle, newDescription, "null", "null", null)

        // Update task in the database
        dbHelper.updateTask(updatedTask)

        // Close the activity and return to TaskListFragment
        finish()
    }
}