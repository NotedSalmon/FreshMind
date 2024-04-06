package com.example.freshmind.UI.Calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Task_DataFiles
import com.example.freshmind.Extras.changeAdapterTextColors
import com.example.freshmind.Extras.changeTextColors
import com.example.freshmind.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarAdapter(private val calendarTasksView: MutableList<Pair<LocalDate, Task_DataFiles>>) : RecyclerView.Adapter<CalendarAdapter.CalendarTaskViewHolder>() {

    private var selectedItemPosition = RecyclerView.NO_POSITION
    private lateinit var dbHelper: DBHelper

    inner class CalendarTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.txtCalendarTask_Title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.txtCalendarTask_Description)
        val startDate: TextView = itemView.findViewById(R.id.txtCalendarTask_StartDate)
        val endDate: TextView = itemView.findViewById(R.id.txtCalendarTask_EndDate)
        init {
            dbHelper = DBHelper(itemView.context)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarTaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false)
        return CalendarTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarTaskViewHolder, position: Int) {
        val (date, task) = calendarTasksView[position]

        // Bind task details to the corresponding TextViews
        holder.titleTextView.text = task.taskTitle
        holder.descriptionTextView.text = task.taskDescription
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedStartDate = task.startTime.format(dateFormatter)
        val formattedEndDate = task.endTime.format(dateFormatter)
        holder.startDate.text = formattedStartDate
        holder.endDate.text = formattedEndDate
        changeAdapterTextColors(holder.itemView.context,holder.titleTextView)

        holder.itemView.isSelected = position == selectedItemPosition
    }

    override fun getItemCount(): Int {
        return calendarTasksView.size
    }

    fun updateTasks(newTasks: List<Pair<LocalDate, Task_DataFiles>>) {
        calendarTasksView.clear()
        calendarTasksView.addAll(newTasks)
        notifyDataSetChanged() // Notify the adapter that the data set has changed
    }
}
