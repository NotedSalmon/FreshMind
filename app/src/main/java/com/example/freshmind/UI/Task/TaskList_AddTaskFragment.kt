package com.example.freshmind.UI.Task


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.freshmind.R

class TaskList_AddTaskFragment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.freshmind.R.layout.activity_create_task)
    }

    private fun btnBackToFragment(view: View) {
        val intent = Intent(this, TaskListFragment::class.java)
        startActivity(intent)
    }

    private fun btnAddTask(view: View) {
        val taskTitle : EditText = findViewById(R.id.txtCreateTask_Title)
        val taskDescription : EditText = findViewById(R.id.txtCreateTask_Description)
    }

}
