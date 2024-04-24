package com.example.freshmind.UI.Calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Task_DataFiles
import com.example.freshmind.Extras.changeAdapterTextColors
import com.example.freshmind.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Adapter for the RecyclerView in the CalendarFragment
 */
class CalendarAdapter(private val calendarTasksView: MutableList<Pair<LocalDate, Task_DataFiles>>) : RecyclerView.Adapter<CalendarAdapter.CalendarTaskViewHolder>() {

    private var selectedItemPosition = RecyclerView.NO_POSITION // Position of the selected item
    private lateinit var dbHelper: DBHelper

    /**
     * ViewHolder for the RecyclerView
     */
    inner class CalendarTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.txtCalendarTask_Title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.txtCalendarTask_Description)
        val startDate: TextView = itemView.findViewById(R.id.txtCalendarTask_StartDate)
        val endDate: TextView = itemView.findViewById(R.id.txtCalendarTask_EndDate)
        init {
            dbHelper = DBHelper(itemView.context)
        }
    }

    /**
     * Inflates the item_calendar layout and returns a new CalendarTaskViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarTaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false)
        return CalendarTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarTaskViewHolder, position: Int) {
        val (date, task) = calendarTasksView[position] // Get the task at the current position

        // Bind task details to the corresponding TextViews
        holder.titleTextView.text = task.taskTitle
        holder.descriptionTextView.text = task.taskDescription
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy") // Date format
        val formattedStartDate = task.startTime.format(dateFormatter)
        val formattedEndDate = task.endTime.format(dateFormatter)
        holder.startDate.text = formattedStartDate // Set the formatted date
        holder.endDate.text = formattedEndDate
        changeAdapterTextColors(holder.itemView.context,holder.titleTextView) // Change the text color of the views

        holder.itemView.isSelected = position == selectedItemPosition // Set the item as selected if it is the selected item
    }

    override fun getItemCount(): Int {
        return calendarTasksView.size
    }

    /**
     * Updates the tasks in the RecyclerView
     */
    fun updateTasks(newTasks: List<Pair<LocalDate, Task_DataFiles>>) {
        calendarTasksView.clear()
        calendarTasksView.addAll(newTasks)
        notifyDataSetChanged() // Notify the adapter that the data set has changed
    }
}
