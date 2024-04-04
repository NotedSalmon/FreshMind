package com.example.freshmind.UI.Settings

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
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.freshmind.Authentication.User_Login
import com.example.freshmind.Authentication.globalUser
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.R
import com.example.wagonersexperts.extra.SHAEncryption.shaEncrypt

var isExpiredTasksEnabled: Boolean = false
class SettingsFragment : Fragment() {

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

    private fun saveDetails(){
        //Toast.makeText(requireContext(), "Details saved", Toast.LENGTH_SHORT).show()
        val newUsernameText = newUsername.text.toString()
        val newEmailText = newEmail.text.toString()
        val oldPasswordText = oldPassword.text.toString()
        val newPasswordText = newPassword.text.toString()

        println("Old Password: $oldPasswordText")
        println("New Password: $newPasswordText")

        val encryptOldPasswordText = shaEncrypt(oldPasswordText)
        val encryptNewPasswordText = shaEncrypt(newPasswordText)

        if (oldPasswordText.isNotEmpty() && newPasswordText.isNotEmpty()) {
            if (oldPasswordText.length > 8){
                if (dbHelper.changePassword(encryptOldPasswordText, encryptNewPasswordText, globalUser)){
                    Toast.makeText(requireContext(), "Password changed successfully", Toast.LENGTH_SHORT).show()
                }
                else{
                    oldPassword.error = "Password is incorrect or does not match"
                    newPassword.error = "Password is less than 8 characters or does not match"
                }
            }
            else{
                oldPassword.error = "Password is incorrect or does not match"
                newPassword.error = "Password is less than 8 characters or does not match"
            }
        }
        else if (newUsernameText.isNotEmpty()) {
            if (dbHelper.changeUsername(globalUser, newUsernameText)){
                globalUser = newUsernameText
                Toast.makeText(requireContext(), "Username changed, log in again", Toast.LENGTH_SHORT).show()
                logOut()
            }
            else{
                Toast.makeText(requireContext(), "Username taken/invalid", Toast.LENGTH_SHORT).show()
            }
        }
        else if (newEmailText.isNotEmpty()) {
            // Perform email change here
            // Example:
            // changeEmail(newEmailText)
            Toast.makeText(requireContext(), "Email changed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(requireContext(), "No changes made", Toast.LENGTH_SHORT).show()
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
