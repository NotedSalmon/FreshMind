package com.example.freshmind.UI.Task

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.freshmind.Authentication.globalUser
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Task_DataFiles
import com.example.freshmind.Extras.getColorResource
import com.example.freshmind.R
import com.example.freshmind.UI.Settings.isExpiredTasksEnabled

/**
 * This is the TaskListFragment class that displays the list of tasks
 */
class TaskListFragment : Fragment(), TaskAdapter.EditTaskClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private val tasks: MutableList<Task_DataFiles> = mutableListOf() // Initialize an empty list
    private lateinit var dbHelper: DBHelper
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DBHelper(requireContext()) // Initialize DBHelper in onCreate()
    }

    /**
     * This method inflates the layout for the fragment
     * It also sets up the RecyclerView and SwipeRefreshLayout
     * It also loads the data from the database
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)
        view?.setBackgroundColor(resources.getColor(getColorResource(requireContext())))
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        taskAdapter = TaskAdapter(tasks, this)
        recyclerView.adapter = taskAdapter

        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
        loadData()
        return view
    }

    /**
     * This method loads the data from the database and checks if expired tasks is enabled
     * If expired tasks is enabled, it hides the expired tasks
     * If expired tasks is disabled, it shows all tasks
     */
    private fun loadData() {
        if (isExpiredTasksEnabled) {
            val validTasks = dbHelper.hideExpiredTasks(globalUser)
            tasks.clear()
            tasks.addAll(validTasks)
        }
        else {
            val updatedTasks = dbHelper.showAllTasks(globalUser)
            tasks.clear()
            tasks.addAll(updatedTasks)
        }
        taskAdapter.notifyDataSetChanged()
        swipeRefreshLayout.isRefreshing = false
    }

    /**
     * This method refreshes the data in the RecyclerView
     */
    private fun refreshData() {
        loadData()
    }

    /**
     * This method inflates the menu for the tasks with the add task option
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * This method handles the click event for the add task option
     * It starts the AddTask activity
     * It also refreshes the task list after adding a task
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_task -> {
                val i = Intent(activity, TaskList_AddTask::class.java)
                i.putExtra("user", globalUser)
                startActivityForResult(i, ADD_TASK_REQUEST_CODE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * This method handles the result of the activity
     * It refreshes the data after adding or editing a task
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            refreshData()
        }
        if (requestCode == EDIT_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            refreshData()
        }
    }

    /**
     * This method is called when the edit icon is clicked
     * It starts the EditTask activity and passes the task details to it
     */
    override fun iconEditTask(position: Int, context: Context) {
        val task = tasks[position]
        val intent = Intent(context, TaskList_EditTask::class.java).apply {
            putExtra("taskID", task.taskID)
            putExtra("taskTitle", task.taskTitle)
            putExtra("taskDescription", task.taskDescription)
            putExtra("startTime", task.startTime.toString())
            putExtra("endTime", task.endTime.toString())
        }
        startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
    }

    /**
     * This is the companion object that contains the request codes for adding and editing tasks
     */
    companion object {
        private const val ADD_TASK_REQUEST_CODE = 1001
        private const val EDIT_TASK_REQUEST_CODE = 1002
    }
}
