package com.example.freshmind.UI

import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.freshmind.R
import com.example.freshmind.UI.Calendar.Utils.makeGone
import com.example.freshmind.UI.Calendar.Utils.makeVisible
import com.example.freshmind.databinding.ActivityWelcomeBinding
import com.example.freshmind.databinding.ContentWelcomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class Starter : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    public lateinit var binding: ActivityWelcomeBinding
    var globalUser: String = "" // Global variable to store the username

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarWelcome.toolbar)
        globalUser = intent.getStringExtra("user").toString() //This extra is how the global customer will be sent between activities

        binding.appBarWelcome.fab.setOnClickListener { view ->
            Snackbar.make(view, "Will Send Feedback", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_welcome, R.id.nav_tasks, R.id.nav_calendar, R.id.nav_notes, R.id.nav_settings
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
}

interface HasToolbar {
    val toolbar: Toolbar? // Return null to hide the toolbar
}

interface HasBackButton

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
            actionBar?.title = context?.getString(R.string.activity_title_view)
        }
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
