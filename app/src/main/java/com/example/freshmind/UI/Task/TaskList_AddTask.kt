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
import com.example.freshmind.Authentication.globalUser
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
 * This class is the activity for adding a task
 * It allows the user to add a new task with a title, description, start date, and end date
 */
class TaskList_AddTask : AppCompatActivity() {
    val dbHelper: DBHelper = DBHelper(this)
    private lateinit var taskTitleEditText: EditText
    private lateinit var taskDescriptionEditText: EditText
    private lateinit var txtTitleCreateTask: TextView

    /**
     * This method is called when the activity is created
     * It initializes the views and sets the click listeners
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        val rootLayout = findViewById<View>(android.R.id.content)
        rootLayout.setBackgroundColor(ContextCompat.getColor(this, getColorResource(this)))

        val btnAddTask = findViewById<Button>(R.id.buttonSaveTask)
        val startDateCalendar = Calendar.getInstance()
        val endDateCalendar = Calendar.getInstance()
        val txtStartDate = findViewById<TextView>(R.id.txtCreateTask_StartDate)
        val txtEndDate = findViewById<TextView>(R.id.txtCreateTask_EndDate)
        taskTitleEditText = findViewById(R.id.txtCreateTask_Title)
        taskDescriptionEditText = findViewById(R.id.txtCreateTask_Description)
        txtTitleCreateTask = findViewById(R.id.txtTitleCreateTask)

        txtStartDate.setOnClickListener { showDateTimePicker(startDateCalendar, txtStartDate) }
        txtEndDate.setOnClickListener { showDateTimePicker(endDateCalendar, txtEndDate) }

        btnAddTask.setOnClickListener {
            if (startDateCalendar.timeInMillis > endDateCalendar.timeInMillis) {
                Toast.makeText(this, "End date cannot be before start date", Toast.LENGTH_SHORT).show()
                txtEndDate.error
            } else { btnAddTask() } }
        // Change colors
        changeTitleColor(this, txtTitleCreateTask)
        changeEditBoxColor(this, taskTitleEditText, taskDescriptionEditText)
        changeTextBoxColor(this, txtStartDate, txtEndDate)
        changeButtonColor(this, btnAddTask)
        changeTextColorsNT(this , taskTitleEditText, taskDescriptionEditText, txtStartDate, txtEndDate)
    }

    /**
     * This method is called when the user clicks the Add Task button
     * It retrieves the task details from the EditTexts and TextViews
     * It then adds the task to the database
     */
    private fun btnAddTask() {
        val startDateTextView: TextView = findViewById(R.id.txtCreateTask_StartDate)
        val endDateTextView: TextView = findViewById(R.id.txtCreateTask_EndDate)

        val taskTitle = taskTitleEditText.text.toString()
        val taskDescription = taskDescriptionEditText.text.toString()
        val startDateString = startDateTextView.text.toString()
        val endDateString = endDateTextView.text.toString()

        // checks if the startDate string is empty
        if (startDateString.isEmpty()) {
            Toast.makeText(this, "Please select a start date", Toast.LENGTH_SHORT).show()
            startDateTextView.error
            return
        }

        // format the date
        val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val startDate = LocalDate.parse(startDateString, dateFormat)

        val localTime = LocalDate.now()

        if (endDateString.isEmpty()) {
            val endDate = startDate
            val userId = dbHelper.returnUserID(globalUser)
            val task = Task_DataFiles(0, userId, taskTitle, taskDescription, startDate, endDate, localTime)
            if (dbHelper.addTask(task)) {
                Toast.makeText(this, "Task created successfully", Toast.LENGTH_LONG).show()
                setResult(RESULT_OK)
                finish() // Closes the activity and returns to the TaskListFragment
            } else {
                Toast.makeText(this, "Error: Task not created", Toast.LENGTH_SHORT).show()
            }
        } else { // if the end date is not empty
            val endDate = LocalDate.parse(endDateString, dateFormat) // parse the end date

            if (startDate.isAfter(endDate)) { // check if the start date is after the end date
                Toast.makeText(this, "End date cannot be before start date", Toast.LENGTH_SHORT).show()
                return
            }
            val userId = dbHelper.returnUserID(globalUser)
            val task = Task_DataFiles(0, userId, taskTitle, taskDescription, startDate, endDate, localTime)
            if (dbHelper.addTask(task)) { // add the task to the database
                Toast.makeText(this, "Task created successfully", Toast.LENGTH_LONG).show()
                setResult(RESULT_OK)
                finish() // Closes the activity and returns to the TaskListFragment
            } else {
                Toast.makeText(this, "Error: Task not created", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * This method shows the date picker dialog, allowing the user to select a date
     */
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
        // Format date and time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateTimeString = dateFormat.format(calendar.time)
        // Update TextView with selected date and time
        textView.text = dateTimeString
    }

}
