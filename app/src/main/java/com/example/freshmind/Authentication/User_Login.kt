package com.example.freshmind.Authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Extras.changeButtonColor
import com.example.freshmind.Extras.changeEditBoxColor
import com.example.freshmind.Extras.changeTextBoxColor
import com.example.freshmind.Extras.changeTextColors
import com.example.freshmind.Extras.changeTextColorsNT
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

    /**
     * This function is called when the activity is created
     * It sets the content view to the activity_login layout
     * It also sets the background color of the layout
     */
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
        changeButtonColor(this, btnRegister, btnLogin)
    }

    /**
     * This function is called when the user clicks the Register button
     * It creates an intent to go to the User_Register activity
     */
    fun btnUserRegister(view: View)
    {
        val intent = Intent(this, User_Register::class.java)
        startActivity(intent)
    }

    /**
     * This function is called when the user clicks the Login button
     * It gets the username and password from the EditTexts
     * It checks if the username and password are valid
     * If they are valid, it goes to the Menu activity
     * If they are not valid, it shows a toast message
     */
    fun btnUserLogin(view: View) {
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        // Validate username and password
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
        // Attempt to authenticate user
        try {
            if (dbHelper.validateUser(username, shaEncrypt(password))) {
                // Credentials are valid, proceed to Menu Activity
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                globalUser = username
                startMenuActivity()
            } else {
                // Invalid credentials
                Toast.makeText(this, "Username or Password Incorrect", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // Handle authentication errors
            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show()
            Log.e("User_Login", "Error during login: ${e.message}", e)
        }
    }
    // Function to start the Menu Activity
    private fun startMenuActivity() {
        val intent = Intent(this, Starter::class.java)
        intent.putExtra("user", globalUser)
        startActivity(intent)
    }
}
