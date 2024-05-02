package com.example.freshmind.UI.Settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.freshmind.Authentication.User_Login
import com.example.freshmind.Authentication.globalUser
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Extras.changeAccountColour
import com.example.freshmind.Extras.changeButtonColor
import com.example.freshmind.Extras.changeEditBoxColor
import com.example.freshmind.Extras.changeSpinner
import com.example.freshmind.Extras.changeSpinnerTextBox
import com.example.freshmind.Extras.changeTextBoxColor
import com.example.freshmind.Extras.changeTextColors
import com.example.freshmind.Extras.changeTextColorsNT
import com.example.freshmind.Extras.getColorResource
import com.example.freshmind.R
import com.example.freshmind.UI.Calendar.Utils.makeGone
import com.example.freshmind.UI.Calendar.Utils.makeVisible
import com.example.freshmind.UI.Starter
import com.example.freshmind.UI.globalTheme
import com.example.wagonersexperts.extra.SHAEncryption.shaEncrypt

var isExpiredTasksEnabled: Boolean = false // Global variable to store the state of the hiddenTask checkbox

/**
 * This class is the fragment for the settings page
 * It allows the user to change their username, email, password, and delete their account
 */
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
        btnSaveChanges = view.findViewById(R.id.btnChangeDetails)

        /**
         * Spinner for changing the theme of the app
         * This is a simple spinner that allows the user to select a theme from a list of themes
         */
        val themes = arrayOf("midnight", "light", "dark")
        val adapter = SpinnerAdapter(requireContext(), themes)
        themeSpinner.adapter = adapter

        val themeIndex = themes.indexOf(globalTheme)
        if (themeIndex != -1) {
            themeSpinner.setSelection(themeIndex)
        }

        /**
         * OnItemSelectedListener for the theme spinner. Stores selection in globalTheme and updates the theme
         */
        themeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                globalTheme = themes[position]
                dbHelper.updateTheme(globalTheme)
                applyTheme()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        /**
         * Checkbox for hiding expired tasks
         * This checkbox allows the user to hide tasks that are expired
         */
        hideTasks.isChecked = isExpiredTasksEnabled
        hideTasks.setOnCheckedChangeListener { _, isChecked ->
            isExpiredTasksEnabled = isChecked
            dbHelper.updateHideTasks(isExpiredTasksEnabled)
        }

        deleteAccount.setOnClickListener {
            deleteButton()
        }

        btnSaveChanges.setOnClickListener {
            saveDetails()
        }

        //Change colours according to the theme
        applyTheme()
        getUserDetails()
        return view
    }

    /**
     * When the view is destroyed, make the floating fab visible again
     */
    override fun onDestroyView() {
        super.onDestroyView()
        val starterActivity = activity as? Starter
        starterActivity?.floatingFab?.makeVisible()
    }

    //Uses the menu_settings.xml file to create the menu in the settings fragment
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_settings, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //Handles the menu item clicks which logs out the user
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * This function applies the theme to the settings fragment depending on the theme selected by the user
     */
    private fun applyTheme() {
        // Apply theme to the entire fragment's view
        view?.setBackgroundColor(ContextCompat.getColor(requireContext(), getColorResource(requireContext())))

        // Update colors of specific views based on the selected theme
        changeSpinner(requireContext(), themeSpinner)
        changeTextColors(requireContext(), txtchangeUsername, txtchangeEmail, txtUserDetails, txtchangePassword, hideTasks, txtTheme)
        changeButtonColor(requireContext(), btnSaveChanges, deleteAccount)
        changeAccountColour(requireContext(), titleAccountSettings)
        changeEditBoxColor(requireContext(), newUsername, newEmail, oldPassword, newPassword)
        changeTextBoxColor(requireContext(), displayFullName, displayEmail, displayPhone)

        // Notify the activity to update its theme
        val starterActivity = activity as? Starter
        starterActivity?.updateTheme()
    }


    /**
     * This function saves the changes made by the user to their account details
     * It checks if the user has entered a new username, email, password, or all three
     * It then updates the database with the new details and displays a toast message to the user
     */
    private fun saveDetails(){
        val newUsernameText = newUsername.text.toString()
        val newEmailText = newEmail.text.toString()
        val oldPasswordText = oldPassword.text.toString()
        val newPasswordText = newPassword.text.toString()
        //Encrypt the passwords
        val encryptOldPasswordText = shaEncrypt(oldPasswordText)
        val encryptNewPasswordText = shaEncrypt(newPasswordText)

        //Check if the user has entered a new username, email, password, or all three
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
        else if (newEmailText.isNotEmpty()) {
            if (!newEmailText.contains("@") || !newEmailText.contains(".")){
                Toast.makeText(requireContext(), "Invalid Email", Toast.LENGTH_SHORT).show()
            }
            else {
                dbHelper.changeEmail(globalUser, newEmailText)
                Toast.makeText(requireContext(), "Email changed", Toast.LENGTH_SHORT).show()
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
        else {
            Toast.makeText(requireContext(), "No changes made", Toast.LENGTH_SHORT).show()
        }
        refreshData()
    }

    //Refreshes the data in the settings fragment
    private fun refreshData(){
        oldPassword.text.clear()
        newPassword.text.clear()
        newUsername.text.clear()
        newEmail.text.clear()
        getUserDetails()
    }

    //Gets the user details from the database
    private fun getUserDetails() {
        val userDetails = dbHelper.getUser(globalUser)
        displayFullName.text = userDetails.FullName
        displayEmail.text = userDetails.Email
        displayPhone.text = userDetails.PhoneNo
    }

    //Deletes the user account
    private fun deleteButton() {
        dbHelper.deleteUser(globalUser)
    }

    //Logs out the user
    private fun logOut() {
        val i = Intent(activity, User_Login::class.java)
        globalUser = ""
        startActivity(i)
    }

}

class SpinnerAdapter(private val context: Context, private val themes: Array<String>) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: inflater.inflate(R.layout.spinner_item_layout, parent, false)
        val textView = view.findViewById<TextView>(R.id.txtSpinnerItem)
        textView.text = themes[position]
        changeSpinnerTextBox(context, textView)
        return view
    }
    override fun getItem(position: Int): Any {
        return themes[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getCount(): Int {
        return themes.size
    }
}

