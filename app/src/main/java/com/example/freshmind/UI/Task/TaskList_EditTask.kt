package com.example.freshmind.UI.Task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import com.example.freshmind.Extras.changeEditBoxColor
import com.example.freshmind.Extras.changeTextBoxColor
import com.example.freshmind.Extras.changeTextColors
import com.example.freshmind.Extras.changeTextColorsNT
import com.example.freshmind.Extras.changeTitleColor
import com.example.freshmind.Extras.getColorResource
import com.example.freshmind.R
import java.text.SimpleDateFormat
import java.time.LocalDate
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
    private lateinit var txtTitleEditTask: TextView
    private var taskID: Int = -1

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
        editTextStartDate.setText(taskStartDate)
        editTextEndDate.setText(taskEndDate)

        editTextStartDate.setOnClickListener { showDateTimePicker(startDateCalendar, editTextStartDate) }
        editTextEndDate.setOnClickListener { showDateTimePicker(endDateCalendar, editTextEndDate)}

        // Set click listener for the Save Task button
        buttonSaveTask.setOnClickListener {
                updateTask()
        }

        changeTitleColor(this, txtTitleEditTask)
        changeEditBoxColor(this, editTextTitle, editTextDescription)
        changeTextBoxColor(this, buttonSaveTask, editTextStartDate, editTextEndDate)
        changeTextColorsNT(this , editTextTitle, editTextDescription, editTextStartDate, editTextEndDate)
    }

    private fun updateTask() {
        val newTitle = editTextTitle.text.toString()
        val newDescription = editTextDescription.text.toString()
        val newStartDate = editTextStartDate.text.toString()
        val newEndDate = editTextEndDate.text.toString()

        // Parse new start and end dates as LocalDate objects
        val startDate = LocalDate.parse(newStartDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val endDate = LocalDate.parse(newEndDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        // Validate the dates
        if (startDate.isAfter(endDate)) {
            Toast.makeText(this, "End date cannot be before start date", Toast.LENGTH_SHORT).show()
            editTextEndDate.error = "Invalid Date"
        } else {
            val localTime = LocalDate.now()

            val updatedTask = Task_DataFiles(taskID, -1, newTitle, newDescription, startDate, endDate, localTime)
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
                // Display selected date on TextView
                displayDate(calendar, textView)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.show()
    }

    // Method to display selected date and time on TextView
    private fun displayDate(calendar: Calendar, textView: TextView) {
        // Format date and time as per your requirement
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateTimeString = dateFormat.format(calendar.time)
        // Update TextView with selected date and time
        textView.text = dateTimeString
    }
}