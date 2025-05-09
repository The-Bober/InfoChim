package com.infochim.ChemLearn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminPanelAccessActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var adminPanelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings) // Replace with your layout file

        // Initialize Firebase references
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("admins")

        adminPanelButton = findViewById(R.id.adminPanelButton)

        // Check if the current user is admin
        checkAdminAccess()
    }

    private fun checkAdminAccess() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // If no user is logged in, deny access
            denyAccess()
            return
        }

        val userId = currentUser.uid

        database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // User is an admin, allow access
                    allowAccess()
                } else {
                    // User is not an admin, deny access
                    denyAccess()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle potential errors
                Toast.makeText(
                    this@AdminPanelAccessActivity,
                    "Error checking admin access: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
                denyAccess()
            }
        })
    }

    private fun allowAccess() {
        adminPanelButton.visibility = View.VISIBLE
        adminPanelButton.setOnClickListener {
            // Navigate to the Admin Panel
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }
    }

    private fun denyAccess() {
        adminPanelButton.visibility = View.GONE
        Toast.makeText(
            this,
            "Access Denied: You do not have permission to access the Admin Panel.",
            Toast.LENGTH_SHORT
        ).show()
    }
}