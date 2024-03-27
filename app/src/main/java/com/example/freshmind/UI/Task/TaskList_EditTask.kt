package com.example.freshmind.UI.Task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Task_DataFiles
import com.example.freshmind.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class TaskList_EditTask : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var buttonSaveTask: Button
    private lateinit var editTextStartDate: TextView
    private lateinit var editTextEndDate: TextView
    private var taskID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        dbHelper = DBHelper(this)
        editTextTitle = findViewById(R.id.txtEditTask_Title)
        editTextDescription = findViewById(R.id.txtEditTask_Description)
        editTextStartDate = findViewById(R.id.txtEditTask_StartDate)
        editTextEndDate = findViewById(R.id.txtEditTask_EndDate)
        buttonSaveTask = findViewById(R.id.buttonSaveTask)

        // Retrieve task details passed from TaskListFragment
        val intent = intent
        taskID = intent.getIntExtra("taskID", -1)
        val taskTitle = intent.getStringExtra("taskTitle")
        val taskDescription = intent.getStringExtra("taskDescription")
        val taskStartDate = intent.getStringExtra("startTime")
        val taskEndDate = intent.getStringExtra("endTime")
        val startDateCalendar = Calendar.getInstance()
        val endDateCalendar = Calendar.getInstance()

        // Set the retrieved task details to EditText fields
        editTextTitle.setText(taskTitle)
        editTextDescription.setText(taskDescription)
        editTextStartDate.setText(taskStartDate)
        editTextEndDate.setText(taskEndDate)

        editTextStartDate.setOnClickListener { showDateTimePicker(startDateCalendar, editTextStartDate) }
        editTextEndDate.setOnClickListener { showDateTimePicker(endDateCalendar, editTextEndDate)}

        // Set click listener for the Save Task button
        buttonSaveTask.setOnClickListener {
                updateTask()
        }
    }

    private fun updateTask() {
        val newTitle = editTextTitle.text.toString()
        val newDescription = editTextDescription.text.toString()
        val newStartDate = editTextStartDate.text.toString()
        val newEndDate = editTextEndDate.text.toString()
        val startDateCalendar = Calendar.getInstance().apply { time = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse(newStartDate)!! }
        val endDateCalendar = Calendar.getInstance().apply { time = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse(newEndDate)!! }

        // Validate the dates
        if (startDateCalendar.timeInMillis > endDateCalendar.timeInMillis) {
            Toast.makeText(this, "End date cannot be before start date", Toast.LENGTH_SHORT).show()
            editTextEndDate.error = "Invalid Date"
        } else {
            // Continue with updating the task
            val localTime =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss"))
            val dateUpdated: LocalDateTime =
                LocalDateTime.parse(localTime, DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss"))

            // Create a new Task_DataFiles object with updated information
            val updatedTask = Task_DataFiles(taskID, -1, newTitle, newDescription, newStartDate, newEndDate, dateUpdated)
            // Update task in the database
            dbHelper.updateTask(updatedTask)

            setResult(RESULT_OK)
            finish()
        }
    }

    private fun showDateTimePicker(calendar: Calendar, textView: TextView) {
        val datePicker = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                // Set selected date to calendar
                calendar.set(year, monthOfYear, dayOfMonth)
                // Show time picker after selecting date
                showTimePicker(calendar, textView)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.show()
    }

    // Method to show time picker dialog
    private fun showTimePicker(calendar: Calendar, textView: TextView) {
        val timePicker = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                // Set selected time to calendar
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                // Display selected date and time on TextView
                displayDateTime(calendar, textView)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        timePicker.show()
    }

    // Method to display selected date and time on TextView
    private fun displayDateTime(calendar: Calendar, textView: TextView) {
        // Format date and time as per your requirement
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateTimeString = dateFormat.format(calendar.time)
        // Update TextView with selected date and time
        textView.text = dateTimeString
    }
}