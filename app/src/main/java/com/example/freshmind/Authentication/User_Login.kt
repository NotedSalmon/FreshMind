package com.example.freshmind.Authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.freshmind.R

class User_Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun btnUserRegister(view: View)
    {
        val intent = Intent(this, User_Register::class.java)
        startActivity(intent)
    }
}
