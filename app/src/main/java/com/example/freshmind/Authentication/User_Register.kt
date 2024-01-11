package com.example.freshmind.Authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.freshmind.R

class User_Register: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun btnUserRegister(view: View)
    {

    }

    fun btnGoBack(view: View)
    {
        val intent = Intent(this, User_Login::class.java)
        startActivity(intent)
    }
}