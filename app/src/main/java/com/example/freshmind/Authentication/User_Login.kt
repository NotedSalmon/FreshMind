package com.example.freshmind.Authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.R
import com.example.freshmind.UI.Starter
import com.example.wagonersexperts.extra.SHAEncryption.shaEncrypt
var globalUser: String = "" // Global variable to store the username
class User_Login : AppCompatActivity() {
    val dbHelper: DBHelper = DBHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun btnUserRegister(view: View)
    {
        val intent = Intent(this, User_Register::class.java)
        startActivity(intent)
    }

    fun btnUserLogin(view: View)
    {
        val username = findViewById<EditText>(R.id.txtUser_Login_Username).text.toString()
        val password = shaEncrypt(findViewById<EditText>(R.id.txtUser_Login_Password).text.toString())

        if (dbHelper.validateUser(username,password)) {
            // Credentials are valid so should go to Menu Activity
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            globalUser = username //Sets the Global Customer as the Username

            val intent = Intent(this@User_Login, Starter::class.java)
            intent.putExtra("customer", globalUser) //This extra is how the global customer will be sent between activities
            startActivity(intent)
        } else {
            Toast.makeText(this, "Username or Password Incorrect", Toast.LENGTH_SHORT).show()

        }
    }
}
