package com.example.freshmind.UI.Task

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Task_DataFiles
import com.example.freshmind.Extras.changeButtonColor
import com.example.freshmind.Extras.changeEditBoxColor
import com.example.freshmind.Extras.changeTextBoxColor
import com.example.freshmind.Extras.changeTextColorsNT
import com.example.freshmind.Extras.changeTitleColor
import com.example.freshmind.Extras.getColorResource
import com.example.freshmind.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

/**
 * This class is the activity for editing a task
 * It allows the user to edit the title, description, start date, and end date of a task
 */
class TaskList_EditTask : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var buttonSaveTask: Button
    private lateinit var editTextStartDate: TextView
    private lateinit var editTextEndDate: TextView
    private lateinit var txtTitleEditTask: TextView
    private var taskID: Int = -1

    /**
     * This method is called when the activity is created
     * It initializes the views and sets the click listeners
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)
        val rootLayout = findViewById<View>(android.R.id.content)
        rootLayout.setBackgroundColor(ContextCompat.getColor(this, getColorResource(this)))

        dbHelper = DBHelper(this)
        editTextTitle = findViewById(R.id.txtEditTask_Title)
        editTextDescription = findViewById(R.id.txtEditTask_Description)
        editTextStartDate = findViewById(R.id.txtEditTask_StartDate)
        editTextEndDate = findViewById(R.id.txtEditTask_EndDate)
        buttonSaveTask = findViewById(R.id.buttonSaveTask)
        txtTitleEditTask = findViewById(R.id.txtTitleEditTask)

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
        editTextStartDate.text = taskStartDate
        editTextEndDate.text = taskEndDate

        editTextStartDate.setOnClickListener { showDateTimePicker(startDateCalendar, editTextStartDate) }
        editTextEndDate.setOnClickListener { showDateTimePicker(endDateCalendar, editTextEndDate)}

        buttonSaveTask.setOnClickListener {
                updateTask()
        }

        // Change the text and other colors matching the global theme
        changeTitleColor(this, txtTitleEditTask)
        changeEditBoxColor(this, editTextTitle, editTextDescription)
        changeButtonColor(this, buttonSaveTask)
        changeTextBoxColor(this, editTextStartDate, editTextEndDate)
        changeTextColorsNT(this , editTextTitle, editTextDescription, editTextStartDate, editTextEndDate)
    }

    /**
     * This method is called when the user clicks the Save button
     * It updates the task details in the database
     */
    private fun updateTask() {
        val newTitle = editTextTitle.text.toString()
        val newDescription = editTextDescription.text.toString()
        val newStartDate = editTextStartDate.text.toString()
        val newEndDate = editTextEndDate.text.toString()

        // Parse new start and end dates as LocalDate objects
        val startDate = LocalDate.parse(newStartDate, DateTimeFormatter.ISO_DATE)
        var endDate = LocalDate.parse(newEndDate, DateTimeFormatter.ISO_DATE)

        // Validate the dates
        if (startDate.isAfter(endDate)) {
            Toast.makeText(this, "End date cannot be before start date", Toast.LENGTH_SHORT).show()
            editTextEndDate.error = "Invalid Date"
        } else if (startDate.isBefore(LocalDate.now())) {
            Toast.makeText(this, "Start date cannot be in the past", Toast.LENGTH_SHORT).show()
            editTextStartDate.error = "Invalid Date"
        } else if (newStartDate.isEmpty()) {
            Toast.makeText(this, "Select a start date", Toast.LENGTH_SHORT).show()
        } else if (newEndDate.isEmpty() && newStartDate.isNotEmpty()) {
            endDate = startDate
        }
        else {
            val localTime = LocalDate.now()

            val updatedTask = Task_DataFiles(taskID, -1, newTitle, newDescription, startDate, endDate, localTime)
            dbHelper.updateTask(updatedTask)

            setResult(RESULT_OK) // Sets the activity result to OK which will refresh the task list
            finish() // Closes the activity and returns to the TaskListFragment
        }
    }

    /**
     * This method shows the date picker dialog, allowing the user to select a date
     */
    private fun showDateTimePicker(calendar: Calendar, textView: TextView) {
        val datePicker = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                // Check if the selected date is after the current date
                val selectedDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                val currentDate = LocalDate.now()
                if (selectedDate.isBefore(currentDate)) {
                    // Show a toast indicating that the selected date is invalid
                    Toast.makeText(this, "Please select a date from today onwards", Toast.LENGTH_SHORT).show()
                } else {
                    // Set selected date to calendar
                    calendar.set(year, monthOfYear, dayOfMonth)
                    // Display selected date on TextView
                    displayDate(calendar, textView)
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Set the minimum date to the current date
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.show() // Show the date picker dialog
    }


    // Method to display selected date and time on TextView
    private fun displayDate(calendar: Calendar, textView: TextView) {
        // Format date and time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateTimeString = dateFormat.format(calendar.time)
        // Update TextView with selected date and time
        textView.text = dateTimeString
    }
}