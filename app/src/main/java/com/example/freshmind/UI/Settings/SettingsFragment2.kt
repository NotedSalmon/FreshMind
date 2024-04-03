package com.example.freshmind.UI.Settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.freshmind.Authentication.User_Login
import com.example.freshmind.Authentication.globalUser
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.R
import com.example.wagonersexperts.extra.SHAEncryption.shaEncrypt

var isExpiredTasksEnabled: Boolean = false
class SettingsFragment2 : Fragment() {

    private lateinit var dbHelper: DBHelper
    private lateinit var newUsername: EditText
    private lateinit var newEmail: EditText
    private lateinit var oldPassword: EditText
    private lateinit var newPassword: EditText
    private lateinit var hideTasks: CheckBox
    private lateinit var btnSaveChanges: Button
    private lateinit var deleteAccount: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DBHelper(requireContext()) // Initialize DBHelper in onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        newUsername = view.findViewById(R.id.txtNewUsername)
        newEmail = view.findViewById(R.id.txtNewEmail)
        oldPassword = view.findViewById(R.id.txtOldPassword)
        newPassword = view.findViewById(R.id.txtNewPassword)
        hideTasks = view.findViewById(R.id.hiddenTaskCheckbox)
        deleteAccount = view.findViewById(R.id.btnDeleteAccount)

        deleteAccount.setOnClickListener {
            deleteButton()
        }
        hideTasks.setOnCheckedChangeListener { _, isChecked ->
            isExpiredTasksEnabled = isChecked
        }

        btnSaveChanges = view.findViewById(R.id.btnChangeDetails)

        btnSaveChanges.setOnClickListener {
            saveDetails()
        }

        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_settings, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun saveDetails(){
        val newUsernameText = newUsername.text.toString().trim()
        val newEmailText = newEmail.text.toString().trim()
        var oldPasswordText = oldPassword.text.toString().trim()
        var newPasswordText = newPassword.text.toString().trim()

        oldPasswordText = shaEncrypt(oldPasswordText)
        newPasswordText = shaEncrypt(newPasswordText)

        // Check if password fields are not empty and perform password change
        if (oldPasswordText.isNotEmpty() && newPasswordText.isNotEmpty()) {
            if (dbHelper.changePassword(oldPasswordText, newPasswordText, globalUser)){
                Toast.makeText(requireContext(), "Password changed successfully", Toast.LENGTH_SHORT).show()
                }
            else{
                Toast.makeText(requireContext(), "Password change failed", Toast.LENGTH_SHORT).show()
            }
        }

        if (newUsernameText.isNotEmpty()) {
            if (dbHelper.changeUsername(globalUser, newUsernameText)){
                globalUser = newUsernameText
                Toast.makeText(requireContext(), "Username changed, log in again", Toast.LENGTH_SHORT).show()
                logOut()
            }
            else{
                Toast.makeText(requireContext(), "Username taken/invalid", Toast.LENGTH_SHORT).show()
            }
        }

        if (newEmailText.isNotEmpty()) {
            // Perform email change here
            // Example:
            // changeEmail(newEmailText)
            Toast.makeText(requireContext(), "Email changed", Toast.LENGTH_SHORT).show()
        }
    }


    private fun deleteButton() {
        dbHelper.deleteUser(globalUser)
    }

    private fun logOut() {
        val i = Intent(activity, User_Login::class.java)
        globalUser = ""
        startActivity(i)
    }

}
