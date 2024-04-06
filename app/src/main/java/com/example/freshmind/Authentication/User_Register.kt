package com.example.freshmind.Authentication

import android.content.Intent
import android.os.Build.VERSION_CODES.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.User_DataFiles
import com.example.freshmind.Extras.changeEditBoxColor
import com.example.freshmind.Extras.changeTextBoxColor
import com.example.freshmind.Extras.changeTitleColor
import com.example.freshmind.Extras.getColorResource
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
    private lateinit var fullnameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var btnGoBack : Button
    private lateinit var btnUserRegister : Button
    private lateinit var title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.freshmind.R.layout.activity_register)
        val rootLayout = findViewById<View>(android.R.id.content)
        rootLayout.setBackgroundColor(ContextCompat.getColor(this, getColorResource(this)))
        fullnameEditText = findViewById(com.example.freshmind.R.id.txtFullName)
        emailEditText = findViewById(com.example.freshmind.R.id.txtEmail)
        phoneNumberEditText = findViewById(com.example.freshmind.R.id.txtPhoneNumber)
        usernameEditText = findViewById(com.example.freshmind.R.id.txtUsername)
        passwordEditText = findViewById(com.example.freshmind.R.id.txtPassword)
        btnGoBack = findViewById(com.example.freshmind.R.id.btnBack)
        btnUserRegister = findViewById(com.example.freshmind.R.id.btnRegister)
        title = findViewById(com.example.freshmind.R.id.txtTitle)
        changeTitleColor(this, title)
        changeEditBoxColor(this, fullnameEditText, emailEditText, phoneNumberEditText, usernameEditText, passwordEditText)
        changeTextBoxColor(this, btnGoBack, btnUserRegister)
    }

    fun btnUserRegister(view: View)
    {
        val userName = fullnameEditText.text.toString()
        val userEmail = emailEditText.text.toString()
        val userPhone = phoneNumberEditText.text.toString()
        val userUsername = usernameEditText.text.toString()
        val validateUserPassword = passwordEditText.text.toString()
        val userPassword = shaEncrypt(passwordEditText.text.toString()) // Encryption used for password only
        val userIsActive = 1

        //Add a loop or something to check all fields, if theres any error fail everything
        if (userName.isEmpty()) {
            fullnameEditText.error = "Full Name is required"
            fullnameEditText.requestFocus()
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

}
