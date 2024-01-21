package com.example.freshmind.Authentication

import android.content.Intent
import android.os.Bundle
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


class User_Register: AppCompatActivity() {
    val dbHelper: DBHelper = DBHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun btnUserRegister(view: View)
    {
        val userName = findViewById<EditText>(R.id.txtFullName).text.toString()
        val userEmail = findViewById<EditText>(R.id.txtEmail).text.toString()
        val userPhone = findViewById<EditText>(R.id.txtPhoneNumber).text.toString()
        val userUsername = findViewById<EditText>(R.id.txtUsername).text.toString()
        val userPassword = shaEncrypt(findViewById<EditText>(R.id.txtPassword).text.toString()) //Encryption used for password only
        val userIsActive = 1

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