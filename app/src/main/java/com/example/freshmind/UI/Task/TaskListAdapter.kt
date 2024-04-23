package com.example.freshmind.UI.Task

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Task_DataFiles
import com.example.freshmind.Extras.changeAdapterTextColors
import com.example.freshmind.Extras.changeTextColors
import com.example.freshmind.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ClosestTasksAdapter(private val tasks: MutableList<Task_DataFiles>) : RecyclerView.Adapter<ClosestTasksAdapter.ClosestTasksViewHolder>() {

    private var selectedItemPosition = RecyclerView.NO_POSITION
    private lateinit var dbHelper: DBHelper

    /**
     * This class is the view holder for the RecyclerView
     * It holds the views for the item_task layout
     * It also sets the click listeners for the delete and edit icons
     */

    inner class ClosestTasksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.txtTask_Title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.txtTask_Description)
        val deleteIcon: ImageView = itemView.findViewById(R.id.icon_DeleteTask)
        val editIcon: ImageView = itemView.findViewById(R.id.icon_EditTask)
        val startDate : TextView = itemView.findViewById(R.id.txtTask_StartDate)
        val endDate : TextView = itemView.findViewById(R.id.txtTask_EndDate)

        init {
            dbHelper = DBHelper(itemView.context)
            deleteIcon.visibility = View.GONE
            editIcon.visibility = View.GONE
            // Set item click listener
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(selectedItemPosition)
                    selectedItemPosition = position
                    notifyItemChanged(selectedItemPosition)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClosestTasksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return ClosestTasksViewHolder(view)
    }

    /**
     * This function is called when the RecyclerView needs to display an item
     * It binds the data to the view holder
     */

    override fun onBindViewHolder(holder: ClosestTasksAdapter.ClosestTasksViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleTextView.text = task.taskTitle
        holder.descriptionTextView.text = task.taskDescription
        changeAdapterTextColors(holder.itemView.context,holder.titleTextView)

        // Format start and end dates
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedStartDate = task.startTime.format(dateFormatter)
        val formattedEndDate = task.endTime.format(dateFormatter)
        holder.startDate.text = formattedStartDate
        holder.endDate.text = formattedEndDate
    }


    override fun getItemCount(): Int {
        return tasks.size
    }
}

class TaskAdapter(private val tasks: MutableList<Task_DataFiles>, private val editTaskClickListener: EditTaskClickListener) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var selectedItemPosition = RecyclerView.NO_POSITION
    private lateinit var dbHelper: DBHelper

    /**
     * This class is the view holder for the RecyclerView
     * It holds the views for the item_task layout
     * It also sets the click listeners for the delete and edit icons
     */

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.txtTask_Title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.txtTask_Description)
        val deleteIcon: ImageView = itemView.findViewById(R.id.icon_DeleteTask)
        val editIcon: ImageView = itemView.findViewById(R.id.icon_EditTask)
        val startDate : TextView = itemView.findViewById(R.id.txtTask_StartDate)
        val endDate : TextView = itemView.findViewById(R.id.txtTask_EndDate)
        var currentTaskID: Int = -1
        init {
            dbHelper = DBHelper(itemView.context)
            // Set item click listener
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(selectedItemPosition)
                    selectedItemPosition = position
                    notifyItemChanged(selectedItemPosition)
                }
            }

            deleteIcon.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    iconDeleteTask(position)
                }
            }

            editIcon.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    editTaskClickListener.iconEditTask(position, itemView.context)
                }
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    /**
     * This function is called when the RecyclerView needs to display an item
     * It binds the data to the view holder
     */

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val context : Context = holder.itemView.context
        val task = tasks[position]
        holder.titleTextView.text = task.taskTitle
        holder.descriptionTextView.text = task.taskDescription

        // Format start and end dates
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedStartDate = task.startTime.format(dateFormatter)
        val formattedEndDate = task.endTime.format(dateFormatter)
        holder.startDate.text = formattedStartDate
        holder.endDate.text = formattedEndDate

        // Set text color based on date
        val currentDate = LocalDate.now()
        if (task.endTime.isBefore(currentDate)) {
            // Task end date is before today's date, set text color to red
            holder.endDate.setTextColor(ContextCompat.getColor(context, R.color.red))
        } else {
            // Task end date is today or in the future, set text color to default
            holder.endDate.setTextColor(ContextCompat.getColor(context, R.color.lightGray))
        }

        holder.currentTaskID = task.taskID

        // Highlight selected item
        holder.itemView.isSelected = position == selectedItemPosition

        if (selectedItemPosition == position) {
            holder.itemView.setBackgroundResource(R.color.deepPurple)
            holder.titleTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.gold))
            holder.deleteIcon.visibility = View.VISIBLE
            holder.editIcon.visibility = View.VISIBLE
        } else {
            holder.itemView.setBackgroundResource(R.color.backgroundIndigo)
            changeTextColors(holder.itemView.context,holder.titleTextView)
            holder.deleteIcon.visibility = View.GONE
            holder.editIcon.visibility = View.GONE
        }
    }



    override fun getItemCount(): Int {
        return tasks.size
    }

    fun iconDeleteTask(position: Int) {
        val deletedTaskID = tasks[position].taskID
        tasks.removeAt(position)
        notifyItemRemoved(position)
        dbHelper.deleteTask(deletedTaskID)
    }

    interface EditTaskClickListener {
        fun iconEditTask(position: Int, context: Context)
    }

}
