package com.example.freshmind.UI.Settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat.applyTheme
import androidx.fragment.app.Fragment
import com.example.freshmind.Authentication.User_Login
import com.example.freshmind.Authentication.globalUser
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Extras.changeAccountColour
import com.example.freshmind.Extras.changeEditBoxColor
import com.example.freshmind.Extras.changeTextBoxColor
import com.example.freshmind.Extras.changeTextColors
import com.example.freshmind.Extras.getColorResource
import com.example.freshmind.R
import com.example.freshmind.UI.Calendar.Utils.makeGone
import com.example.freshmind.UI.Calendar.Utils.makeVisible
import com.example.freshmind.UI.Starter
import com.example.freshmind.UI.globalTheme
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
    private lateinit var displayFullName : TextView
    private lateinit var displayEmail : TextView
    private lateinit var displayPhone : TextView
    private lateinit var txtchangeUsername: TextView
    private lateinit var txtchangeEmail: TextView
    private lateinit var txtchangePassword: TextView
    private lateinit var txtUserDetails: TextView
    private lateinit var titleAccountSettings: TextView
    private lateinit var txtTheme: TextView
    private lateinit var themeSpinner: Spinner

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
        view?.setBackgroundColor(resources.getColor(getColorResource(requireContext())))

        val starterActivity = activity as? Starter
        starterActivity?.floatingFab?.makeGone()

        newUsername = view.findViewById(R.id.txtNewUsername)
        newEmail = view.findViewById(R.id.txtNewEmail)
        oldPassword = view.findViewById(R.id.txtOldPassword)
        newPassword = view.findViewById(R.id.txtNewPassword)
        hideTasks = view.findViewById(R.id.hiddenTaskCheckbox)
        deleteAccount = view.findViewById(R.id.btnDeleteAccount)
        displayFullName = view.findViewById(R.id.txtUserDetails_FullName)
        displayEmail = view.findViewById(R.id.txtUserDetails_Email)
        displayPhone = view.findViewById(R.id.txtUserDetails_PhoneNumber)
        txtchangeUsername = view.findViewById(R.id.txtChangeUsername)
        txtchangeEmail = view.findViewById(R.id.txtChangeEmail)
        txtchangePassword = view.findViewById(R.id.txtChangePassword)
        txtUserDetails = view.findViewById(R.id.txtUserDetails)
        titleAccountSettings = view.findViewById(R.id.titleAccountSettings)
        txtTheme = view.findViewById(R.id.txtTheme)
        themeSpinner = view.findViewById(R.id.theme_spinner)

        /**
         * Spinner for changing the theme of the app
         * This is a simple spinner that allows the user to select a theme from a list of themes
         */
        val themes = arrayOf("midnight", "light", "dark")
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, themes)
        adapter.setDropDownViewResource(R.layout.spinner_item_layout)
        themeSpinner.adapter = adapter

        val themeIndex = themes.indexOf(globalTheme)
        if (themeIndex != -1) {
            themeSpinner.setSelection(themeIndex)
        }


        themeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Handle theme selection
                globalTheme = themes[position]
                dbHelper.updateTheme(globalTheme)
                applyTheme()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        deleteAccount.setOnClickListener {
            deleteButton()
        }

        hideTasks.isChecked = isExpiredTasksEnabled
        hideTasks.setOnCheckedChangeListener { _, isChecked ->
            isExpiredTasksEnabled = isChecked
            dbHelper.updateHideTasks(isExpiredTasksEnabled)
        }

        btnSaveChanges = view.findViewById(R.id.btnChangeDetails)

        btnSaveChanges.setOnClickListener {
            saveDetails()
        }
        changeTextColors(requireContext(), txtchangeUsername, txtchangeEmail, txtUserDetails, txtchangePassword, deleteAccount, hideTasks,btnSaveChanges, txtTheme)
        changeAccountColour(requireContext(), titleAccountSettings)
        changeEditBoxColor(requireContext(), newUsername, newEmail, oldPassword, newPassword)
        changeTextBoxColor(requireContext(), displayFullName, displayEmail, displayPhone)
        getUserDetails()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val starterActivity = activity as? Starter
        starterActivity?.floatingFab?.makeVisible()
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

    private fun applyTheme() {
        view?.setBackgroundColor(resources.getColor(getColorResource(requireContext())))
        changeTextColors(requireContext(), txtchangeUsername, txtchangeEmail, txtUserDetails, txtchangePassword, deleteAccount, hideTasks,btnSaveChanges)
        changeAccountColour(requireContext(), titleAccountSettings)
        changeEditBoxColor(requireContext(), newUsername, newEmail, oldPassword, newPassword)
        changeTextBoxColor(requireContext(), displayFullName, displayEmail, displayPhone)
    }

    private fun saveDetails(){
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
            dbHelper.changeEmail(globalUser, newEmailText)
            Toast.makeText(requireContext(), "Email changed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(requireContext(), "No changes made", Toast.LENGTH_SHORT).show()
        }
        refreshData()
    }

    private fun refreshData(){
        oldPassword.text.clear()
        newPassword.text.clear()
        newUsername.text.clear()
        newEmail.text.clear()
        getUserDetails()
    }

    private fun getUserDetails() {
        val userDetails = dbHelper.getUser(globalUser)
        displayFullName.text = userDetails.FullName
        displayEmail.text = userDetails.Email
        displayPhone.text = userDetails.PhoneNo
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
