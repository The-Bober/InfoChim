package com.infochim.ChemLearn

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val adminPanelButton = findViewById<Button>(R.id.adminPanelButton)
        val darkModeSwitch = findViewById<Switch>(R.id.darkModeSwitch)
        val logoutButton = findViewById<Button>(R.id.logoutButton) // Add a Logout button

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("DarkMode", false)

        // Set initial state of the Dark Mode switch
        darkModeSwitch.isChecked = isDarkMode

        // Apply the current theme
        setDarkMode(isDarkMode)

        // Navigate to AdminActivity
        adminPanelButton.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }

        // Toggle Dark Mode
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            setDarkMode(isChecked)
            // Save the preference
            sharedPreferences.edit { putBoolean("DarkMode", isChecked) }
        }

        // Logout Button Click Listener
        logoutButton.setOnClickListener {
            auth.signOut() // Sign out the user from Firebase
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear activity stack
            startActivity(intent) // Redirect to LoginActivity
            finish() // Close the current activity
        }
    }

    private fun setDarkMode(enabled: Boolean) {
        if (enabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}