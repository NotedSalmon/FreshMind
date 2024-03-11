package com.example.freshmind.Authentication

import android.content.Intent
import android.os.Build.VERSION_CODES.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.User_DataFiles
import com.example.freshmind.R
import com.example.wagonersexperts.extra.SHAEncryption.shaEncrypt
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.google.firebase.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

private const val TAG = "User_Register"

class User_Register: AppCompatActivity() {

    val dbHelper: DBHelper = DBHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.freshmind.R.layout.activity_register)
    }

    fun btnUserRegister(view: View)
    {
        val fullNameEditText = findViewById<EditText>(com.example.freshmind.R.id.txtFullName)
        val emailEditText = findViewById<EditText>(com.example.freshmind.R.id.txtEmail)
        val phoneNumberEditText = findViewById<EditText>(com.example.freshmind.R.id.txtPhoneNumber)
        val usernameEditText = findViewById<EditText>(com.example.freshmind.R.id.txtUsername)
        val passwordEditText = findViewById<EditText>(com.example.freshmind.R.id.txtPassword)

        val userName = fullNameEditText.text.toString()
        val userEmail = emailEditText.text.toString()
        val userPhone = phoneNumberEditText.text.toString()
        val userUsername = usernameEditText.text.toString()
        val validateUserPassword = passwordEditText.text.toString()
        val userPassword = shaEncrypt(passwordEditText.text.toString()) // Encryption used for password only
        val userIsActive = 1

        if (userName.isEmpty()) {
            fullNameEditText.error = "Full Name is required"
            fullNameEditText.requestFocus()
            return
        }

        if (userEmail.isEmpty() || !userEmail.contains("@") || !userEmail.contains(".")){
            emailEditText.error = "Invalid Email Used"
            emailEditText.requestFocus()
            return
        }

        if (userPhone.isEmpty()) {
            phoneNumberEditText.error = "Phone Number is required"
            phoneNumberEditText.requestFocus()
            return
        }

        if (userUsername.isEmpty() || dbHelper.checkUsername(userUsername)){
            usernameEditText.error = "Username Invalid or Already Taken"
            usernameEditText.requestFocus()
            return
        }

        if (validateUserPassword.isEmpty() || validateUserPassword.length < 8) {
            passwordEditText.error = "Password must be at least 8 characters"
            passwordEditText.requestFocus()
            return
        }


        val localTime = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss"))

        val dateCreated: LocalDateTime = LocalDateTime.parse(localTime, DateTimeFormatter.ofPattern("yyyy-MM-dd/-/HH:mm:ss"))


        val customer = User_DataFiles(0,userName,userEmail,userPhone,userUsername, userPassword, userIsActive, dateCreated, dateCreated)
        if(dbHelper.addUser(customer)){ //Calls function addCustomer within the dbHelper
            Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, User_Login::class.java) //Sends user to the Login Activity
            startActivity(intent)
        }
        else Toast.makeText(this, "Error: Account not created", Toast.LENGTH_SHORT).show() //Error message

    }

    fun btnGoBack(view: View)
    {
        val intent = Intent(this, User_Login::class.java)
        startActivity(intent)
    }

    fun btnTestFirebase(view: View)
    {


    }
}
