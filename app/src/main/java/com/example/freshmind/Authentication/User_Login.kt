package com.example.freshmind.Authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Extras.changeAccountColour
import com.example.freshmind.Extras.changeEditBoxColor
import com.example.freshmind.Extras.changeTextBoxColor
import com.example.freshmind.Extras.changeTitleColor
import com.example.freshmind.Extras.getColorResource
import com.example.freshmind.R
import com.example.freshmind.UI.Starter
import com.example.wagonersexperts.extra.SHAEncryption.shaEncrypt
var globalUser: String = "" // Global variable to store the username
class User_Login : AppCompatActivity() {
    val dbHelper: DBHelper = DBHelper(this)
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var btnRegister : Button
    private lateinit var btnLogin : Button
    private lateinit var title: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val rootLayout = findViewById<ConstraintLayout>(R.id.loginLayout)
        rootLayout.setBackgroundColor(resources.getColor(getColorResource(this)))


        usernameEditText = findViewById(R.id.txtUser_Login_Username)
        passwordEditText = findViewById(R.id.txtUser_Login_Password)
        btnRegister = findViewById(R.id.btnUser_Register)
        btnLogin = findViewById(R.id.btnUser_Login)
        title = findViewById(R.id.txtTitle)
        changeTitleColor(this, title)
        changeEditBoxColor(this, usernameEditText, passwordEditText)
        changeTextBoxColor(this, btnRegister, btnLogin)
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

        if (username.isEmpty()) {
            usernameEditText.error = "Username is required"
            usernameEditText.requestFocus()
            return
        }
        if (password.isEmpty()) {
            passwordEditText.error = "Password is required"
            passwordEditText.requestFocus()
            return
        }

        if (dbHelper.validateUser(username,password)) {
            // Credentials are valid so should go to Menu Activity
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            globalUser = username //Sets the Global Customer as the Username

            val intent = Intent(this@User_Login, Starter::class.java)
            intent.putExtra("user", globalUser) //This extra is how the global customer will be sent between activities
            startActivity(intent)
        } else {
            Toast.makeText(this, "Username or Password Incorrect", Toast.LENGTH_SHORT).show()

        }
    }
}
