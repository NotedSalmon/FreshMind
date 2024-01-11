package com.example.freshmind.UI.Settings

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.freshmind.R

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var usernameTextView: TextView

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // Set up theme preference summary
        val themePreference: ListPreference? = findPreference("key_theme")
        themePreference?.setOnPreferenceChangeListener { _, newValue ->
            themePreference.summary = getThemeSummary(newValue.toString())
            true
        }
        themePreference?.summary = getThemeSummary(themePreference?.value ?: "light")

        // Set up user settings display
        usernameTextView = view?.findViewById(R.id.textViewUsernameValue) ?: TextView(requireContext())
        displayUserSettings()
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
        if (key == "key_username") {
            displayUserSettings()
        }
        // Add more conditions for other preferences as needed
    }

    private fun getThemeSummary(themeValue: String): String {
        return when (themeValue) {
            "light" -> "Light Theme"
            "dark" -> "Dark Theme"
            else -> "Unknown Theme"
        }
    }

    private fun displayUserSettings() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val username = sharedPreferences.getString("key_username", "")
        usernameTextView.text = username
        // Add more TextViews and display logic for other user settings as needed
    }
}
