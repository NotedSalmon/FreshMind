package com.example.freshmind

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.freshmind.Authentication.User_Login

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
}

