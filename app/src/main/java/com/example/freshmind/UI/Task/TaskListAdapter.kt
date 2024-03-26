package com.example.freshmind.UI.Task

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Task_DataFiles
import com.example.freshmind.R
import com.example.freshmind.UI.Task.TaskListFragment
import com.example.freshmind.UI.Task.TaskList_EditTask

class TaskAdapter(private val tasks: MutableList<Task_DataFiles>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

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
                    iconEditTask(position, itemView.context)
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

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleTextView.text = task.taskTitle
        holder.descriptionTextView.text = task.taskDescription
        holder.startDate.text = task.startTime
        holder.endDate.text = task.endTime
        holder.currentTaskID = task.taskID

        // Highlight selected item
        holder.itemView.isSelected = position == selectedItemPosition

        if (selectedItemPosition == position) {
            holder.itemView.setBackgroundResource(R.color.purple_200)
            holder.deleteIcon.visibility = View.VISIBLE
            holder.editIcon.visibility = View.VISIBLE
        } else {
            holder.itemView.setBackgroundResource(R.color.white)
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

    /**
     * This function is called when the edit icon is clicked
     * It opens the TaskList_EditTask activity with the task details
     */
    fun iconEditTask(position: Int, context: Context) {
        val task = tasks[position]
        val intent = Intent(context, TaskList_EditTask::class.java).apply {
            putExtra("taskID", task.taskID)
            putExtra("taskTitle", task.taskTitle)
            putExtra("taskDescription", task.taskDescription)
            putExtra("startTime", task.startTime)
            putExtra("endTime", task.endTime)
        }
        context.startActivity(intent)
    }

}
