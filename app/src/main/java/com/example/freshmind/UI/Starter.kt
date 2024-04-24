package com.example.freshmind.UI

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.freshmind.UI.Calendar.Utils.makeGone
import com.example.freshmind.UI.Calendar.Utils.makeVisible
import com.example.freshmind.databinding.ActivityWelcomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.time.LocalDate
import com.example.freshmind.R

var globalTheme : String = "midnight" // Global variable to store the theme
class Starter : AppCompatActivity() {
    /**
     * This is the main activity of the app. It is the entry point of the app and contains the
     * navigation drawer and the toolbar. The activity also contains the floating action button.
     * The activity also contains the global variable globalUser which is used to store the username
     */
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityWelcomeBinding
    lateinit var floatingFab: FloatingActionButton
    var globalUser: String = "" // Global variable to store the username
    private val db = Firebase.firestore
    private val feedbackRef = db.collection("feedback") // Reference to the feedback collection
    private val TAG = "Feedback"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarWelcome.toolbar)
        globalUser = intent.getStringExtra("user")
            .toString() //This extra is how the global customer will be sent between activities

        binding.appBarWelcome.fab.setOnClickListener {
            sendFeedback()
        }

        floatingFab = binding.appBarWelcome.fab

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_welcome,
                R.id.nav_tasks,
                R.id.nav_calendar,
                R.id.nav_notes,
                R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Update the navigation header when globalUser changes
        updateNavigationHeader()
    }

    // Function to update the navigation header with the current globalUser
    private fun updateNavigationHeader() {
        val headerView = binding.navView.getHeaderView(0)
        val txtUserUsername: TextView = headerView.findViewById(R.id.txtUserUsername)
        txtUserUsername.text = globalUser
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * This function is called when the user clicks on the floating action button. It opens a
     * BottomSheetDialog where the user can enter feedback. The feedback is then sent to the
     * Firestore database which can then be viewed in the Website I created.
     */
    private fun sendFeedback() {
        // Create a BottomSheetDialog
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.feedback_bottom_sheet, null)
        dialog.setContentView(view)

        // Find views in the dialog
        val feedbackText = view.findViewById<EditText>(R.id.feedback_edit_text)
        val feedbackSubmit = view.findViewById<Button>(R.id.submit_feedback_button)

        // Set click listener for submit button
        feedbackSubmit.setOnClickListener {
            val feedback = feedbackText.text.toString()
            if (feedback.isNotEmpty()) {
                // Construct feedback data using the variables assigned
                val date = LocalDate.now()
                val feedbackData = hashMapOf(
                    "username" to globalUser,
                    "feedback" to feedback,
                    "date" to date.toString() // Convert LocalDate to String
                )

                // Add feedback data to Firestore
                feedbackRef
                    .add(feedbackData)
                    .addOnSuccessListener {documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        Snackbar.make(view, "Feedback sent", Snackbar.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                        Snackbar.make(view, "Failed to send feedback", Snackbar.LENGTH_SHORT).show()
                    }
            } else {
                feedbackText.error = "Feedback cannot be empty"
            }
        }
        dialog.show()
    }
}

    interface HasToolbar {
    val toolbar: Toolbar? // Return null to hide the toolbar
}

interface HasBackButton

/**
 * This fragment was created for the purpose of the Calendar as it is the only fragment that
 * requires a toolbar. This fragment is used to set the toolbar to the activity's toolbar when the
 * fragment is started and to set the activity's toolbar back to the activity's toolbar when the
 * fragment is stopped.
 */
abstract class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {
    abstract val titleRes: Int?
    val activityToolbar: Toolbar
        get() = (requireActivity() as Starter).binding.appBarWelcome.toolbar

    override fun onStart() {
        super.onStart()
        if (this is HasToolbar) {
            activityToolbar.makeGone()
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        }

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        if (this is HasBackButton) {
            actionBar?.title = if (titleRes != null) context?.getString(titleRes!!) else ""
            actionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            actionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

    override fun onStop() {
        super.onStop()
        if (this is HasToolbar) {
            activityToolbar.makeVisible()
            (requireActivity() as AppCompatActivity).setSupportActionBar(activityToolbar)
        }

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        if (this is HasBackButton) {
        }
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
