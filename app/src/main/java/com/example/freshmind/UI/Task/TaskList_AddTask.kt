package com.example.freshmind.UI.Task


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.freshmind.Authentication.globalUser
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Task_DataFiles
import com.example.freshmind.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class TaskList_AddTask : AppCompatActivity() {
    val dbHelper: DBHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.freshmind.R.layout.activity_create_task)

        val btnAddTask = findViewById<View>(R.id.buttonSaveTask)
        val startDateCalendar = Calendar.getInstance()
        val endDateCalendar = Calendar.getInstance()
        val txtStartDate = findViewById<TextView>(R.id.txtCreateTask_StartDate)
        val txtEndDate = findViewById<TextView>(R.id.txtCreateTask_EndDate)

        txtStartDate.setOnClickListener { showDateTimePicker(startDateCalendar, txtStartDate) }
        txtEndDate.setOnClickListener { showDateTimePicker(endDateCalendar, txtEndDate) }
        btnAddTask.setOnClickListener { btnAddTask() }

    }

    private fun btnAddTask() {
        val taskTitleEditText: EditText = findViewById(R.id.txtCreateTask_Title)
        val taskDescriptionEditText: EditText = findViewById(R.id.txtCreateTask_Description)
        val startDateTextView: TextView = findViewById(R.id.txtCreateTask_StartDate)
        val endDateTextView: TextView = findViewById(R.id.txtCreateTask_EndDate)

        val taskTitle = taskTitleEditText.text.toString()
        val taskDescription = taskDescriptionEditText.text.toString()
        val startDate = startDateTextView.text.toString()
        val endDate = endDateTextView.text.toString()

        val localTime = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss"))

        val dateCreated: LocalDateTime = LocalDateTime.parse(localTime, DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss"))

        val userId = dbHelper.returnUserID(globalUser)

        val task = Task_DataFiles(0,userId,taskTitle,taskDescription,startDate,endDate, dateCreated)
        if(dbHelper.addTask(task)){
            Toast.makeText(this, "Task created successfully", Toast.LENGTH_LONG).show()
            finish()
        }
        else Toast.makeText(this, "Error: Task not created", Toast.LENGTH_SHORT).show() //Error message

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
