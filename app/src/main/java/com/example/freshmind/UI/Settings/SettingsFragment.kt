package com.example.freshmind.UI.Settings

import android.app.AlertDialog
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.text.InputFilter
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.preference.*
import com.example.freshmind.Authentication.User_Login
import com.example.freshmind.Authentication.globalUser
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.R
//var isExpiredTasksEnabled: Boolean = false
class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var usernameTextView: TextView
    private lateinit var expiredTasksCheckBox: CheckBox
    private lateinit var usernamePreference: EditTextPreference
    private lateinit var dbHelper: DBHelper
    private lateinit var emailPreference: EditTextPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        setPreferencesFromResource(R.xml.preferences, rootKey)
        dbHelper = DBHelper(requireContext())
        //usernameTextView = view?.findViewById(R.id.textViewUsernameValue) ?: TextView(requireContext())
        // = view?.findViewById(R.id.checkBoxExpiredTasks) ?: CheckBox(requireContext())

        /**
         * Setting the Username values and validating the new username
         * it uses the DBHelper to check if the new username is already in use and if so will
         * call the showErrorDialog function to display an error message
         */
        usernamePreference = findPreference("key_username")!!
        usernamePreference.setOnBindEditTextListener { editText ->
            editText.filters = arrayOf(InputFilter.LengthFilter(20)) // Set max length if needed
        }
        usernamePreference.setOnPreferenceChangeListener { _, newValue ->
            val newUsername = newValue as String
            if (dbHelper.checkUsername(newUsername)) {
                showErrorDialog("New Username cannot be $newUsername")
                false // Return false to prevent saving
            } else {
                dbHelper.changeUsername(globalUser, newUsername)
                globalUser = newUsername
                logOut()
                Toast.makeText(requireContext(), "Username changed please Log In again", Toast.LENGTH_LONG).show()
                true
            }
        }

        /**
         * Setting the Email values and validating the new email
         */
        emailPreference = findPreference("key_username")!!
        emailPreference.setOnBindEditTextListener { editText ->
            editText.filters = arrayOf(InputFilter.LengthFilter(20)) // Set max length if needed
        }
        emailPreference.setOnPreferenceChangeListener { _, newValue ->
            val newEmail = newValue as String
            if (dbHelper.checkUsername(newEmail)) {
                showErrorDialog("Email already in use")
                false // Return false to prevent saving
            } else {
                dbHelper.changeEmail(globalUser, newEmail)
                globalUser = newEmail
                logOut()
                Toast.makeText(requireContext(), "Username changed please Log In again", Toast.LENGTH_LONG).show()
                true
            }
        }
    }

    private fun showErrorDialog(errorMessage: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage(errorMessage)
            .setPositiveButton("OK", null)
            .show()
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

    override fun onResume() {
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            "key_username" -> {
                displayUserSettings()
            }
            "key_email" -> {
                displayUserSettings()
            }
            "key_expiredTasks" -> {
                isExpiredTasksEnabled = sharedPreferences?.getBoolean(key, false) ?: false
            }
        }
    }

    private fun displayUserSettings() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val username = sharedPreferences.getString("key_username", "")
        val email = sharedPreferences.getString("key_email", "")
        val expiredTasks = sharedPreferences.getBoolean("key_expiredTasks", false)
        usernameTextView.text = username
        expiredTasksCheckBox.isChecked = expiredTasks

        isExpiredTasksEnabled = expiredTasks
        // Add logic to handle other user settings as needed
    }

    private fun logOut() {
        val i = Intent(activity, User_Login::class.java)
        globalUser = ""
        startActivity(i)
    }
}
