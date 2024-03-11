package com.example.freshmind

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.freshmind.Authentication.User_Login
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun btnGo(view: View)
    {
        val intent = Intent(this, User_Login::class.java)
        startActivity(intent)
    }

    fun btnTest(view: View)
    {
        val intent = Intent(this, User_Login::class.java)
        startActivity(intent)
    }


    private fun saveTask() {
        // Extract data from EditText views
        //val title = dialogView.findViewById<EditText>(R.id.editTextTaskTitle).text.toString()
        // Extract other data for description, start date, end date, and reminder

        // Create a Task object or perform the necessary operations
        // Save the task to the database or handle it based on your app's logic
    }
}

