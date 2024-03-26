package com.example.freshmind.UI.Task

import android.app.Activity
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
import com.example.freshmind.R


class TaskListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private val tasks: MutableList<Task_DataFiles> = mutableListOf() // Initialize an empty list
    private lateinit var dbHelper: DBHelper
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DBHelper(requireContext()) // Initialize DBHelper in onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)


        tasks.addAll(getAllTasks())

        taskAdapter = TaskAdapter(tasks)
        recyclerView.adapter = taskAdapter

        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        loadData()
        return view
    }
    private fun loadData() {
        val updatedTasks = dbHelper.showAllTasks(globalUser)
        tasks.clear()
        tasks.addAll(updatedTasks)
        taskAdapter.notifyDataSetChanged()
        swipeRefreshLayout.isRefreshing = false
    }

    private fun refreshData() {
        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getAllTasks() : MutableList<Task_DataFiles> {
        val allTasks = mutableListOf<Task_DataFiles>()
        dbHelper.showAllTasks(globalUser).forEach {
            allTasks.add(it)
        }
        return allTasks
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_task -> {
                val i = Intent(activity, TaskList_AddTask::class.java)
                i.putExtra("user", globalUser)
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Task added successfully, refresh the task list
            tasks.clear()
            tasks.addAll(getAllTasks())
            taskAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        private const val ADD_TASK_REQUEST_CODE = 1001
    }
}
