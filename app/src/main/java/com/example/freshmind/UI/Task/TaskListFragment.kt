package com.example.freshmind.UI.Task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshmind.Database.Task_DataFiles
import com.example.freshmind.R

class TaskListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val tasks = generateDummyTasks()
        taskAdapter = TaskAdapter(tasks)
        recyclerView.adapter = taskAdapter

        return view
    }

    private fun generateDummyTasks(): List<Task_DataFiles> {
        // Replace this with your actual data retrieval logic
        return listOf(
            Task_DataFiles(1, "Task 1", "Description for Task 1", null, null, null),
            Task_DataFiles(2, "Task 2", "Description for Task 2", null, null, null),
            Task_DataFiles(3, "Task 3", "Description for Task 3", null, null, null)
            // Add more tasks as needed
        )
    }
}
