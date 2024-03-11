package com.example.freshmind.UI.Task

import TaskAdapter
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
import com.example.freshmind.Authentication.globalUser
import com.example.freshmind.Database.Task_DataFiles
import com.example.freshmind.R


class TaskListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private val tasks: MutableList<Task_DataFiles> = mutableListOf() // Initialize an empty list

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        tasks.addAll(generateDummyTasks())

        taskAdapter = TaskAdapter(tasks)
        recyclerView.adapter = taskAdapter

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun generateDummyTasks(): MutableList<Task_DataFiles> {
        // Replace this with your actual data retrieval logic
        val dummyTasks = mutableListOf<Task_DataFiles>()
        dummyTasks.addAll(
            listOf(
                Task_DataFiles(1, 1, "Task 1", "Description for Task 1", null, null, null),
                Task_DataFiles(2, 1, "Task 2", "Description for Task 2", null, null, null),
                Task_DataFiles(3, 1, "Task 3", "Description for Task 3", null, null, null)
                // Add more tasks as needed
            )
        )
        return dummyTasks
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_task -> {
                val i = Intent(activity, TaskList_AddTaskFragment::class.java)
                i.putExtra("user", globalUser)
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
