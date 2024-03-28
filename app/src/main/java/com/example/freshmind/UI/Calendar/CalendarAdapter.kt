package com.example.freshmind.UI.Calendar


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Task_DataFiles
import com.example.freshmind.R
import java.time.LocalDate

class CalendarAdapter(private val calendarTasks: MutableMap<LocalDate, List<Task_DataFiles>>, private val selectedDate: LocalDate?) : RecyclerView.Adapter<CalendarAdapter.CalendarTaskViewHolder>() {

    private var selectedItemPosition = RecyclerView.NO_POSITION
    private lateinit var dbHelper: DBHelper

    /**
     * This class is the view holder for the RecyclerView
     * It holds the views for the item_task layout
     * It also sets the click listeners for the delete and edit icons
     */

    inner class CalendarTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.txtCalendarTask_Title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.txtCalendarTask_Description)
        val startDate : TextView = itemView.findViewById(R.id.txtCalendarTask_StartDate)
        val endDate : TextView = itemView.findViewById(R.id.txtCalendarTask_EndDate)
        init {
            dbHelper = DBHelper(itemView.context)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarTaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false)
        return CalendarTaskViewHolder(view)
    }

    /**
     * This function is called when the RecyclerView needs to display an item
     * It binds the data to the view holder
     */

    override fun onBindViewHolder(holder: CalendarTaskViewHolder, position: Int) {
        val dates = calendarTasks.keys.toList()
        val date = dates[position]

        if (date == selectedDate) {
            val tasks = calendarTasks[date] ?: emptyList()

            // Display tasks for the selected date
            tasks.forEachIndexed { index, task ->
                holder.titleTextView.append(task.taskTitle)
                holder.descriptionTextView.append(task.taskDescription)
                holder.startDate.append(task.startTime)
                holder.endDate.append(task.endTime)

                if (index < tasks.size - 1) {
                    holder.titleTextView.append("\n")
                    holder.descriptionTextView.append("\n")
                    holder.startDate.append("\n")
                    holder.endDate.append("\n")
                }
            }
        } else {
            // Clear the text fields if the date does not match the selected date
            holder.titleTextView.text = ""
            holder.descriptionTextView.text = ""
            holder.startDate.text = ""
            holder.endDate.text = ""
        }

        holder.itemView.isSelected = position == selectedItemPosition
    }

    override fun getItemCount(): Int {
        return calendarTasks.size
    }

    fun updateTasks(newTasks: MutableMap<LocalDate, List<Task_DataFiles>>) {
        calendarTasks.clear()
        calendarTasks.putAll(newTasks)
        notifyDataSetChanged() // Notify the adapter that the data set has changed
    }

}
